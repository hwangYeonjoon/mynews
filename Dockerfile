# 1단계: 빌드
FROM gradle:8.4.0-jdk17 AS build
WORKDIR /app
COPY . .
RUN ./gradlew bootJar --no-daemon

# 2단계: 실행
FROM eclipse-temurin:17-jdk
WORKDIR /app
COPY --from=build /app/build/libs/app.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]