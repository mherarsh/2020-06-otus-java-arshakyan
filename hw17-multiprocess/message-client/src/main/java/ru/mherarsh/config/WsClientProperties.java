package ru.mherarsh.config;

public interface WsClientProperties {
    String getClientId();

    String getReceiverId();

    String getServerEndpoint();

    String getProducerEndpoint();

    String getSubscriptionPrefix();

    default String getSubscriptionEndpoint() {
        return getSubscriptionPrefix() + "." + getClientId();
    }
}
