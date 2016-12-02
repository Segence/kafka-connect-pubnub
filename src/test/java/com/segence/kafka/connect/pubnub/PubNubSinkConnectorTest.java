package com.segence.kafka.connect.pubnub;

import org.junit.Test;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class PubNubSinkConnectorTest {

    private static final Map<String, String> TEST_CONFIGURATION = new HashMap<String, String>() {{
        put("publishKey", "ABCD");
        put("subscribeKey", "EDFG");
        put("channel", "channel-name");
        put("useSecureConnection", "true");
    }};

    private PubNubSinkConnector underTest = new PubNubSinkConnector();

    @Test
    public void taskConfigsShouldReturnConfigurationForAllTasks() {

        List<Map<String, String>> expectedResult = Arrays.asList(TEST_CONFIGURATION, TEST_CONFIGURATION);

        underTest.start(TEST_CONFIGURATION);
        List<Map<String, String>> result = underTest.taskConfigs(2);

        assertThat(result, is(expectedResult));
    }
}
