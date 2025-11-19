# ----- ESTÁGIO DE BUILD (Compila a aplicação Quarkus) -----
# Usamos uma imagem Eclipse Temurin (OpenJDK) 17
FROM eclipse-temurin:17-jdk-focal AS build
WORKDIR /app

# REMOVEMOS A LINHA DE microdnf AQUI! Ela não é mais necessária.

# Copia o Maven Wrapper (mvnw e .mvn) com permissão de execução
COPY --chmod=+x educavrv/mvnw .
COPY educavrv/.mvn .mvn

# Copia o arquivo pom.xml (está na raiz do repo)
COPY pom.xml .

# Copia o código-fonte (está dentro da pasta educavrv/)
COPY educavrv/src src

# Compila a aplicação Quarkus, pulando os testes
# Isso cria o JAR executável em target/quarkus-app/
RUN ./mvnw package -DskipTests

# ----- ESTÁGIO DE EXECUÇÃO (Roda a aplicação compilada) -----
# Usamos uma imagem de runtime mais leve do Eclipse Temurin 17
FROM eclipse-temurin:17-jre-focal
WORKDIR /work/

# Copia os artefatos compilados do estágio de build para o estágio de execução
COPY --from=build /app/target/quarkus-app/ .

# Expõe a porta padrão do Quarkus
EXPOSE 8080

# Comando para iniciar a aplicação Quarkus
CMD ["java", "-jar", "quarkus-run.jar"]