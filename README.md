# Guest Book Service

<p align="center">
  <a href="#">
    <img src="./img/img.png" alt="playlist logo" width="73" height="73">
  </a>
</p>

<h3 align="center">Invoicify (Back-End Spring Boot REST API)</h3>

<p align="center">
  Invoicify is a service where the user can go and create a Company and add/remove invoice to Company.
  <br>

  <br>
</p>

[![Build, Test, Release, Deploy](https://github.com/gajjuCoderBoi/java-pundits-invoicify/actions/workflows/cicd.yml/badge.svg)](https://github.com/gajjuCoderBoi/java-pundits-invoicify/actions/workflows/cicd.yml)  [![codecov](https://codecov.io/gh/gajjuCoderBoi/java-pundits-invoicify/branch/develop/graph/badge.svg?token=N8P18UJIME)](https://codecov.io/gh/gajjuCoderBoi/java-pundits-invoicify)

## Table of contents

- [Technology Used](#technology-used)
- [Getting Started](#getting-started)
- [Auto-Deploy Instructions](#auto-deploy-instructions)
- [Releases](#releases)
- [API](#api)
- [What's Included](#whats-included)
- [Creators](#developers)

## Technology Used

- Gradle as Build Tool
- Spring boot is used for the backend Services.
- H2 database for Tests
- Postgres database to store the data.
- Junit and Mockito for Tests.
- Deployed on Heroku

## Getting Started

Software Requirement:

1. Gradle ([install](https://gradle.org/install/))

2. Java (minimum java 11) ([install](https://www.oracle.com/java/technologies/javase-downloads.html))

3. Docker ([install](https://docs.docker.com/get-docker/))

4. Heroku CLI Deployment. - Optional for deployment process. ([install](https://devcenter.heroku.com/articles/heroku-cli))
   
Build from Source:

1. Clone the repository.
     ```
     https://github.com/gajjuCoderBoi/java-pundits-invoicify
     ```

2. Build: 
   
   Server (Jar file)

      ```shell
       $ cd java-pundits-invoicify
       $ ./gradlew clean build
       # It will automatically download all dependencies and generate **invoicify.jar** in the **target** subfolder in project directory.
      ```
   or

   Docker image.
   
   ```shell
     $ cd java-pundits-invoicify
     $ docker build -t <your email>/java-pundits-invoicify .
   ```

3. Launch Server: Jar

   ```shell
      $ cd java-pundits-invoicify/target
      $ java -jar invoicify.jar
   ```

   Note: By default Invoicify Server uses embedded H2 database. If you want to use PostgreSQL database engine you need to replace following lines in configuration file: **<project-dir>/conf.yml** in our case **java-pundits-invoicify/conf.yml** .
   After changes please repeat Step 2.
   
   
```yaml
      spring:
        datasource:
         driver-class-name: org.h2.Driver
         url: jdbc:h2:mem:invoicifydb
         username: sa
         password: sa
   ```

   Configuration parameters for PostgreSQL (replace [SERVER-ADDRESS], [PORT] (if available), [DATABASE], [USER], [PASSWORD] with appropriate values):

```yaml
      spring:
         datasource:
            driver-class-name: org.postgresql.Driver
            url: jdbc:postgresql://[SERVER-ADDRESS]:[PORT]/[DATABASE]
            username: [USERNAME]
            password: [PASSWORD]
```

4. Run: 
      
      Jar Server

   ```shell
   $ java -jar target/invoicify.jar
   ```
   Note: By default Invoicify Server runs on port 8080. If you want to change the port follow either of these steps:
      
   * Using config.yml (need to repeat step 2)

     ```yaml
        # Add Server Port property
        server.port=your-port                        
     ```
   * Java Command
      
      ```shell
         $ java -jar target/invoicify.jar --server.port=9090
         ## or by using the equivalent syntax:
         $ java jar -Dserver.port=9090 target/invoicify.jar
      ```

      Docker Server:      

   ```shell
      $ docker run -d -p 8080:8080 --name invoicify <your email>/invoicify
   ```
   Note: To update database please follow Step 3 database instructions. 

5. Enjoy



## Developers

**José Antonio Vega Ruiz**

- <https://github.com/joseantoniovegaruiz2>

**Koustav Bhar**

- <https://github.com/kbhar01>

**Raghavendra Singh**

- <https://github.com/raghav-codes>

**Mohammad Javed**

- <https://github.com/gajjuCoderBoi>