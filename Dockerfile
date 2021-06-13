FROM adoptopenjdk/openjdk11:alpine-jre

# maintainer info
LABEL maintainer="suchait.gaurav.ctr@sabre.com"

# add volume pointing to /tmp
VOLUME /tmp

# Make port 8090 available to the world outside the container
EXPOSE 8092

# application jar file when packaged
ARG jar_file=target/teletype-ingest-service.jar

# add application jar file to container
COPY ${jar_file} teletype-ingest-service.jar

# run the jar file
ENTRYPOINT ["java", "-jar", "teletype-ingest-service.jar"]