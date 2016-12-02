package com.segence.kafka.connect.pubnub;

import com.pubnub.api.PubNub;
import com.pubnub.api.callbacks.PNCallback;
import com.pubnub.api.models.consumer.PNPublishResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class PubNubClient {

    private static final Logger LOGGER = LoggerFactory.getLogger(PubNubClient.class);

    private PubNub pubNub;

    PubNubClient(PubNub pubNub) {
        this.pubNub = pubNub;
    }

    void publishMessage(Object value, String channel, boolean shouldStore, boolean usePOST, PNCallback<PNPublishResult> callback) {
        this.pubNub.publish().message(value).channel(channel).shouldStore(shouldStore).usePOST(usePOST).async(callback);
    }

    void shutDown() {
        LOGGER.info("Disconnecting from PubNub...");
        pubNub.disconnect();
        LOGGER.info("Cleaning up PubNub connection pool...");
        pubNub.destroy();
        LOGGER.info("Successfully stopped PubNub Sink Task.");
    }
}
