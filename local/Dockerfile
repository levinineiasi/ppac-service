FROM gradle:7.2.0-jdk17 AS build
COPY --chown=gradle:gradle . /home/gradle/src
WORKDIR /home/gradle/src
RUN gradle --watch-fs build --no-daemon

ENV SPRING_PROFILES=dev

EXPOSE 8080

# here we start server in a simpler way, through gradle
ENTRYPOINT ["gradle", "bootRun", "--args='--spring.profiles.active=${SPRING_PROFILES}'"]