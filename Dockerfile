FROM gradle:8.9-jdk21 AS build

WORKDIR /workspace

COPY src src
COPY build.gradle ./build.gradle
COPY settings.gradle ./settings.gradle

RUN gradle clean build

FROM openjdk:21-jdk-slim

WORKDIR /app

RUN adduser --system appuser && addgroup --system appuser && adduser appuser appuser
USER appuser

COPY --from=build /workspace/build/libs/file-service-0.0.1-SNAPSHOT.jar app.jar

ENTRYPOINT ["java","-jar","app.jar"]