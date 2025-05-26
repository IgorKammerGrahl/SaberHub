# backend/Dockerfile

# Estágio de construção otimizado
FROM maven:3.9.9-eclipse-temurin-17-alpine AS build

WORKDIR /app
COPY . .
RUN mvn -B dependency:go-offline
RUN mvn clean package -DskipTests

# Estágio de produção
FROM eclipse-temurin:17-jre-alpine

# Configuração de segurança
RUN addgroup -S spring && adduser -S spring -G spring
USER spring:spring

WORKDIR /app

# Copia o JAR mantendo o nome original
COPY --from=build /app/target/elearning-platform-*.jar app.jar

# Configurações essenciais
EXPOSE 8081
ENV SPRING_PROFILES_ACTIVE=docker

# Removido --server.port=8081 para evitar conflito com variável de ambiente no docker-compose
ENTRYPOINT ["java", "-jar", "app.jar"]