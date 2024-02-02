FROM openjdk:21-jdk-slim

ARG APP_HOME=/app
ARG JAR_FILE=build/libs/neo4jdemo-*.jar

WORKDIR $APP_HOME
COPY ${JAR_FILE} app.jar

RUN groupadd -r appservice && useradd -r -g appservice appservice
RUN chown -R appservice:appservice $APP_HOME

USER appservice

ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom", "-DJAVA_OPTS='-Xms128m -Xmx2g'","-jar", "app.jar"]
