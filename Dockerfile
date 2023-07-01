FROM amazoncorretto:20
COPY target/*.jar filmorate.jar
ENTRYPOINT ["java", "-jar", "/filmorate.jar"]