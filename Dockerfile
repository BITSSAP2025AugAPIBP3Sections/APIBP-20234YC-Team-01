FROM eclipse-temurin:17-jre
ADD target/GreenGrub-0.0.1-SNAPSHOT.jar greengrub.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/greengrub.jar"]