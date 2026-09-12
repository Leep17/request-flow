# Первая стадия multi-stage Docker build.
#
# Используем готовый image, в котором уже есть:
# - Maven 3.9
# - JDK 21
#
# AS build даёт этой стадии имя "build".
# Потом из неё мы заберём собранный JAR.
FROM maven:3.9-eclipse-temurin-21 AS build


# Устанавливаем рабочую директорию внутри этой стадии контейнера.
#
# Все следующие относительные пути будут считаться относительно /app.
#
# Например:
# COPY pom.xml .
#
# положит pom.xml в:
# /app/pom.xml
WORKDIR /app


# Копируем pom.xml из проекта на компьютере
# внутрь build-stage в /app.
#
# Maven использует pom.xml для понимания:
# - зависимостей
# - плагинов
# - версии Java
# - процесса сборки проекта
COPY pom.xml .


# Копируем конфигурацию Checkstyle.
#
# Она нужна, потому что во время mvn clean package
# у нас запускается Maven Checkstyle Plugin.
COPY checkstyle.xml .


# Дополнительный файл настроек Checkstyle.
#
# checkstyle.xml ссылается на suppressions.xml,
# поэтому без него Docker-сборка падала с ошибкой:
# Unable to find: suppressions.xml
COPY suppressions.xml .


# Копируем исходный код проекта.
#
# Слева:
# src
# — папка на нашем компьютере, входящая в Docker build context.
#
# Справа:
# ./src
# — каталог внутри /app.
#
# В результате:
# /app/src
COPY src ./src


# Выполняем Maven-сборку ВНУТРИ Docker build-stage.
#
# clean
# → удаляет предыдущие результаты сборки.
#
# package
# → компилирует проект и создаёт JAR.
#
# -DskipTests
# → тесты не запускаются.
#
# После выполнения появляется примерно:
# /app/target/request-flow-1.0-SNAPSHOT.jar
RUN mvn clean package -DskipTests



# Начинается ВТОРАЯ стадия Dockerfile.
#
# Здесь Maven и JDK для сборки уже не нужны.
# Нам достаточно JRE 21, потому что приложение уже собрано
# и его нужно только запустить.
#
# Благодаря этому финальный image не содержит Maven
# и прочие инструменты сборки.
FROM eclipse-temurin:21-jre


# Рабочая директория уже финального runtime-image.
WORKDIR /app


# Копируем JAR из ПЕРВОЙ стадии с именем "build".
#
# --from=build
# → взять файл не с нашего Mac,
#   а из предыдущей стадии Dockerfile.
#
# /app/target/*.jar
# → путь к JAR, который собрал Maven.
#
# app.jar
# → имя файла в текущей runtime-stage.
#
# В итоге внутри финального image будет:
# /app/app.jar
COPY --from=build /app/target/*.jar app.jar


# Команда, которая будет выполняться
# при запуске контейнера из этого image.
#
# По сути выполняется:
#
# java -jar app.jar
#
# То есть запускается Spring Boot приложение.
#
# ENTRYPOINT в JSON-форме считается предпочтительной:
# ["java", "-jar", "app.jar"]
ENTRYPOINT ["java", "-jar", "app.jar"]