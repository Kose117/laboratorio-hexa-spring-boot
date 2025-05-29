# Usa una imagen base de OpenJDK
FROM openjdk:17-jdk-slim

# Instala netcat para poder realizar la verificación de conexión
RUN apt-get update && apt-get install -y netcat

# Establece el directorio de trabajo
WORKDIR /app

# Copia el archivo JAR de tu aplicación al contenedor
COPY rest-input-adapter/target/rest-input-adapter-0.0.1-SNAPSHOT.jar app.jar

# Comando para esperar a que MariaDB esté disponible y luego ejecutar la aplicación
ENTRYPOINT ["sh", "-c", "while ! nc -z mariadb-container 3306; do echo 'Esperando a que MariaDB esté disponible...'; sleep 1; done; echo 'MariaDB está listo!'; java -jar app.jar"]
