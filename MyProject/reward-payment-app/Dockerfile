FROM eclipse-temurin:21

COPY reward-payment-app-1.0.1.jar reward-payment-app.jar

ENTRYPOINT ["java", "-Dspring.profiles.active=mysql-container", "-jar", "reward-payment-app.jar"]

EXPOSE 8095