FROM openjdk:8-jdk-alpine
EXPOSE 8080
ADD target/lysaai-core-api.jar lysaai-core-api.jar
ENTRYPOINT ["java","-jar","/lysaai-core-api.jar"]