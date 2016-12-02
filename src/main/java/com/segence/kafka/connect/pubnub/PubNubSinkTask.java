package com.segence.kafka.connect.pubnub;

import com.pubnub.api.PNConfiguration;
import com.pubnub.api.PubNub;
import com.pubnub.api.callbacks.PNCallback;
import com.pubnub.api.models.consumer.PNPublishResult;
import com.pubnub.api.models.consumer.PNStatus;
import com.segence.kafka.connect.pubnub.configuration.ConnectorConfigurations;
import org.apache.kafka.clients.consumer.OffsetAndMetadata;
import org.apache.kafka.common.TopicPartition;
import org.apache.kafka.connect.sink.SinkRecord;
import org.apache.kafka.connect.sink.SinkTask;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;
import java.util.Map;

import static com.segence.kafka.connect.pubnub.configuration.ConnectorConfiguration.CHANNEL;
import static com.segence.kafka.connect.pubnub.configuration.ConnectorConfiguration.PUBLISH_KEY;
import static com.segence.kafka.connect.pubnub.configuration.ConnectorConfiguration.USE_SECURE_CONNECTION;
import static com.segence.kafka.connect.pubnub.configuration.ConnectorConfigurations.CONFIG_DEFINITIONS;

class PubNubSinkTask extends SinkTask {

    private static final Logger LOGGER = LoggerFactory.getLogger(PubNubSinkTask.class);

    private PubNub pubNubClient;
    private String channelName;

    PubNubSinkTask() { }

    PubNubSinkTask(PubNub pubNubClient) {
        this.pubNubClient = pubNubClient;
    }

    @Override
    public String version() {
        return getClass().getPackage().getImplementationVersion();
    }

    @Override
    public void start(Map<String, String> props) {

        LOGGER.info("Starting PubNub Sink Task...");

        ConnectorConfigurations connectorConfigurations = new ConnectorConfigurations(CONFIG_DEFINITIONS, props, true);

        channelName = connectorConfigurations.getString(CHANNEL.getInternalConfigKeyName());

        PNConfiguration pnConfiguration = new PNConfiguration();
        pnConfiguration.setPublishKey(connectorConfigurations.getString(PUBLISH_KEY.getInternalConfigKeyName()));
        pnConfiguration.setSecure(connectorConfigurations.getBoolean(USE_SECURE_CONNECTION.getInternalConfigKeyName()));

        pubNubClient = new PubNub(pnConfiguration);
    }

    @Override
    public void put(Collection<SinkRecord> records) {
        records.forEach(record -> {
            LOGGER.debug("Attempting to publish message[" + record + "]");
            pubNubClient.publish()
                  .message(record.value())
                  .channel(channelName)
                  .async(new PNCallback<PNPublishResult>() {
                      @Override
                      public void onResponse(PNPublishResult result, PNStatus status) {
                          if (status.isError()) {
                              LOGGER.error("Error publishing message. Reason: " + status.getErrorData());
                              LOGGER.debug("Message details[" + record + "]");
                          } else {
                              LOGGER.debug("Successfully published message[" + record + "]");
                          }
                      }
                  });
        });
    }

    @Override
    public void flush(Map<TopicPartition, OffsetAndMetadata> offsets) {
        LOGGER.debug("Flushing PubNub Sink");
    }

    @Override
    public void stop() {
        LOGGER.info("Disconnecting from PubNub...");
        pubNubClient.disconnect();
        LOGGER.info("Cleaning up PubNub connection pool...");
        pubNubClient.destroy();
        LOGGER.info("Successfully stopped PubNub Sink Task.");
    }
}
