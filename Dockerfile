# ----- ESTÁGIO DE BUILD (Compila a aplicação Quarkus) -----
# Usamos uma imagem Red Hat UBI com OpenJDK 17
FROM registry.access.redhat.com/ubi8/openjdk-17 AS build
WORKDIR /app

# Instala ferramentas essenciais que o Maven pode precisar (tar, gzip, unzip)
# Isso resolve o erro "Cannot exec: No such file or directory" para tar/gzip
RUN microdnf update -y && microdnf install -y tar gzip unzip

# Copia o Maven Wrapper (mvnw e .mvn) com permissão de execução
# Isso resolve o erro "Permission denied" para mvnw
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
# Usamos uma imagem de runtime mais leve com OpenJDK 17
FROM registry.access.redhat.com/ubi8/openjdk-17-runtime
WORKDIR /work/

# Copia os artefatos compilados do estágio de build para o estágio de execução
COPY --from=build /app/target/quarkus-app/ .

# Expõe a porta padrão do Quarkus
EXPOSE 8080

# Comando para iniciar a aplicação Quarkus
CMD ["java", "-jar", "quarkus-run.jar"]