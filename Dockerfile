FROM gradle:6.8.3-jdk11 as builder
COPY . /app
WORKDIR /app
RUN gradle build -q

FROM openjdk:11-jre-slim
WORKDIR /app
COPY --from=builder /app/target/*.jar app.jar
CMD [ "sh", "-c", "java -Xmx300m -Xss512k -Dserver.port=$PORT -jar /app/app.jar" ]