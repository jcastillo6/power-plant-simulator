FROM eclipse-temurin:21-jre-alpine
USER root
WORKDIR /app
COPY build/libs/*.jar app.jar
EXPOSE 8080

ENV SPRING_PROFILES_ACTIVE=dev

ENTRYPOINT java $JAVA_OPTS -Dspring.profiles.active=$SPRING_PROFILES_ACTIVE -jar /app/app.jar

