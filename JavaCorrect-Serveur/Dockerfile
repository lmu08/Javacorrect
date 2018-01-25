# Use java 8 image.
FROM openjdk:8

# Copy all project files to the container.
COPY . /Javacorrect

# Set the default working directory.
WORKDIR /Javacorrect

# Install maven.
RUN apt-get update && apt-get install -y maven

# Build the .jar file with all the dependencies of this project.
RUN mvn assembly:assembly -DdescriptorId=jar-with-dependencies

# Run the archive to start the server?
RUN java -jar target/gestionMail-0.0.1-SNAPSHOT-jar-with-dependencies.jar