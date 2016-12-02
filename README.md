Kafka Connect PubNub connector
==============================

- Test with coverage: `./gradlew test jacocoTestReport`
- Build JAR with dependencies: `./gradlew shadowJar`
- Static analysis using [SonarQube](http://www.sonarqube.org): `./gradlew sonarRunner`

Example config:

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

The `shouldStore` and `usePOST` entries are optional, and their default value is `false`.
