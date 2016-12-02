package com.segence.kafka.connect.pubnub;

import com.pubnub.api.PNConfiguration;
import com.pubnub.api.PubNub;
import com.pubnub.api.callbacks.PNCallback;
import com.pubnub.api.models.consumer.PNPublishResult;
import com.pubnub.api.models.consumer.PNStatus;
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

    private PubNub pubNubClient;
    private String channelName;
    private boolean shouldStore;
    private boolean usePOST;

    public PubNubSinkTask() { }

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

        PubNubConnectorConfiguration pubNubConnectorConfiguration = new PubNubConnectorConfiguration(CONFIG_DEFINITIONS, props, true);

        channelName = pubNubConnectorConfiguration.getString(CHANNEL.getConfigKeyName());

        PNConfiguration pnConfiguration = new PNConfiguration();
        pnConfiguration.setPublishKey(pubNubConnectorConfiguration.getString(PUBLISH_KEY.getConfigKeyName()));
        pnConfiguration.setSubscribeKey(pubNubConnectorConfiguration.getString(SUBSCRIBE_KEY.getConfigKeyName()));
        pnConfiguration.setSecure(pubNubConnectorConfiguration.getBoolean(USE_SECURE_CONNECTION.getConfigKeyName()));

        shouldStore = pubNubConnectorConfiguration.getBoolean(SHOULD_STORE.getConfigKeyName());
        usePOST = pubNubConnectorConfiguration.getBoolean(USE_POST.getConfigKeyName());

        pubNubClient = new PubNub(pnConfiguration);
    }

    @Override
    public void put(Collection<SinkRecord> records) {
        records.forEach(record -> {
            LOGGER.debug("Attempting to publish message[key=" + record.key() + ", value=" + record.value() + "]");
            pubNubClient.publish()
                  .message(record.value())
                  .channel(channelName)
                  .shouldStore(shouldStore)
                  .usePOST(usePOST)
                  .async(new PNCallback<PNPublishResult>() {
                      @Override
                      public void onResponse(PNPublishResult result, PNStatus status) {
                          if (status.isError()) {
                              LOGGER.error(
                                  "Error publishing message. Reason: " + status.getErrorData().getInformation(),
                                  status.getErrorData().getThrowable()
                              );
                              LOGGER.debug("Message details[key=" + record.key() + ", value=" + record.value() + "]");
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
