FROM eclipse-temurin:21

COPY liquibase-container-app-1.0.1.jar liquibase-container-app.jar

ENTRYPOINT ["java", "-Dspring.profiles.active=mysql-container", "-jar", "liquibase-container-app.jar"]

EXPOSE 8080
