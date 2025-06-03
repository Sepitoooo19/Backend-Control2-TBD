FROM openjdk:17
ARG JAR_FILE=./target/*.jar
COPY ${JAR_FILE} backend-control2.jar
ENTRYPOINT ["java","-jar","/backend-control2.jar"]