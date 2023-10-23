# Use the official OpenJDK base image
FROM openjdk:11

# Set the working directory in the container
WORKDIR /app

# Copy the JAR file from your project to the container
COPY target/github-repository-list-api-1.0.0.jar /app/

# Expose the port your application will run on
EXPOSE 8080

# Define the command to run your application
CMD ["java", "-jar", "github-repository-list-api-1.0.0.jar"]