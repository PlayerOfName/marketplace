FROM eclipse-temurin:21-jdk-jammy
LABEL authors="shvets"

WORKDIR /app
COPY target/*.jar app.jar
EXPOSE 8080

# Указываем профиль через переменную окружения (со значением по умолчанию)
ENV PROFILE=dev
ENTRYPOINT ["java", "-jar", "app.jar", "--spring.profiles.active=${PROFILE}"]