FROM amazoncorretto:21

ARG JAR_FILE=target/superhero-1.0.0.jar
COPY ${JAR_FILE} superhero.jar

EXPOSE 8080

ENTRYPOINT ["java","-jar","/superhero.jar"]
