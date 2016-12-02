package com.segence.kafka.connect.pubnub;

import com.segence.kafka.connect.pubnub.configuration.PubNubConnectorConfiguration;
import org.apache.kafka.common.config.ConfigDef;
import org.apache.kafka.connect.connector.Task;
import org.apache.kafka.connect.sink.SinkConnector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static com.segence.kafka.connect.pubnub.configuration.ConnectorConfigurationEntry.CHANNEL;
import static com.segence.kafka.connect.pubnub.configuration.ConnectorConfigurationEntry.PUBLISH_KEY;
import static com.segence.kafka.connect.pubnub.configuration.ConnectorConfigurationEntry.USE_SECURE_CONNECTION;

public class PubNubSinkConnector extends SinkConnector {

    private static final Logger LOGGER = LoggerFactory.getLogger(PubNubSinkConnector.class);

    private String publishKey;
    private String channelName;
    private String useSecureConnection;

    @Override
    public String version() {
        return getClass().getPackage().getImplementationVersion();
    }

    @Override
    public void start(Map<String, String> props) {
        this.publishKey = props.get(PUBLISH_KEY.getConfigKeyName());
        this.channelName = props.get(CHANNEL.getConfigKeyName());
        this.useSecureConnection = props.get(USE_SECURE_CONNECTION.getConfigKeyName());
    }

    @Override
    public Class<? extends Task> taskClass() {
        return PubNubSinkTask.class;
    }

    @Override
    public List<Map<String, String>> taskConfigs(int maxTasks) {
        Map<String, String> config = new HashMap<>();
        config.put(PUBLISH_KEY.getInternalConfigKeyName(), publishKey);
        config.put(CHANNEL.getInternalConfigKeyName(), channelName);
        config.put(USE_SECURE_CONNECTION.getInternalConfigKeyName(), useSecureConnection);
        return IntStream.range(0, maxTasks).mapToObj(task -> config).collect(Collectors.toList());
    }

    @Override
    public void stop() {
        LOGGER.info("Stopping PubNub connector...");
    }

    @Override
    public ConfigDef config() {
        return PubNubConnectorConfiguration.CONFIG_DEFINITIONS;
    }
}
