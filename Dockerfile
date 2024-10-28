FROM ubuntu:latest
LABEL authors="dmytrosydoruk"
FROM openjdk:21

WORKDIR /app

COPY target/yurjinia-1.5.jar app.jar

EXPOSE 5440

ENTRYPOINT ["java", "-jar", "app.jar"]

