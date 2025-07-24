FROM openjdk:21-jdk-slim

WORKDIR /app

COPY /build/libs/*.jar pay.jar

ENTRYPOINT ["java", "-jar", "pay.jar"]