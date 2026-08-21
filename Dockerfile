# =========================================================
# ESTAGIO 1 - Build
# Usa JDK completo: precisa compilar.
# =========================================================
FROM eclipse-temurin:21-jdk-alpine AS build

WORKDIR /app

# Copiados primeiro e SOZINHOS, de proposito.
# O Docker guarda cada camada em cache e so refaz da primeira
# mudanca em diante. Como o pom.xml muda raramente e o codigo
# muda a cada commit, isolar o download de dependencias aqui
# faz com que ele seja reaproveitado do cache na maioria dos builds.
COPY mvnw .
COPY .mvn .mvn
COPY pom.xml .

RUN chmod +x mvnw && ./mvnw dependency:go-offline -B

# So agora o codigo-fonte. Alteracoes aqui invalidam apenas
# esta camada e as seguintes, nao o download de dependencias.
COPY src src

RUN ./mvnw clean package -DskipTests -B

# =========================================================
# ESTAGIO 2 - Runtime
# Usa apenas o JRE. Compilador, Maven, codigo-fonte e cache
# de dependencias ficam para tras, reduzindo drasticamente o
# tamanho da imagem final e a superficie de vulnerabilidades.
# =========================================================
FROM eclipse-temurin:21-jre-alpine AS runtime

# Usuario sem privilegios administrativos. Por padrao o processo
# dentro do contêiner roda como root; se a aplicacao for explorada,
# o atacante herda esse poder. Um usuario comum limita o alcance.
RUN addgroup -S spring && adduser -S spring -G spring

WORKDIR /app

COPY --from=build --chown=spring:spring /app/target/*.jar app.jar

USER spring

ENV TZ=America/Sao_Paulo

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]