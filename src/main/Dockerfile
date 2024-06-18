# Use OpenJDK 18
FROM openjdk:18-jdk-slim

# Set the working directory
WORKDIR /app

# Copy the jar file into the container
COPY /target/SalesInvoice-0.0.1-SNAPSHOT.jar /app/SalesInvoice-0.0.1-SNAPSHOT.jar

# Expose the application port
EXPOSE 8080

# Define the command to run the application
ENTRYPOINT ["java", "-jar", "SalesInvoice-0.0.1-SNAPSHOT.jar"]
