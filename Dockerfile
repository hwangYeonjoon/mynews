# 빌드용 이미지
FROM gradle:8.4.0-jdk17 AS build
WORKDIR /app
COPY . .

# ✅ gradle → ./gradlew 로 변경
RUN ./gradlew build --no-daemon

# 실행용 이미지
FROM eclipse-temurin:17-jdk
WORKDIR /app
COPY --from=build /app/build/libs/*.jar app.jar

# 실행 명령
ENTRYPOINT ["java", "-jar", "app.jar"]