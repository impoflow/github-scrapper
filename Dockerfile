FROM maven:3.9.5-eclipse-temurin-17 as builder
WORKDIR /app

COPY . /app

RUN mvn clean package

FROM openjdk:17-jdk-slim

WORKDIR /app

COPY --from=builder /app/target/*.jar /app/app.jar

VOLUME ["/app/datalake"]

CMD ["java", "-jar", "/app/app.jar"]
