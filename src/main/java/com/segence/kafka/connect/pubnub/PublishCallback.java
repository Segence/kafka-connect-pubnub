package com.segence.kafka.connect.pubnub;

import com.pubnub.api.callbacks.PNCallback;
import com.pubnub.api.models.consumer.PNPublishResult;
import com.pubnub.api.models.consumer.PNStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class PublishCallback extends PNCallback<PNPublishResult> {

    private static final Logger LOGGER = LoggerFactory.getLogger(PublishCallback.class);

    @Override
    public void onResponse(PNPublishResult result, PNStatus status) {
        if (status.isError()) {
            LOGGER.error(
                "Error publishing message. Reason: " + status.getErrorData().getInformation(),
                status.getErrorData().getThrowable()
            );
        } else {
            LOGGER.debug("Successfully published message");
        }
    }
}
