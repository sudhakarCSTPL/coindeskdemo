 

# coindeskdemo

 
[![License](http://img.shields.io/:license-apache-blue.svg)](http://www.apache.org/licenses/LICENSE-2.0.html)

Minimal [Spring Boot](http://projects.spring.io/spring-boot/) sample app.

## Requirements

For building and running the application you need:

- [Java 17](https://www.openlogic.com/openjdk-downloads)
- [Maven 3](https://maven.apache.org)

## Running the application locally

There are several ways to run a Spring Boot application on your local machine. One way is to execute the `main` method in the `com.example.democoindeskapi.DemocoindeskapiApplication.java
 ` class from your IDE.

Alternatively you can use the [Spring Boot Maven plugin](https://docs.spring.io/spring-boot/docs/current/reference/html/build-tool-plugins-maven-plugin.html) like so:

git clone https://github.com/sudhakarCSTPL/coindeskdemo.git 

```shell
mvn spring-boot:run
```

 mvn install : will package the application 

 travers to folder target java -jar democoindeskapi-0.0.1-SNAPSHOT.jar --spring.profiles.active=dev to run locally 

 
  

## Copyright

Released under the Apache License 2.0. See the [LICENSE](https://github.com/codecentric/springboot-sample-app/blob/master/LICENSE) file.
