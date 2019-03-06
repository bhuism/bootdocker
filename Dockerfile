FROM openjdk:8-jdk-alpine

COPY target/bootdocker.jar /app/

ENTRYPOINT ["java", "-jar", "app/bootdocker.jar"]
