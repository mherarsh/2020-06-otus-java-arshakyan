package ru.mherarsh.messagesystem;

import com.google.common.util.concurrent.Striped;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import ru.mherarsh.socket.WebSocketSessionStorage;

import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.locks.Lock;
import java.util.function.Function;

@Component
public class MsClientStorageImpl implements MsClientStorage {
    private static final Logger logger = LoggerFactory.getLogger(MsClientStorageImpl.class);

    private final WebSocketSessionStorage webSocketSessionStorage;
    private final Function<String, MsClientAdapter> msClientAdapterFactory;
    private final ConcurrentMap<String, String> sessionClientsStorage = new ConcurrentHashMap<>();
    private final ConcurrentMap<String, MsClientAdapter> msClientStorage = new ConcurrentHashMap<>();
    private final Striped<Lock> stripedLock = Striped.lazyWeakLock(256);

    public MsClientStorageImpl(WebSocketSessionStorage webSocketSessionStorage, Function<String, MsClientAdapter> msClientAdapterFactory) {
        this.webSocketSessionStorage = webSocketSessionStorage;
        this.msClientAdapterFactory = msClientAdapterFactory;
    }

    @Override
    public Optional<MsClientAdapter> getMqClientById(String clientId) {
        if (clientId == null) {
            return Optional.empty();
        }

        return Optional.ofNullable(msClientStorage.get(clientId));
    }

    @Override
    public void putClient(String sessionId, String clientId) {
        blockOperationById(clientId, () ->
                getMqClientById(clientId).ifPresentOrElse(
                        x -> updateClientSession(sessionId, clientId),
                        () -> addNewClient(sessionId, clientId)));
    }

    @Override
    public void deleteClient(String sessionId) {
        deleteSession(sessionId)
                .ifPresent(clientId -> blockOperationById(clientId,
                        () -> deleteMqClient(clientId)
                                .ifPresent(this::deleteClientFromMessageStore)));
    }

    private void updateClientSession(String newSessionId, String clientId) {
        var oldSessionId = getClientSessionId(clientId);
        sessionClientsStorage.remove(oldSessionId);
        sessionClientsStorage.put(newSessionId, clientId);

        webSocketSessionStorage.closeSessions(oldSessionId);

        logger.info("updateClientSession - oldSession {}, newSession {}", oldSessionId, newSessionId);
    }

    private String getClientSessionId(String clientId) {
        for (var entry : sessionClientsStorage.entrySet()) {
            if (clientId.equals(entry.getValue())) {
                return entry.getKey();
            }
        }

        return null;
    }

    private void deleteClientFromMessageStore(MsClientAdapter msClientAdapter) {
        if (msClientAdapter == null) {
            return;
        }

        msClientAdapter.deleteClientFromMessageStore();
    }

    private Optional<MsClientAdapter> deleteMqClient(String clientId) {
        if (clientId == null) {
            return Optional.empty();
        }

        return Optional.ofNullable(msClientStorage.remove(clientId));
    }

    private void addNewClient(String sessionId, String clientId) {
        var client = msClientAdapterFactory.apply(clientId);

        sessionClientsStorage.put(sessionId, clientId);
        msClientStorage.put(clientId, client);
    }

    private Optional<String> deleteSession(String sessionId) {
        if (sessionId == null) {
            return Optional.empty();
        }

        return Optional.ofNullable(sessionClientsStorage.remove(sessionId));
    }

    private void blockOperationById(String blockingId, Runnable task) {
        var lock = stripedLock.get(blockingId);
        lock.lock();

        try {
            task.run();
        } finally {
            lock.unlock();
        }
    }
}
