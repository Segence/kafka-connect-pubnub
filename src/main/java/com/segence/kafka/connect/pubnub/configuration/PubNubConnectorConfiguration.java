package com.segence.kafka.connect.pubnub.configuration;

import org.apache.kafka.common.config.AbstractConfig;
import org.apache.kafka.common.config.ConfigDef;
import org.jooq.lambda.Seq;

import java.util.Map;

public class PubNubConnectorConfiguration extends AbstractConfig {
    public static final ConfigDef CONFIG_DEFINITIONS =
            Seq.of(ConnectorConfigurationEntry.values()).foldLeft(new ConfigDef(), (configDef, configEntry) ->
                configDef.define(configEntry.getConfigKeyName(),
                                 configEntry.getConfigType(),
                                 configEntry.getImportance(),
                                 configEntry.getDescription()
                                )
            );

    public PubNubConnectorConfiguration(ConfigDef definition, Map<?, ?> originals, boolean doLog) {
        super(definition, originals, doLog);
    }
}
