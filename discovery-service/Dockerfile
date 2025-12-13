FROM openjdk:24-jdk

WORKDIR /app
COPY build/libs/*-SNAPSHOT.jar app.jar
EXPOSE 8761
ENTRYPOINT [ "java", "-jar", "app.jar" ]