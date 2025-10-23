FROM eclipse-temurin:21

COPY wait-for-it.sh wait-for-it.sh
RUN chmod +x /wait-for-it.sh

# Копируем jar приложения Liquibase
COPY liquibase-container-app-1.0.1.jar /liquibase-container-app.jar

# Ждём обе базы перед запуском
CMD ["/bin/sh", "-c", "/wait-for-it.sh reward-calculation-db:3306 --timeout=60 -- /wait-for-it.sh reward-payment-db:3306 --timeout=60 -- java -jar /liquibase-container-app.jar"]
EXPOSE 8080
