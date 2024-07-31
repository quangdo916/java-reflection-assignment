#Stage 1: build a .jar file
FROM maven:3.9.8-amazoncorretto-21-al2023 AS builder

WORKDIR /app

COPY pom.xml ./
COPY src ./src
COPY bom ./bom
COPY data.csv ./data.csv

RUN mvn clean package

#Stage 2: Copy only necessary files
FROM eclipse-temurin:21-jdk-noble

WORKDIR /app

COPY --from=builder /app/target/reflection-assignment-1.0-SNAPSHOT.jar ./app.jar
COPY --from=builder /app/target/libs ./libs
COPY --from=builder /app/data.csv ./data.csv

CMD ["java", "-jar", "app.jar"]
