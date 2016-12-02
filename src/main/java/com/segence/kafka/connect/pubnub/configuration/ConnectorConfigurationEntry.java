package com.segence.kafka.connect.pubnub.configuration;

import org.apache.kafka.common.config.ConfigDef.Importance;
import org.apache.kafka.common.config.ConfigDef.Type;

import java.util.Optional;

public enum ConnectorConfigurationEntry {

    PUBLISH_KEY("publishKey", Optional.empty(), Type.STRING, Importance.HIGH, "The PubNub publish key used to publish messages."),
    SUBSCRIBE_KEY("subscribeKey", Optional.empty(),Type.STRING, Importance.HIGH, "The PubNub subscribe key used to publish messages."),
    CHANNEL("channel", Optional.empty(),Type.STRING, Importance.HIGH, "The PubNub channel to publish messages to."),
    USE_SECURE_CONNECTION("useSecureConnection", Optional.empty(), Type.BOOLEAN, Importance.HIGH, "Flag to enable or disable using secure connection to the PubNub API."),
    SHOULD_STORE("shouldStore", Optional.of(false), Type.BOOLEAN, Importance.MEDIUM, "Store in history. If not specified, then the history configuration on the key is used."),
    USE_POST("usePOST", Optional.of(false), Type.BOOLEAN, Importance.MEDIUM, "Use HTTP POST method to publish.");

    private String configKeyName;
    private Optional<Object> defaultValue;
    private Type configType;
    private Importance importance;
    private String description;

    ConnectorConfigurationEntry(String configKeyName, Optional<Object> defaultValue, Type configType, Importance importance, String description) {
        this.configKeyName = configKeyName;
        this.defaultValue = defaultValue;

        this.configType = configType;
        this.importance = importance;
        this.description = description;
    }

    public String getConfigKeyName() {
        return configKeyName;
    }

    public Optional<Object> getDefaultValue() {
        return defaultValue;
    }

    public Type getConfigType() {
        return configType;
    }

    public Importance getImportance() {
        return importance;
    }

    public String getDescription() {
        return description;
    }
}
