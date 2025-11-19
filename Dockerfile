# Estágio de Build: Compila a aplicação Quarkus
FROM registry.access.redhat.com/ubi8/openjdk-17 AS build
WORKDIR /app
COPY educavrv/mvnw .
COPY educavrv/.mvn .mvn
COPY educavrv/src src
RUN ./mvnw package -DskipTests

# Estágio de Execução: Executa a aplicação Quarkus
FROM registry.access.redhat.com/ubi8/openjdk-17-runtime
WORKDIR /work/
COPY --from=build /app/target/quarkus-app/ .
EXPOSE 8080
CMD ["java", "-jar", "quarkus-run.jar"]
