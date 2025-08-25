# Usar OpenJDK 21 como imagen base
FROM openjdk:21-jdk-slim

# Establecer directorio de trabajo
WORKDIR /app

# Copiar el archivo JAR de la aplicación
COPY target/learning-platform-0.0.1-SNAPSHOT.jar app.jar

# Exponer el puerto 8081
EXPOSE 8081

# Configurar variables de entorno para producción
ENV JAVA_OPTS="-Xmx512m -Xms256m"
ENV SPRING_PROFILES_ACTIVE=docker

# Ejecutar la aplicación
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]
