FROM maven:3.8.1-openjdk-17 AS build
COPY src /home/app/src
COPY pom.xml /home/app
WORKDIR /home/app
RUN mvn clean install

FROM openjdk:17-jdk-alpine
ARG JAR_FILE=checkout-service-0.0.1-SNAPSHOT.jar
COPY --from=build /home/app/target/${JAR_FILE} app.jar
ENTRYPOINT ["java","-jar","/app.jar"]

