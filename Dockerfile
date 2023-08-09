FROM amazoncorretto:17
MAINTAINER hitzseb
COPY target/authentication-0.0.1-SNAPSHOT.jar authentication
ENTRYPOINT ["java", "-jar","authentication"]
EXPOSE 8080