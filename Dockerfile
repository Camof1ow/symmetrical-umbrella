# Build stage
FROM gradle:jdk21 AS build
WORKDIR /home/gradle/src
COPY build.gradle settings.gradle ./
COPY src ./src
# Cache Gradle packages
RUN gradle build --no-daemon --parallel > /dev/null 2>&1 || true
# Build the application
RUN gradle build -x test --no-daemon

# Run stage
FROM eclipse-temurin:21-jre-jammy
WORKDIR /app
COPY --from=build /home/gradle/src/build/libs/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]