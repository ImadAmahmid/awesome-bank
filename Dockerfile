# Base image
FROM eclipse-temurin:17-jdk-alpine

# Copy files
COPY  ./application/target/application-0.0-SNAPSHOT.jar /bank-application.jar

# Expose port
EXPOSE 8080

# Entrypoint ignity
ENTRYPOINT ["java","-jar", "-Dspring.profiles.active=h2,dev","/bank-application.jar"]