# Use OpenJDK 18
FROM openjdk:18-jdk-slim

# Set the working directory
WORKDIR /app

# Copy the jar file into the container
COPY /target/SalesInvoice-0.0.1-SNAPSHOT.jar /app/SalesInvoice-0.0.1-SNAPSHOT.jar

# Copy the Python script into the container
COPY src/main/scripts/generateSalesInvoices.py /app/scripts/generateSalesInvoices.py

# Update package lists and install Python 3 and pip
RUN apt-get update && apt-get install -y python3 python3-pip

# Install the required Python packages
RUN pip3 install requests faker

# Expose the application port
EXPOSE 8080

# Define the command to run the application
ENTRYPOINT ["java", "-jar", "SalesInvoice-0.0.1-SNAPSHOT.jar"]
