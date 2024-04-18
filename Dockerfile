# Stage 1: Project Build
FROM maven:3.9.6-amazoncorretto-21-al2023 AS build
WORKDIR /app

COPY pom.xml .
COPY src ./src

RUN mvn package -DskipTests

# Stage 2: Project execution
FROM amazoncorretto:21
WORKDIR /app

# Copy the JAR generated in the previous stage to the container
COPY --from=build app/target/*.jar /app/superheroes.jar

EXPOSE 8080

ENTRYPOINT ["java","-jar","superheroes.jar","-web -webAllowOthers -tcp -tcpAllowOthers -browser"]
