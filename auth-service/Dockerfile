FROM gradle:jdk21-alpine AS builder
ENV APP_HOME=/usr/app
WORKDIR $APP_HOME
COPY build.gradle .
COPY ./src ./src
RUN gradle clean bootJar

FROM eclipse-temurin:21.0.4_7-jre-alpine AS final
ENV APP_HOME=/usr/app
WORKDIR $APP_HOME
COPY --from=builder $APP_HOME/build/libs/*.jar .
EXPOSE 8080
ENTRYPOINT exec java -jar *.jar