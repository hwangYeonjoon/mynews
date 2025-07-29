# 베이스 이미지 설정 (OpenJDK 17 이상 권장)
FROM openjdk:17-jdk-slim

# JAR 파일 복사 (your-app-name.jar 는 실제 빌드한 JAR 이름으로)
ARG JAR_FILE=build/libs/mynews-0.0.1-SNAPSHOT.jar
COPY ${JAR_FILE} app.jar

# 포트 노출
EXPOSE 8080

# 앱 실행
ENTRYPOINT ["java", "-jar", "/app.jar"]