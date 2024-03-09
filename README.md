# Spring Boot Starter Runtime Metadata

[![GA](https://img.shields.io/badge/Release-Alpha-orange)](https://img.shields.io/badge/Release-Alpha-orange) ![Github Action CI Workflow Status](https://github.com/cf-toolsuite/spring-boot-starter-runtime-metadata/actions/workflows/ci.yml/badge.svg) [![Known Vulnerabilities](https://snyk.io/test/github/cf-toolsuite/spring-boot-starter-runtime-metadatar/badge.svg?style=plastic)](https://snyk.io/test/github/cf-toolsuite/spring-boot-starter-runtime-metadata) [![Release](https://jitpack.io/v/cf-toolsuite/spring-boot-starter-runtime-metadata.svg)](https://jitpack.io/#cf-toolsuite/spring-boot-starter-runtime-metadata/master-SNAPSHOT) [![License](https://img.shields.io/badge/License-Apache%202.0-blue.svg)](https://opensource.org/licenses/Apache-2.0)

This starter aims to provide a few custom actuator endpoints:

* GET `/actuator/jars` will list the dependent `.jar` files embedded in a Spring Boot application
* GET `/actuator/pom` will emit the contents of the `pom.xml` file (if the application was built with Maven)
* GET `actuator/info` enhanced to provide
  * a CycloneDX SBOM in JSON format (only if the application packages an sbom.json)
  * just the list of dependencies in JSON format (again only if the application packages an sbom.json)


## Background

The thinking goes, most developers want to deploy apps to an "app-aware" platform.  How might a Spring Boot application register itself and its dependencies with the platform?  As well, provide information at runtime about its dependencies.

The first target is Cloud Foundry.  But there's nothing that stops us from doing the same on other target runtime platforms (e.g. Kubernetes).


## Prerequisites

* [SDKMan](https://sdkman.io/)
* JDK 17 or better
* (Optionally) access to a [Cloud Foundry](https://www.cloudfoundry.org/) foundation with read-only admin credentials


## Clone

```
gh repo clone cf-toolsuite/spring-boot-starter-runtime-metadata
```


## Build

```
sdk install java 17.0.10-librca
sdk use java 17.0.10-librca
./mvnw clean install
```


## How to use

Add the following dependency to your application's pom.xml file

```
<dependency>
  <groupId>io.pivotal.app.actuator</groupId>
  <artifactId>spring-boot-starter-runtime-metadata</artifactId>
  <version>0.1.0</version>
</dependency>
```

Then make sure to enable the additional contributions to the `management.info` endpoint (e.g., in application.yml)

```
management:
  info:
    dependencies:
      enabled: true
    sbom:
      enabled: true
```

And if you want to expose the `/actuator/info`, `/actuator/jars` and `/actuator/pom` endpoints, you'll need to add (e.g., in application.yml)

```
  endpoints:
    web:
      exposure:
        include: "info,jars,pom"
```
> where `endpoints` above is a sibling of (shares the same indentiation as) `info`

Build your application, then start it up.

Visit the above-mentioned custom [actuator endpoints](https://docs.spring.io/spring-boot/docs/current/reference/html/actuator.html).


## Endpoints

So what do you get when you've plumbed everything correctly?

```
GET /actuator/jars
```

```
❯ http :8080/actuator/jars
HTTP/1.1 200 OK
Content-Length: 5438
Content-Type: application/vnd.spring-boot.actuator.v3+json

[
    "BOOT-INF/lib/lombok-1.18.30.jar",
    "BOOT-INF/lib/jakarta.validation-api-3.0.2.jar",
    "BOOT-INF/lib/commons-lang3-3.14.0.jar",
    "BOOT-INF/lib/jackson-datatype-jsr310-2.16.1.jar",
    "BOOT-INF/lib/jackson-annotations-2.16.1.jar",
    "BOOT-INF/lib/jackson-core-2.16.1.jar",
    "BOOT-INF/lib/jackson-databind-2.16.1.jar",
    "BOOT-INF/lib/jackson-dataformat-csv-2.16.1.jar",
    "BOOT-INF/lib/java-uuid-generator-5.0.0.jar",
    "BOOT-INF/lib/slf4j-api-2.0.12.jar",
    "BOOT-INF/lib/java-cfenv-boot-3.1.5.jar",
    "BOOT-INF/lib/java-cfenv-3.1.3.jar",
    "BOOT-INF/lib/java-cfenv-jdbc-3.1.5.jar",
    "BOOT-INF/lib/spring-boot-3.2.3.jar",
    "BOOT-INF/lib/spring-context-6.1.4.jar",
    "BOOT-INF/lib/spring-aop-6.1.4.jar",
    "BOOT-INF/lib/spring-expression-6.1.4.jar",
    "BOOT-INF/lib/json-io-4.19.1.jar",
    "BOOT-INF/lib/jldap-2009-10-07.jar",
    "BOOT-INF/lib/spring-boot-autoconfigure-3.2.3.jar",
    "BOOT-INF/lib/logback-classic-1.4.14.jar",
    "BOOT-INF/lib/logback-core-1.4.14.jar",
    "BOOT-INF/lib/log4j-to-slf4j-2.21.1.jar",
    "BOOT-INF/lib/log4j-api-2.21.1.jar",
    "BOOT-INF/lib/jul-to-slf4j-2.0.12.jar",
    "BOOT-INF/lib/spring-boot-actuator-autoconfigure-3.2.3.jar",
    "BOOT-INF/lib/spring-boot-actuator-3.2.3.jar",
    "BOOT-INF/lib/micrometer-observation-1.12.3.jar",
    "BOOT-INF/lib/micrometer-commons-1.12.3.jar",
    "BOOT-INF/lib/micrometer-jakarta9-1.12.3.jar",
    "BOOT-INF/lib/jackson-module-parameter-names-2.16.1.jar",
    "BOOT-INF/lib/reactor-netty-http-1.1.16.jar",
    "BOOT-INF/lib/netty-codec-http-4.1.107.Final.jar",
    "BOOT-INF/lib/netty-common-4.1.107.Final.jar",
    "BOOT-INF/lib/netty-buffer-4.1.107.Final.jar",
    "BOOT-INF/lib/netty-transport-4.1.107.Final.jar",
    "BOOT-INF/lib/netty-codec-4.1.107.Final.jar",
    "BOOT-INF/lib/netty-codec-http2-4.1.107.Final.jar",
    "BOOT-INF/lib/netty-resolver-dns-4.1.107.Final.jar",
    "BOOT-INF/lib/netty-resolver-4.1.107.Final.jar",
    "BOOT-INF/lib/netty-codec-dns-4.1.107.Final.jar",
    "BOOT-INF/lib/netty-resolver-dns-native-macos-4.1.107.Final-osx-x86_64.jar",
    "BOOT-INF/lib/netty-resolver-dns-classes-macos-4.1.107.Final.jar",
    "BOOT-INF/lib/netty-transport-native-epoll-4.1.107.Final-linux-x86_64.jar",
    "BOOT-INF/lib/netty-transport-native-unix-common-4.1.107.Final.jar",
    "BOOT-INF/lib/netty-transport-classes-epoll-4.1.107.Final.jar",
    "BOOT-INF/lib/spring-web-6.1.4.jar",
    "BOOT-INF/lib/spring-beans-6.1.4.jar",
    "BOOT-INF/lib/spring-webflux-6.1.4.jar",
    "BOOT-INF/lib/spring-data-r2dbc-3.2.3.jar",
    "BOOT-INF/lib/spring-data-relational-3.2.3.jar",
    "BOOT-INF/lib/jsqlparser-4.6.jar",
    "BOOT-INF/lib/spring-data-commons-3.2.3.jar",
    ...
```

```
GET /actuator/pom
```

```
❯ http :8080/actuator/pom
HTTP/1.1 200 OK
Content-Length: 28052
Content-Type: application/vnd.spring-boot.actuator.v3+json;charset=UTF-8

<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0" xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd"
  xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance">
  <modelVersion>4.0.0</modelVersion>

  <groupId>io.pivotal</groupId>
  <artifactId>cf-butler</artifactId>
  <version>1.0-SNAPSHOT</version>

  <name>cf-butler</name>
  <description>My purpose in life is to cleanup stale apps and services on a Cloud Foundry foundation. I can be configured to report on and remove orphaned services and stopped app instances older than a configurable duration. I do many other useful things too.</description>

  <scm>
    <connection>scm:git:git://github.com/cf-toolsuite/cf-butler.git</connection>
    <developerConnection>scm:git:ssh://github.com/cf-toolsuite/cf-butler.git</developerConnection>
    <url>https://github.com/cf-toolsuite/cf-butler</url>
  </scm>
  ...
```

```
GET /actuator/info
```

Just a sample of what you can get from the additionally contributed `dependencies` section

```
❯ http :8080/actuator/info | jq .dependencies
[
  {
    "groupId": "org.projectlombok",
    "artifactId": "lombok",
    "version": "1.18.30"
  },
  {
    "groupId": "jakarta.validation",
    "artifactId": "jakarta.validation-api",
    "version": "3.0.2"
  },
  {
    "groupId": "org.apache.commons",
    "artifactId": "commons-lang3",
    "version": "3.14.0"
  },
  ...
```


## Roadmap

### Cloud Foundry and Buildpacks

What if the [Java Buildpack](https://github.com/cloudfoundry/java-buildpack?tab=readme-ov-file#configuration-and-extension) could be extended where it would detect if the app to be deployed is a [Spring Boot 3.x](https://docs.spring.io/spring-boot/docs/current/reference/html/) application?  If that is the case, add this dependency to the classpath if not already specified, when assembling the droplet.


## Credits

### Software Bill of Materials

* Adapted earlier work by Maciej Walkowiak, here: https://maciejwalkowiak.com/blog/maven-dependencies-spring-boot-actuator-info/.
* Keeping an eye on https://github.com/spring-projects/spring-boot/issues/22924.
