Gradle Java bootstrap
=====================

Using Gradle as the build tool and JUnit as the test framework with Hamcrest and Mockito.

- Run: `./gradlew run`
- Test with coverage: `./gradlew test cobertura`
- Build JAR with dependencies: `./gradlew shadowJar`
- Create distribution (including start scripts): `./gradlew distZip`
- Build RPM: `./gradlew clean installDist buildRpm`
- Static analysis using [SonarQube](http://www.sonarqube.org): `./gradlew sonarRunner`
