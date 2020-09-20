#
# Build stage
#
FROM maven:3.6.0-jdk-8-slim AS build
COPY src /opt/apps/eu-clinical-trial-web-scraper/src
COPY pom.xml /opt/apps/eu-clinical-trial-web-scraper
RUN mvn -f /opt/apps/eu-clinical-trial-web-scraper/pom.xml clean package

#
# Package stage
#
FROM openjdk:8-jdk-alpine
COPY --from=build /opt/apps/eu-clinical-trial-web-scraper/target/clinical-trial-web-application-2.1-SNAPSHOT.jar clinical-trial-web-application-2.1-SNAPSHOT.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","clinical-trial-web-application-2.1-SNAPSHOT.jar"]

#docker build . -t clinicaltrialapp
#docker run -it -d -p 8080:8080 clinicaltrialapp:latest

