package com.segence.kafka.connect.pubnub;

import com.segence.kafka.connect.pubnub.configuration.PubNubConnectorConfiguration;
import org.apache.kafka.common.config.ConfigDef;
import org.apache.kafka.connect.connector.Task;
import org.hamcrest.MatcherAssert;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static com.segence.kafka.connect.pubnub.TestStubs.TEST_CONFIGURATION;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

public class PubNubSinkConnectorTest {

    private PubNubSinkConnector underTest = new PubNubSinkConnector();

    @Test
    public void taskConfigsShouldReturnConfigurationForAllTasks() {

        List<Map<String, String>> expectedResult = Arrays.asList(TEST_CONFIGURATION, TEST_CONFIGURATION);

        underTest.start(TEST_CONFIGURATION);
        List<Map<String, String>> result = underTest.taskConfigs(2);

        assertThat(result, is(expectedResult));
    }

    @Test
    public void versionShouldReturnTheCorrectVersion() {
        String result = underTest.version();
        MatcherAssert.assertThat(result, is(getClass().getPackage().getImplementationVersion()));
    }

    @Test
    public void taskClassShouldReturnTheCorrectTaskClass() {
        Class<? extends Task> result = underTest.taskClass();
        assertTrue("Expected PubNubSinkTask.class but found other type", result == PubNubSinkTask.class);
    }

    @Test
    public void stopShouldShutDownTheClient() {
        ConfigDef result = underTest.config();
        assertThat(result, is(PubNubConnectorConfiguration.CONFIG_DEFINITIONS));
    }
}
