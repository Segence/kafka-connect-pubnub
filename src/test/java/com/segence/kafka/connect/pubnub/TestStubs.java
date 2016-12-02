package com.segence.kafka.connect.pubnub;

import java.util.HashMap;
import java.util.Map;

public class TestStubs {

    public static final String CHANNEL_NAME = "channel-name";

    static final Map<String, String> TEST_CONFIGURATION = new HashMap<String, String>() {{
        put("publishKey", "ABCD");
        put("subscribeKey", "EDFG");
        put("channel", CHANNEL_NAME);
        put("useSecureConnection", "true");
    }};
}
