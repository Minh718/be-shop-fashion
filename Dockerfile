# Stage 1: Build the Java app
# FROM maven:3.8.3-openjdk-17 as build
# WORKDIR /app
# COPY . .
# RUN mvn package

# Stage 2: Run the app
# FROM openjdk:11-jre-slim
# COPY --from=build /app/target/my-app.jar /my-app.jar
# CMD ["java", "-jar", "/my-app.jar"]

FROM openjdk:11-jre-slim
COPY shop1905-0.0.1-SNAPSHOT.jar /my-app.jar
CMD ["java", "-jar", "/my-app.jar"]
