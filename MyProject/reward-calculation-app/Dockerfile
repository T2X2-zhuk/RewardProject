FROM eclipse-temurin:21

COPY reward-calculation-app-1.0.1.jar reward-calculation-app.jar

ENTRYPOINT ["java", "-Dspring.profiles.active=mysql-container", "-jar", "reward-calculation-app.jar"]

EXPOSE 8080