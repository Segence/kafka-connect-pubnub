package com.segence.kafka.connect.pubnub;

import com.pubnub.api.PNConfiguration;
import com.pubnub.api.PubNub;
import com.segence.kafka.connect.pubnub.configuration.PubNubConnectorConfiguration;
import org.apache.kafka.clients.consumer.OffsetAndMetadata;
import org.apache.kafka.common.TopicPartition;
import org.apache.kafka.connect.sink.SinkRecord;
import org.apache.kafka.connect.sink.SinkTask;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;
import java.util.Map;

import static com.segence.kafka.connect.pubnub.configuration.ConnectorConfigurationEntry.*;
import static com.segence.kafka.connect.pubnub.configuration.PubNubConnectorConfiguration.CONFIG_DEFINITIONS;

public class PubNubSinkTask extends SinkTask {

    private static final Logger LOGGER = LoggerFactory.getLogger(PubNubSinkTask.class);

    private PubNubClient pubNubClient;
    private PublishCallback publishCallback;

    private String channelName;
    private boolean shouldStore;
    private boolean usePOST;

    public PubNubSinkTask() { }

    PubNubSinkTask(PubNubClient pubNubClient, PublishCallback publishCallback) {
        this.pubNubClient = pubNubClient;
        this.publishCallback = publishCallback;
    }

    void setPublishProperties(String channelName, boolean shouldStore, boolean usePOST) {
        this.channelName = channelName;
        this.shouldStore = shouldStore;
        this.usePOST = usePOST;
    }

    @Override
    public String version() {
        return getClass().getPackage().getImplementationVersion();
    }

    @Override
    public void start(Map<String, String> props) {

        LOGGER.info("Starting PubNub Sink Task...");

        PubNubConnectorConfiguration pubNubConnectorConfiguration = new PubNubConnectorConfiguration(CONFIG_DEFINITIONS, props, true);

        PNConfiguration pnConfiguration = new PNConfiguration();
        pnConfiguration.setPublishKey(pubNubConnectorConfiguration.getString(PUBLISH_KEY.getConfigKeyName()));
        pnConfiguration.setSubscribeKey(pubNubConnectorConfiguration.getString(SUBSCRIBE_KEY.getConfigKeyName()));
        pnConfiguration.setSecure(pubNubConnectorConfiguration.getBoolean(USE_SECURE_CONNECTION.getConfigKeyName()));

        setPublishProperties(
            pubNubConnectorConfiguration.getString(CHANNEL.getConfigKeyName()),
            pubNubConnectorConfiguration.getBoolean(SHOULD_STORE.getConfigKeyName()),
            pubNubConnectorConfiguration.getBoolean(USE_POST.getConfigKeyName())
        );

        pubNubClient = new PubNubClient(new PubNub(pnConfiguration));
        publishCallback = new PublishCallback();
    }

    @Override
    public void put(Collection<SinkRecord> records) {
        records.forEach(record -> {
            LOGGER.debug("Attempting to publish message[key=" + record.key() + ", value=" + record.value() + "]");
            pubNubClient.publishMessage(record.value(), channelName, shouldStore, usePOST, publishCallback);
        });
    }

    @Override
    public void flush(Map<TopicPartition, OffsetAndMetadata> offsets) {
        LOGGER.debug("Flushing PubNub Sink");
    }

    @Override
    public void stop() {
        pubNubClient.shutDown();
    }
}
