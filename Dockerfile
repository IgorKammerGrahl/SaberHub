FROM eclipse-temurin:17-jdk-alpine

# Cria usuário e grupo para segurança
RUN addgroup -S spring && adduser -S spring -G spring
USER spring:spring

WORKDIR /app

# Copia o JAR e o application.properties
COPY target/*.jar app.jar
COPY src/main/resources/application.properties ./config/

ENTRYPOINT ["java", "-jar", "app.jar", "--spring.config.location=classpath:/application.properties,file:/app/config/application.properties"]