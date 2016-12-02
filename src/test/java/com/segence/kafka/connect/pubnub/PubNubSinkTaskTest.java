package com.segence.kafka.connect.pubnub;

import org.apache.kafka.clients.consumer.OffsetAndMetadata;
import org.apache.kafka.common.TopicPartition;
import org.apache.kafka.connect.sink.SinkRecord;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Arrays;
import java.util.Map;

import static com.segence.kafka.connect.pubnub.TestStubs.CHANNEL_NAME;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class PubNubSinkTaskTest {

    private static final String VALUE = "ABC";
    private static final String ANOTHER_VALUE = "DEF";

    @Mock
    private SinkRecord sinkRecordMock;

    @Mock
    private SinkRecord anotherSinkRecordMock;

    @Mock
    private PubNubClient pubNubClientMock;

    @Mock
    private PublishCallback publishCallbackMock;

    @Mock
    private Map<TopicPartition, OffsetAndMetadata> offsetsMock;

    private PubNubSinkTask underTest;

    @Before
    public void setUp() {
        underTest = new PubNubSinkTask(pubNubClientMock, publishCallbackMock);

    }

    @Test
    public void putShouldInvokePublish() {
        when(sinkRecordMock.value()).thenReturn(VALUE);
        when(anotherSinkRecordMock.value()).thenReturn(ANOTHER_VALUE);

        underTest.setPublishProperties(CHANNEL_NAME, false, false);
        underTest.put(Arrays.asList(sinkRecordMock, anotherSinkRecordMock));

        verify(pubNubClientMock).publishMessage(VALUE, CHANNEL_NAME, false, false, publishCallbackMock);
        verify(pubNubClientMock).publishMessage(ANOTHER_VALUE, CHANNEL_NAME, false, false, publishCallbackMock);
    }

    @Test
    public void versionShouldReturnTheCorrectVersion() {
        String result = underTest.version();
        assertThat(result, is(getClass().getPackage().getImplementationVersion()));
    }

    @Test
    public void flushShouldNotCallTheClient() {
        underTest.flush(offsetsMock);
        verifyZeroInteractions(pubNubClientMock);
    }

    @Test
    public void stopShouldShutDownTheClient() {
        underTest.stop();
        verify(pubNubClientMock).shutDown();
    }
}
