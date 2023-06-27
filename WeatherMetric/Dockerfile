FROM gradle:jdk17-alpine AS build
COPY . /home/gradle/src
WORKDIR /home/gradle/src
RUN ./gradlew build -x test

FROM openjdk:17-jdk-slim
EXPOSE 9000
RUN mkdir /app
COPY --from=build /home/gradle/src/build/libs/weather.metric-0.0.1-SNAPSHOT.jar /app/weather.metric-0.0.1-SNAPSHOT.jar
ENTRYPOINT ["java","-jar","/app/weather.metric-0.0.1-SNAPSHOT.jar"]
