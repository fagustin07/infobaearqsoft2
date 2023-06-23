FROM maven:3.8.3-openjdk-17

WORKDIR /app
COPY . .

RUN mvn clean package

ENTRYPOINT ["java", "-jar", "./target/weather_loader-0.0.1-SNAPSHOT.jar"]