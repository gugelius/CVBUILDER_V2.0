# Используем базовый образ с Eclipse Temurin для JDK 22
FROM eclipse-temurin:22-jdk AS build

# Устанавливаем рабочую директорию внутри контейнера
WORKDIR /app

# Копируем проект внутрь контейнера
COPY . .

# Делаем gradlew исполняемым и собираем JAR
RUN chmod +x gradlew && ./gradlew bootJar --no-daemon

# Используем легковесный образ для финального контейнера
FROM eclipse-temurin:22-jre
WORKDIR /app
COPY --from=build /app/build/libs/*.jar app.jar

# Открываем порт 8080
EXPOSE 8080

# Запускаем приложение
CMD ["java", "-jar", "app.jar"]
