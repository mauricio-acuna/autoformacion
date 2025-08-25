# Odin Team Learning Platform

## Descripción General

La plataforma Odin Team Learning Platform es una solución integral para la gestión, desarrollo y monitoreo de proyectos educativos y empresariales. Este proyecto combina tecnologías modernas de frontend y backend, integrando herramientas de monitoreo, migraciones de base de datos, y despliegue en contenedores, todo en un solo repositorio.

## Características Principales

- **Frontend moderno**: Utiliza Next.js y Tailwind CSS para una experiencia de usuario rápida, responsiva y atractiva.
- **Backend robusto**: Basado en Java y Spring Boot, con migraciones gestionadas por Flyway y configuración flexible.
- **Gestión de base de datos**: Prisma y migraciones SQL para mantener la integridad y evolución del esquema.
- **Despliegue sencillo**: Docker y Docker Compose para facilitar la ejecución y el despliegue en cualquier entorno.
- **Monitoreo**: Integración con Prometheus para métricas y monitoreo de servicios.
- **Testing y cobertura**: Incluye pruebas automatizadas y reportes de cobertura con Jacoco.

## Estructura del Proyecto

```
ODIN/
├── build.gradle, settings.gradle, gradlew, gradlew.bat
├── docker-compose.yml, Dockerfile
├── next.config.js, package.json, postcss.config.js, tailwind.config.ts, tsconfig.json
├── src/
│   ├── app/ (Frontend Next.js)
│   ├── components/ui/ (Componentes reutilizables)
│   ├── lib/ (Utilidades)
│   ├── main/java/com/pluto/learning/ (Backend Java)
│   ├── main/resources/ (Configuración y migraciones)
│   ├── test/java/com/pluto/learning/ (Pruebas backend)
│   ├── test/resources/ (Configuración de pruebas)
│   └── types/ (Definiciones TypeScript)
├── prisma/schema.prisma (Modelo de base de datos)
├── monitoring/prometheus.yml (Configuración de monitoreo)
├── build/ (Artefactos, reportes y resultados de pruebas)
└── README.md, README_status.md, prd.md, prd-expanded.md
```

## Tecnologías Utilizadas

- **Frontend**: Next.js, React, Tailwind CSS, TypeScript
- **Backend**: Java, Spring Boot, Gradle
- **Base de datos**: Prisma, SQL
- **DevOps**: Docker, Docker Compose
- **Monitoreo**: Prometheus
- **Testing**: Jacoco, JUnit

## Instalación y Ejecución

### Requisitos
- Node.js >= 18
- Java >= 17
- Docker
- Gradle (incluido en el proyecto)

### Pasos rápidos

1. **Clonar el repositorio**
   ```sh
   git clone https://github.com/mauricio-acuna/autoformacion.git
   cd autoformacion/ODIN
   ```
2. **Instalar dependencias frontend**
   ```sh
   npm install
   ```
3. **Construir y ejecutar backend**
   ```sh
   ./gradlew build
   ./gradlew bootRun
   ```
4. **Ejecutar con Docker**
   ```sh
   docker-compose up --build
   ```

## Migraciones de Base de Datos

Las migraciones se gestionan con Flyway y Prisma. Los scripts SQL se encuentran en `src/main/resources/db/migration/` y el esquema en `prisma/schema.prisma`.

## Monitoreo

La configuración de Prometheus está en `monitoring/prometheus.yml`. Permite recolectar métricas del backend y visualizar el estado de los servicios.

## Testing y Reportes

- Pruebas unitarias y de integración en `src/test/java/com/pluto/learning/`.
- Reportes de cobertura en `build/reports/tests/test/index.html`.

## Personalización

- Modifica los archivos de configuración para adaptar el entorno (`application.properties`, `docker-compose.yml`, etc).
- Agrega migraciones SQL para evolucionar la base de datos.
- Crea nuevos componentes UI en `src/components/ui/`.

## Contribución

1. Haz un fork del repositorio.
2. Crea una rama con tu feature o fix.
3. Realiza un pull request detallando los cambios.

## Licencia

Este proyecto está bajo la licencia MIT.

## Contacto y Soporte

Para dudas, sugerencias o soporte, contacta al equipo Odin Team o abre un issue en el repositorio.

---

¡Esperamos que disfrutes y contribuyas a Odin Team Learning Platform!
