FROM openjdk:17-slim

WORKDIR /opt/app
ARG JAR_FILE
ARG INFO_APP_BUILD

COPY ${JAR_FILE} /opt/app/app.jar
ENV INFO_APP_BUILD=${INFO_APP_BUILD}
ENV SPRING_PROFILES=${SPRING_PROFILES}

ENV DB_HOST=${DB_HOST}
ENV DB_NAME=${DB_NAME}
ENV DB_SCHEMA=${DB_SCHEMA}
ENV DB_USER=${DB_USER}
ENV DB_PASS=${DB_PASS}

EXPOSE 8080

CMD java -Djavax.net.ssl.trustStore -XX:MaxRAMPercentage=50.0 -XX:InitialRAMPercentage=50.0 -Dspring.profiles.active=${SPRING_PROFILES} --add-opens java.base/java.lang=ALL-UNNAMED -jar /opt/app/app.jar