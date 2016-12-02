package com.segence.kafka.connect.pubnub.configuration;

import org.apache.kafka.common.config.ConfigDef.Importance;
import org.apache.kafka.common.config.ConfigDef.Type;

public enum ConnectorConfiguration {

    PUBLISH_KEY("publishKey", "PUBLISH_KEY", Type.STRING, Importance.HIGH, "The PubNub publish key used to publish messages."),
    CHANNEL("channel", "CHANNEL", Type.STRING, Importance.HIGH, "The PubNub channel to publish messages to."),
    USE_SECURE_CONNECTION("useSecureConnection", "USE_SECURE_CONNECTION", Type.BOOLEAN, Importance.HIGH, "Flag to enable or disable using secure connection to the PubNub API.");

    private String configKeyName;
    private String internalConfigKeyName;
    private Type configType;
    private Importance importance;
    private String description;

    ConnectorConfiguration(String configKeyName, String internalConfigKeyName, Type configType, Importance importance, String description) {
        this.configKeyName = configKeyName;
        this.internalConfigKeyName = internalConfigKeyName;
        this.configType = configType;
        this.importance = importance;
        this.description = description;
    }

    public String getConfigKeyName() {
        return configKeyName;
    }

    public String getInternalConfigKeyName() {
        return internalConfigKeyName;
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
