FROM eclipse-temurin:21-jre-alpine
WORKDIR /app
COPY ./target/*.jar application.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "application.jar"]