FROM openjdk:11-jre-slim-buster

WORKDIR /app
COPY . .
RUN ./gradlew build -x test

ENTRYPOINT ["java","-jar","./build/libs/weather.metric-0.0.1-SNAPSHOT.jar"]