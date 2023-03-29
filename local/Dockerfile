FROM gradle:7.2.0-jdk17 AS build
COPY --chown=gradle:gradle . /home/gradle/src
WORKDIR /home/gradle/src
RUN gradle --watch-fs build --no-daemon

FROM openjdk:17-slim

ENV SPRING_PROFILES=docker

EXPOSE 8080

RUN mkdir /opt/app

COPY --from=build /home/gradle/src/ppac-service-assembly/build/libs/*.jar /opt/app/app.jar

CMD java -Djavax.net.ssl.trustStore -XX:MaxRAMPercentage=50.0 -XX:InitialRAMPercentage=50.0 -Dspring.profiles.active=${SPRING_PROFILES} --add-opens java.base/java.lang=ALL-UNNAMED -jar /opt/app/app.jar