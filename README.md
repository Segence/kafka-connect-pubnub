Kafka Connect PubNub connector
==============================

A Kafka Connect *sink* connector to publish messages to the [PubNub](https://www.pubnub.com/) platform.

Building
--------

- Test with coverage: `./gradlew test jacocoTestReport`
- Build JAR with dependencies: `./gradlew shadowJar`
- Static analysis using [SonarQube](http://www.sonarqube.org): `./gradlew sonarRunner`

*Note*: built for Java 8 so requires JDK 8 to compile and JRE 8 to run.

Configuration
-------------

Example config for distributed mode:

```
{
  "name": "pubnub-sink",
  "config": {
    "connector.class": "com.segence.kafka.connect.pubnub.PubNubSinkConnector",
    "tasks.max": 1,
    "topics": "test",
    "key.ignore": true,
    "publishKey": "",
    "subscribeKey": "",
    "channel": "",
    "useSecureConnection": false,
    "shouldStore": false,
    "usePOST": false
  }
}
```

Configuration options:

| **Name**              | **Description**                                                                        | **Default value**  |
|:----------------------|:---------------------------------------------------------------------------------------|--------------------|
| *publishKey*          | The PubNub publish key used to publish messages.                                       | *(none)*
| *subscribeKey*        | The PubNub subscribe key used to subscribe to a channel.                               | *(none)*
| *channel*             | The PubNub channel to publish messages to.                                             | *(none)*
| *useSecureConnection* | Flag to enable or disable using secure connection to the PubNub API.                   | *(none)*
| *shouldStore*         | Flag to store in history. If false, then the history configuration on the key is used. | false
| *usePOST*             | Flag to use HTTP POST method to publish.                                               | false

