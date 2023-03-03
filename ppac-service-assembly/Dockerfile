FROM openjdk:17-slim

WORKDIR /opt/app
ARG JAR_FILE
ARG INFO_APP_BUILD

COPY ${JAR_FILE} /opt/app/app.jar
ENV INFO_APP_BUILD=${INFO_APP_BUILD}
ENV SPRING_PROFILES=docker

EXPOSE 8080

CMD java -Djavax.net.ssl.trustStore -XX:MaxRAMPercentage=50.0 -XX:InitialRAMPercentage=50.0 -Dspring.profiles.active=${SPRING_PROFILES} -jar /opt/app/app.jar