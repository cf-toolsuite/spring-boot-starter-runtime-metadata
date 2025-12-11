# Spring Boot Starter Runtime Metadata

[![GA](https://img.shields.io/badge/Release-GA-blue)](https://img.shields.io/badge/Release-GA-blue) ![Github Action CI Workflow Status](https://github.com/cf-toolsuite/spring-boot-starter-runtime-metadata/actions/workflows/ci.yml/badge.svg) [![Known Vulnerabilities](https://snyk.io/test/github/cf-toolsuite/spring-boot-starter-runtime-metadatar/badge.svg?style=plastic)](https://snyk.io/test/github/cf-toolsuite/spring-boot-starter-runtime-metadata) [![Release](https://jitpack.io/v/cf-toolsuite/spring-boot-starter-runtime-metadata.svg)](https://jitpack.io/#cf-toolsuite/spring-boot-starter-runtime-metadata/master-SNAPSHOT) [![License](https://img.shields.io/badge/License-Apache%202.0-blue.svg)](https://opensource.org/licenses/Apache-2.0)

![spring-boot-starter-runtime-metadata diagram](docs/spring-boot-starter-runtime-metadata.png)

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
* JDK 25 or better (for version 1.0.0+), JDK 21 or better (for versions prior to 0.4.0)
* (Optionally) access to a [Cloud Foundry](https://www.cloudfoundry.org/) foundation with read-only admin credentials
* Your application source's Maven `pom.xml` or Gradle `build.gradle` file should declare a dependency on Spring Boot 3.2 or better (4.0.x recommended for version 1.0.0)

## Clone

```bash
gh repo clone cf-toolsuite/spring-boot-starter-runtime-metadata
```

## Build

```bash
sdk install Java 25.0.1-librca
sdk use Java 25.0.1-librca
./mvnw clean install
```

> Note: As of `spring-boot-starter-runtime-metadata` `1.0.0`, we have a runtime dependency on Java 25 or better. Version `0.4.0` through `0.7.0` require Java 21 or better. Prior versions have a runtime dependency on Java 17 or better.

## How to use

### Compatibility Matrix

| Spring Boot | Runtime Metadata |
|-------------|------------------|
| 3.2.x | 0.3.0 |
| 3.3.x | 0.4.0 |
| 3.4.x | 0.6.0 |
| 3.5.x | 0.7.0 |
| 4.0.x | 1.0.0 |

### with Spring Boot 3.2.x

Add the following `dependency` to your application's `pom.xml` file

```pom
<dependency>
  <groupId>org.cftoolsuite.actuator</groupId>
  <artifactId>spring-boot-starter-runtime-metadata</artifactId>
  <version>0.3.0</version>
</dependency>
```

If you want to embed and expose a bill of materials from your artifact, then you'll also want to add this `plugin` to your application's `pom.xml` file too

```pom
<plugin>
  <groupId>org.cyclonedx</groupId>
  <artifactId>cyclonedx-maven-plugin</artifactId>
  <version>2.8.0</version>
  <executions>
    <execution>
      <phase>validate</phase>
      <goals>
        <goal>makeAggregateBom</goal>
      </goals>
    </execution>
  </executions>
  <configuration>
    <outputFormat>json</outputFormat>
    <outputName>classes/bom</outputName>
  </configuration>
</plugin>
```

#### Gradle

Add the following `dependency` to your application's `build.gradle` file

```gradle
dependencies {
    compile group: 'org.cftoolsuite.actuator', name: 'spring-boot-starter-runtime-metadata', version: '0.3.0'
}
```

If you want to embed and expose a bill of materials from your artifact, then you'll also want to add this plugin to your application's `build.gradle` file too

```gradle
plugins {
  id 'org.cyclonedx.bom' version '1.8.2'
}

tasks.named("cyclonedxBom") {
  destination = file("${buildDir}/classes")
}
```

### with Spring Boot 3.3.x

> Note: As of `spring-boot-starter-runtime-metadata` `0.4.0`, you will also be able to obtain a software-bill-of-materials from the `/actuator/sbom` endpoint, which is now [built-in](https://github.com/spring-projects/spring-boot/wiki/Spring-Boot-3.3-Release-Notes#sbom-actuator-endpoint) to Spring Boot `3.3`.

### with Spring Boot 3.5.x

#### Maven

Add the following `dependency` to your application's `pom.xml` file

```pom
<dependency>
  <groupId>org.cftoolsuite.actuator</groupId>
  <artifactId>spring-boot-starter-runtime-metadata</artifactId>
  <version>0.7.0</version>
</dependency>
```

If you want to embed and expose a bill of materials from your artifact, then you'll also want to add this `plugin` to your application's `pom.xml` file too

```pom
<plugin>
  <groupId>org.cyclonedx</groupId>
  <artifactId>cyclonedx-maven-plugin</artifactId>
</plugin>
```

#### Gradle

Add the following `dependency` to your application's `build.gradle` file

```gradle
dependencies {
    compile group: 'org.cftoolsuite.actuator', name: 'spring-boot-starter-runtime-metadata', version: '0.6.0'
}
```

If you want to embed and expose a bill of materials from your artifact, then you'll also want to add this `plugin` to your application's `build.gradle` file too

```gradle
plugins {
  id 'org.cyclonedx.bom' version '1.8.2'
}

tasks.named("cyclonedxBom") {
  destination = file("${buildDir}/META-INF/sbom")
}
```

### with Spring Boot 4.0.x

> **Important**: Version 1.0.0 introduces a significant architectural change. The `cyclonedx-core-java` library has been removed in favor of direct JSON parsing with Jackson 3. This change was necessary because `cyclonedx-core-java` has not yet migrated to Jackson 3, and Spring Boot 4.0 defaults to Jackson 3 with the new `tools.jackson` package namespace. This ensures a clean dependency tree without Jackson 2 artifacts (except for `jackson-annotations`, which intentionally remains under `com.fasterxml.jackson.core` per the Jackson 3 specification).

#### Maven

Add the following `dependency` to your application's `pom.xml` file

```pom
<dependency>
  <groupId>org.cftoolsuite.actuator</groupId>
  <artifactId>spring-boot-starter-runtime-metadata</artifactId>
  <version>1.0.0</version>
</dependency>
```

If you want to embed and expose a bill of materials from your artifact, then you'll also want to add this `plugin` to your application's `pom.xml` file too

```pom
<plugin>
  <groupId>org.cyclonedx</groupId>
  <artifactId>cyclonedx-maven-plugin</artifactId>
</plugin>
```

#### Gradle

Add the following `dependency` to your application's `build.gradle` file

```gradle
dependencies {
    implementation 'org.cftoolsuite.actuator:spring-boot-starter-runtime-metadata:1.0.0'
}
```

If you want to embed and expose a bill of materials from your artifact, then you'll also want to add this `plugin` to your application's `build.gradle` file too

```gradle
plugins {
  id 'org.cyclonedx.bom' version '2.0.0'
}

tasks.named("cyclonedxBom") {
  destination = file("${buildDir}/META-INF/sbom")
}
```

### Cloud Native Buildpacks

If you use the [pack](https://buildpacks.io/docs/for-app-developers/how-to/build-inputs/) CLI to assemble a container image, then you're probably saying to yourself: "I don't need to add a plugin to my build configuration because `pack` will write bill of materials files in various formats to a specific layer that I can later download a copy from".

So, you build your app from source with:

```bash
pack build {owner/your-app-name} --path {/path/to/your/app/source} --builder paketobuildpacks/builder-jammy-full
```
> Replace `{owner/your-app-name}` and `{/path/to/your/app/source}` with the owner/name of your application and the path to source (i.e., where your build file is) respectively

Verify your image includes bill of materials files in various formats by following these [instructions](https://buildpacks.io/docs/for-app-developers/how-to/build-outputs/download-sbom/).

Among several sub-directories underneath the `layers/sbom/launch` directory, you find:

```text
+- paketo-buildpacks_executable-jar
  +- sbom.cdx.json
  +- sbom.syft.json
```

Unfortunately, these files are not available and accessible in the container image at runtime.  But what you can do is make a copy of the `sbom.cdx.json` file.

Set a variable name.

For applications with dependencies on Spring Boot 3.2, set

```text
SBOM_FILENAME=sbom.json
```

For applications with dependencies on Spring Boot 3.3 or better, set

```text
SBOM_FILENAME=META-INF/sbom/application.cdx.json
```

```bash
cp -f layers/sbom/launch/paketo-buildpacks_executable-jar/sbom.cdx.json src/main/resources/$SBOM_FILENAME
```

Then rebuild the container image.  (Remember to repeat this process for any change you make to source).

### Spring Boot application.yml

Then make sure to enable the additional contributions to the `management.info` endpoint

```yaml
management:
  info:
    dependencies:
      enabled: true
    sbom:
      enabled: true
```

And if you want to expose the `/actuator/info`, `/actuator/jars` and `/actuator/pom` endpoints, you'll need to add (e.g., in application.yml)

```yaml
  endpoints:
    web:
      exposure:
        include: "info,jars,pom"
```
> where `endpoints` above is a sibling of (shares the same indentation as) `info`

Also, note if you want to expose the `/actuator/sbom` endpoint, available since Spring Boot 3.3, you'll need to update the above to be

```yaml
endpoints:
    web:
      exposure:
        include: "health,info,jars,pom,sbom"
```

Build your application, then start it up.

Visit the above-mentioned custom [actuator endpoints](https://docs.spring.io/spring-boot/docs/current/reference/html/actuator.html).

## Endpoints

So what do you get when you've plumbed everything correctly?

```python
GET /actuator/jars
```

```bash
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
    ...
```

```python
GET /actuator/pom
```

```bash
❯ http :8080/actuator/pom
HTTP/1.1 200 OK
Content-Length: 28052
Content-Type: application/vnd.spring-boot.actuator.v3+json;charset=UTF-8

<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0" xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd"
  xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance">
  <modelVersion>4.0.0</modelVersion>

  <groupId>org.cftoolsuite.cfapp</groupId>
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

```python
GET /actuator/info
```

Just a sample of what you can get from the additionally contributed `dependencies` section

```bash
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

And how you can download a software bill of materials

```bash
❯ http :8080/actuator/info | jq .sbom > sbom.json
```

## Roadmap

### Cloud Foundry and Buildpacks

What if the [Java Buildpack](https://github.com/cloudfoundry/java-buildpack?tab=readme-ov-file#configuration-and-extension) could be extended where it would detect if the app to be deployed is a [Spring Boot 3.x](https://docs.spring.io/spring-boot/docs/current/reference/html/) application?  If that is the case, add this dependency to the classpath if not already specified, when assembling the droplet.

## Credits

### Software Bill of Materials

* Adapted earlier work by Maciej Walkowiak, here: https://maciejwalkowiak.com/blog/maven-dependencies-spring-boot-actuator-info/.
