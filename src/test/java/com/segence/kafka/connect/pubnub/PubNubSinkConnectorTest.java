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
        put("channel", "channel-name");
        put("useSecureConnection", "true");
    }};

    private static final Map<String, String> EXPECTED_TEST_CONFIGURATION = new HashMap<String, String>() {{
        put("PUBLISH_KEY", "ABCD");
        put("CHANNEL", "channel-name");
        put("USE_SECURE_CONNECTION", "true");
    }};

    private PubNubSinkConnector underTest = new PubNubSinkConnector();

    @Test
    public void taskConfigsShouldReturnConfigurationForAllTasks() {

        List<Map<String, String>> expectedResult = Arrays.asList(EXPECTED_TEST_CONFIGURATION, EXPECTED_TEST_CONFIGURATION);

        underTest.start(TEST_CONFIGURATION);
        List<Map<String, String>> result = underTest.taskConfigs(2);

        assertThat(result, is(expectedResult));
    }
}
