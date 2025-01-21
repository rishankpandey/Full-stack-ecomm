FROM maven:3.8.5-openjdk-17 AS build
COPY . .
RUN mvn clean package -DshipTests

FROM openjdk:17.0.1-jdk-slim
COPY --from=build/target/spring-boot-ecommerce-0.0.1-SNAPSHOT.jar demo.jar
EXPOSE 8443
ENTRYPOINT ["java","-jar","demo.jar"]