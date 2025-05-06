FROM openjdk:17-jdk-slim

RUN apt-get update && apt-get install -y netcat

WORKDIR /app

COPY rest-input-adapter/target/rest-input-adapter-0.0.1-SNAPSHOT.jar app.jar

ENTRYPOINT ["sh", "-c", "while ! nc -z mariadb-container 3306; do echo 'Esperando a que MariaDB esté disponible...'; sleep 1; done; echo 'MariaDB está listo!'; java -jar app.jar"]
