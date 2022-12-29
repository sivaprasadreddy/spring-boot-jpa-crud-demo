# spring-boot-jpa-crud-demo

## How to run using Gradle?
```shell
$ ./gradlew build
$ docker-compose up -d
$ ./gradlew bootRun
# to create native binary
$ ./gradlew nativeCompile
$ ./build/libs/spring-boot-jpa-crud-demo
# to create docker image with native binary
$ ./gradlew bootBuildImage --imageName=sivaprasadreddy/spring-boot-jpa-crud-demo
$ docker-compose -f docker-compose.yml -f docker-compose-app.yml up -d
```

## How to run using Maven?
```shell
$ ./mvnw verify
$ docker-compose up -d
$ ./mvnw spring-boot:run 
# to create native binary
$ ./mvnw -Pnative native:compile
$ ./target/spring-boot-jpa-crud-demo
# to create docker image with native binary
$ ./mvnw -Pnative spring-boot:build-image -Dspring-boot.build-image.imageName=sivaprasadreddy/spring-boot-jpa-crud-demo
$ docker-compose -f docker-compose.yml -f docker-compose-app.yml up -d
```
