# Dockerfile
FROM openjdk:21-ea-24-oracle

WORKDIR /app
# Aquí debemos asegurarnos del nombre de nuestro jar coincida
COPY target/citas-0.0.1-SNAPSHOT.jar app.jar
# Ubicación y nombre del wallet descomprimido
COPY Wallet_FullStack /app/oracle_wallet/
EXPOSE 8080

CMD ["java", "-jar", "app.jar"]
