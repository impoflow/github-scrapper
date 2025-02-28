FROM maven:3.9.5-eclipse-temurin-17 as builder

WORKDIR /app

COPY . /app

RUN mvn clean package

CMD ["java", "-jar", "target/github-scrapper.jar"]
