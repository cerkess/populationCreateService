FROM openjdk:17
VOLUME /tmp
ADD target/populationCreateService-1.0-SNAPSHOT.jar population-create-service.jar
EXPOSE 8080
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/population-create-service.jar"]