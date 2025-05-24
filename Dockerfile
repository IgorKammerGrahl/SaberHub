# Estágio de construção
FROM maven:3.9.9-eclipse-temurin-17-alpine AS build

WORKDIR /app

# Copia apenas os arquivos necessários para o build
COPY pom.xml .
COPY src ./src

# Baixa dependências e empacota o projeto
RUN mvn -B dependency:go-offline
RUN mvn clean package -DskipTests

# ---

# Estágio de produção
FROM eclipse-temurin:17-jdk-alpine

# Configura usuário seguro
RUN addgroup -S spring && adduser -S spring -G spring
USER spring:spring

WORKDIR /app

# Copia o JAR gerado
COPY --from=build /app/target/elearning-platform-*.jar app.jar

# Expõe a porta e define o entrypoint
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]