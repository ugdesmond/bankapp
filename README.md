# mini bank  app

## Requirements

For building and running the application you need:

- [JDK 1.8](http://www.oracle.com/technetwork/java/javase/downloads/jdk8-downloads-2133151.html)
- [Gradle 6.1](https://gradle.org/)
-- [POSTGRES SQL](https://www.postgresql.org/)

## Running the application locally

There are several ways to run a Spring Boot application on your local machine. One way is to execute the `main` method in the `com.app.bankapp.BankApplication` class from your IDE.

Alternatively you can use the [Spring Boot Gradle plugin](https://docs.spring.io/spring-boot/docs/current/reference/html/build-tool-plugins-maven-plugin.html) like so:

```shell
gradle bootRun
```

## Steps for creating user accounts

-Authenticate  using:
 -email:admin@gmail.com
 -password:admin
 - [authentication url](http://localhost:8083/api/authentication)
 -method:POST
 - create User Account.
 
## Steps for making transfer
-deposit money in the created user account
-transfer from the account to another account.


## Copyright

Released under the Apache License 2.0. See the [LICENSE](https://github.com/codecentric/springboot-sample-app/blob/master/LICENSE) file.
