# chaos-engineering-demo
Provides REST API to demonstrate fault tolerance and recovery capabilities.

## Build
    mvn clean package

## Run
    java -jar ./target/chaos-engineering-demo-0.0.1-SNAPSHOT.jar
By default, the application will start with an internal database.  This default configuration can be overridden as described below.
    
##  Configuration
Run-time parameters can be supplied to the application through a Spring profile configuration file or directly from the command-line.

### Configuration file example
Sample configuration file named `application-sample.yaml` containing:
```
database:
  url: jdbc:oracle:thin:@[server]:[port]:[sid]
  username: sample-username
  password: sample-password
```
Activate this profile from the command line with:

    java -jar ./target/chaos-engineering-demo-0.0.1-SNAPSHOT.jar --spring.profiles.active=sample

[More details on Spring profiles...](https://docs.spring.io/spring-boot/docs/current/reference/html/spring-boot-features.html#boot-features-external-config-profile-specific-properties)

#### Built-in Spring profiles

 - `chaos-monkey` - provides resiliency tools to randomly simulate latency and failures in dependent systems.  [More information on Chaos Monkey...](https://github.com/Netflix/chaosmonkey)

### Command-line Configuration Override
Configuration parameters can be set (or overridden) from the command-line as follows:

    java -jar ./target/chaos-engineering-demo-0.0.1-SNAPSHOT.jar --database.password=password-override

[More info on overriding configuration values from the command-line...]([https://docs.spring.io/spring-boot/docs/current/reference/html/spring-boot-features.html#boot-features-external-config-command-line-args](https://docs.spring.io/spring-boot/docs/current/reference/html/spring-boot-features.html#boot-features-external-config-command-line-args))

## Integration Testing
### Swagger
The API can be explored interactively through the included integration with Swagger - accessed via following URL:

    http://[server|localhost]:[port|8080]/swagger-ui.html
### Postman
TBD

## Release

Mostly follow [GitFlow](https://www.atlassian.com/git/tutorials/comparing-workflows/gitflow-workflow "GitFlow") as implemented by [Git-Flow Maven Plugin](https://github.com/aleksandr-m/gitflow-maven-plugin#git-flow-maven-plugin)

### Create a release
- `mvn gitflow:release-start`
- verify the release
- `mvn gitflow:release-finish`

### Patch a TEST release
- Create a feature branch off of the release branch for the fix.
- Apply the fix to the feature branch.
- Merge back to the release branch and then merge the release branch to develop.

### Patch a PRODUCTION Release
- prune to remove any old remote hotfix references (`git fetch -p`)
- switch to master and pull to update
- `mvn gitflow:hotfix-start`
- Apply the fix.
- `mvn gitflow:hotfix-finish`