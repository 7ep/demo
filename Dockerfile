# Use the official Gradle image with JDK 11 as the base image
FROM gradle:7.6.1-jdk11  

# Set the working directory inside the container  
WORKDIR /desktop_app  

# Copy the Gradle build script into the container  
COPY build.gradle ./  

# Copy the Gradle wrapper scripts into the container  
COPY gradlew gradlew.bat ./  

# Copy the Gradle folder containing wrapper files  
COPY gradle ./gradle  

# Copy all other project files into the container  
COPY . .  

# Give execution permission to the Gradle wrapper script  
RUN chmod +x ./gradlew  

# Expose port 8080 for external access to the application  
EXPOSE 8080  

# Start the application using the Gradle wrapper,  
# with "--continuous" to automatically rebuild on file changes  
CMD ["./gradlew", "appRun", "--continuous"] 
#Fixing appRun because gradle is case sensitive 
