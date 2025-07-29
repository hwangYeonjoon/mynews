# 빌드용 이미지
FROM gradle:8.4.0-jdk17 AS build
WORKDIR /app

# gradle wrapper까지 포함한 모든 소스 복사
COPY . .

# ✅ gradle 대신 ./gradlew 사용
RUN ./gradlew build --no-daemon

# 실행용 이미지
FROM eclipse-temurin:17-jdk
WORKDIR /app

# 빌드된 JAR 복사 (와일드카드 처리)
COPY --from=build /app/build/libs/*.jar app.jar

# 실행 명령
ENTRYPOINT ["java", "-jar", "app.jar"]