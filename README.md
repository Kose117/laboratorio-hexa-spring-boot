```markdown
# personapp-hexa-spring-boot
**Plantilla de Laboratorio: Arquitectura Limpia + Spring Boot + CLI/REST**

---

## 𝗣𝗿𝗼𝗽𝗼𝘀𝗶𝘁𝗼
El propósito de este repositorio es servir como **ejemplo didáctico** de una aplicación desarrollada sobre la Arquitectura Hexagonal (o Limpia), donde la lógica de negocio está completamente desacoplada de los adaptadores de entrada (CLI y REST) y de los adaptadores de salida (MariaDB y MongoDB). Esto permite:
- Alternar *bases de datos* (MariaDB ↔ MongoDB) sin modificar los casos de uso.  
- Tener dos *puntos de entrada* (línea de comandos o API REST) ejecutándose de forma independiente.  
- Facilitar el mantenimiento y la extensión a nuevos adaptadores (por ejemplo, agregar un canal de mensajería en el futuro).

---

## 𝑹𝒆𝒒𝒖𝒊𝒔𝒊𝒕𝒐𝒔
Antes de comenzar, asegúrate de contar con:
1. **Java 11 o superior** instalado y con `JAVA_HOME` correctamente apuntado.  
2. **Maven 3.6+** para compilar el proyecto.  
3. **Docker & Docker Compose** (versión reciente) para levantar contenedores de la capa Infraestructura.  
4. **MariaDB** configurado para escuchar en el puerto **3307**, con usuario/contraseña válidos.  
5. **MongoDB** escuchando en el puerto **27017**.  
6. **Lombok** agregado en el `pom.xml` y **Annotation Processing** habilitado en el IDE:
   - En **IntelliJ**: Settings → Build, Execution, Deployment → Compiler → Annotation Processors → marcar “Enable”.  
   - En **Eclipse**: Project → Properties → Java Compiler → Annotation Processing → activar.  
   - En **VS Code**: Instalar “Java Extension Pack” y en `.vscode/settings.json`:
     ```jsonc
     {
       "java.compile.settings.annotationProcessing.enabled": true
     }
     ```

---


## 𝗘𝗷𝗲𝗰𝘂𝗰𝗶𝗼́𝗻

**Adaptador REST**  
- El servicio REST se levanta en el puerto **3000**.  
- La interfaz de Swagger UI estará disponible en: http://localhost:3000/swagger-ui.html

---

### Paso 1: Construir el proyecto
mvn clean package

### Paso 2: Levantar Swagger (REST + Swagger UI)
docker compose up -d --build

### Paso 3: Ejecutar el CLI
java -jar cli-input-adapter/target/cli-input-adapter-0.0.1-SNAPSHOT.jar