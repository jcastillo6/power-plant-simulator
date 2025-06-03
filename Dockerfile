FROM gradle:8.14.0-jdk21 AS build
WORKDIR /app

# Copy only the files needed for dependency resolution
COPY build.gradle settings.gradle gradle/ ./
RUN gradle dependencies --no-daemon

# Copy the source code
COPY . .

# Build the application
RUN gradle clean build -x test --no-daemon

FROM eclipse-temurin:21-jre-alpine
USER root
WORKDIR /app

COPY --from=build /app/build/libs/*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["sh", "-c", "java ${JAVA_OPTS} \
                       -Dspring.profiles.active=${SPRING_PROFILES_ACTIVE} \
                       -Dspring.data.mongodb.host=${MONGODB_HOST} \
                       -Dspring.data.mongodb.username=${MONGODB_USER} \
                       -Dspring.data.mongodb.password=${MONGODB_PASSWORD} \
                       -jar /app/app.jar"]
