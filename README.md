# Neo4j Demo Application

A simple demo app that utilises SpringBoot 3.2, Kotlin 1.9, Java 21, & Neo4j

Data & DB Structure used from: https://github.com/jpatokal/openflights

Another good demo is available here:
https://github.com/neo4j-examples/movies-java-spring-data-neo4j

Neo4j Prompting Patterns found in this course -
[Build an Neo4j-backed Chatbot using Python](https://graphacademy.neo4j.com/courses/llm-chatbot-python/?ref=github) 

https://neo4j.com/docs/graph-data-science/current/installation/neo4j-server/

Build & run the application (skip tests)
```
./gradlew clean build -x test -x integTest
```

As it takes a while to load in all the dat files via liquibase & the autogenerated data, I suggest:
- Starting up docker compose with mysql (`./gradlew composeUp`)
- Running the Application through IntelliJ

## Version with Axion

Tagging a new release (ensure to have appropriate permissions in GitHub)

```shell
./gradlew release -Prelease.disableChecks
```

Use the `-Prelease.scope` flag to indicate incremental level, i.e. `-Prelease.scope=incrementMajor` for a major release

Test it out locally with `-Prelease.dryRun`

Versioning starts from `1.0.0` by default

## Performing a release:

After versioning the release, re-run the build
```shell
./gradlew clean build -x test -x integTest
```

In the build/libs, you'll see the jar with the latest release version.

Next run the docker build
```shell
docker build -t $(basename "$PWD"):latest -t $(basename "$PWD"):$(git describe --tags) -f docker/Dockerfile .
```

Publish the docker image where required.

## Running the Docker image

Testing out the docker image locally (use the network address displayed during the composeUp command)
```shell
docker run --network=<YOUR_NETWORK_ID> -e SPRING_PROFILES_ACTIVE=docker -p 8080:8080 -d neo4jdemo
```

Alternatively, you can add the image to the docker-compose file & run the app from there

https://erangad.medium.com/using-openai-semantic-search-with-neo4j-8cab050ee553
https://medium.com/neo4j/cosine-similarity-in-neo4j-d617b0442439
https://neo4j.com/docs/graph-data-science/current/algorithms/similarity-functions/
