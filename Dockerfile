# Base image
FROM eclipse-temurin:17-jdk-alpine

# Copy files
COPY  ./application/target/application-0.0-SNAPSHOT.jar /bank-application.jar

# Set profile to run using postgres
ENV SPRING_PROFILES_ACTIVE="postgres-docker"

# Expose port
EXPOSE 8080

# Entrypoint ignity
ENTRYPOINT ["java","-jar", "-Dspring.profiles.active=postgres","/bank-application.jar"]