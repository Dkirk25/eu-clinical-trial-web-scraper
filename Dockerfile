# For Java 8, try this
FROM openjdk:8-jdk-alpine
# For Java 11, try this
#FROM adoptopenjdk/openjdk11:alpine-jre

# Refer to Maven build -> finalName
ARG JAR_FILE=target/clinical-trial-web-application-2.1-SNAPSHOT.jar

# cd /opt/app
WORKDIR /opt/apps/clinical-trial-web-application



# cp target/spring-boot-web.jar /opt/app/app.jar
COPY ${JAR_FILE} clinical-trial-web-application-2.1-SNAPSHOT.jar

EXPOSE 8080

# java -jar /opt/app/app.jar
ENTRYPOINT ["java","-jar","clinical-trial-web-application-2.1-SNAPSHOT.jar"]


#docker build . -t clinicaltrialapp
#docker run -it -d -p 8080:8080 clinicaltrialapp:latest

