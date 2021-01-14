package ru.mherarsh.messagesystem;

import java.util.Optional;

public interface MsClientStorage {
     public Optional<MsClientAdapter> getMqClientById(String clientId);

     public void putClient(String sessionId, String clientId);

     public void deleteClient(String sessionId);
}
