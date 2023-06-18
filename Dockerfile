FROM openjdk:8
VOLUME /tmp
ADD target/developmentHiring-1.0-SNAPSHOT.jar development-hiring.jar
EXPOSE 8080
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/development-hiring.jar"]