package ru.mherarsh.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "ms")
public class WsClientPropertiesImpl implements WsClientProperties {
    private String clientId;
    private String receiverId;
    private String serverEndpoint;
    private String producerEndpoint;
    private String subscriptionPrefix;
}
