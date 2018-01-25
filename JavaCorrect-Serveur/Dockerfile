FROM openjdk:8

COPY . /Javacorrect

WORKDIR /Javacorrect

RUN apt-get update && apt-get install -y maven
RUN mvn assembly:assembly -DdescriptorId=jar-with-dependencies
RUN java -jar target/gestionMail-0.0.1-SNAPSHOT-jar-with-dependencies.jar
