FROM gradle:6.8.3-jdk11 as builder
COPY . /app
WORKDIR /app
RUN gradle build --info

FROM openjdk:11-jre-slim
WORKDIR /app
COPY --from=builder /app/build/libs/*.jar app.jar
ENTRYPOINT ["java", "-jar", "/app/app.jar"]