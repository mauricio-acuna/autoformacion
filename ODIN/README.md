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
  git clone https://github.com/mauricio-acuna/weboffline.git
  cd weboffline/ODIN
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


## 📘 Guía de Onboarding, Mantenimiento y Evolución

Para una guía completa sobre funcionamiento, mantenimiento, mejores prácticas, producción y evolución, consulta:

- [`docs/guia_onboarding.md`](docs/guia_onboarding.md)

### Resumen rápido:
- Funcionamiento general: integración frontend, backend, bases de datos, eventos y observabilidad.
- Módulos principales: LMS, Assessment Engine, Career Platform, Enterprise Features.
- Mantenimiento: configuración centralizada, migraciones, tests, observabilidad, CI/CD.
- Mejores prácticas: versionado, calidad, seguridad, documentación, evolución.
- Producción: checklist de despliegue seguro y monitoreado.
- Onboarding: roadmap, primeros pasos, recursos clave, backlog evolutivo.

Para dudas, sugerencias o soporte, contacta al equipo Odin Team o abre un issue en el repositorio.

---

¡Esperamos que disfrutes y contribuyas a Odin Team Learning Platform!

### DevOps & CI/CD
- **GitHub Actions** - Matrix builds, quality gates, security scanning
- **SonarQube** - Code quality, security vulnerabilities, technical debt
- **Testcontainers** - Integration testing con PostgreSQL/MongoDB/Kafka
- **K6/JMeter** - Load testing, performance benchmarking

## 📋 Módulos Funcionales Expandidos

### 1. Learning Management System (LMS)
- **Adaptive Learning**: Algoritmos que ajustan dificultad según performance
- **Progress Dashboard**: Métricas detalladas por skill, tiempo, y project complexity
- **Learning Paths**: 12 meses estructurados en 4 fases con 24+ labs
- **Peer Learning**: Code review, pair programming virtual, mentorship matching

### 2. Assessment Engine Avanzado
- **Auto-grading**: Evaluación automática multi-dimensional (funcionalidad, calidad, performance)
- **Performance Analytics**: Benchmarks, optimization tracking, resource usage
- **Contract Testing**: Validación automática de APIs y integrations
- **Chaos Engineering**: Fault injection, resilience testing

### 3. Career Development Platform
- **Mock Interviews**: System design scenarios, coding challenges, behavioral prep
- **Portfolio Generator**: Automated GitHub showcase, technical blog integration
- **Job Matching**: Skills-to-market alignment, salary benchmarking
- **Professional Network**: Alumni connections, industry mentors, recruiters

### 4. Enterprise Features
- **Multi-tenancy**: Soporte para organizaciones, custom branding
- **Internationalization**: Multi-language support, localization
- **Compliance**: GDPR, audit logging, data governance
- **Analytics**: Real-time metrics, predictive insights, ROI tracking

## 🗓 Roadmap de Capacitación Expandido (12 meses)

### **FASE 1: FUNDAMENTOS MODERNOS (Meses 1–3)**
- **Mes 1**: Java 21 + Performance Engineering (Virtual Threads, GC tuning, profiling)
- **Mes 2**: Spring Boot 3.3 Enterprise (Security, custom starters, observability)  
- **Mes 3**: Persistencia Excellence (PostgreSQL advanced, MongoDB, testing strategies)

### **FASE 2: DISTRIBUTED SYSTEMS (Meses 4–6)**
- **Mes 4**: Event-Driven Architecture (Kafka deep dive, schema evolution)
- **Mes 5**: Microservices Resilience (Saga patterns, circuit breakers, chaos engineering)
- **Mes 6**: API Excellence & Security (OpenAPI 3.1, OAuth2 mastery, API Gateway)

### **FASE 3: CLOUD-NATIVE EXCELLENCE (Meses 7–9)**
- **Mes 7**: Kubernetes Production (Advanced workloads, Helm, GitOps)
- **Mes 8**: Observabilidad Enterprise (Metrics, tracing, alerting, SRE practices)
- **Mes 9**: Production Operations (CI/CD excellence, deployment strategies)

### **FASE 4: ESPECIALIZACIÓN Y CAPSTONE (Meses 10–12)**
- **Mes 10**: Performance & Auto-Scaling (Load testing, optimization, capacity planning)
- **Mes 11**: Enterprise Features (Stream processing, search, cloud integration)
- **Mes 12**: Capstone "Pluto-Lite" + Career Mastery (Portfolio, interviews, networking)

### 3. Sistema de Evaluaciones
#### Quizzes
- Auto-corrección con banco etiquetado por skill y dificultad
- 20-30 preguntas por módulo
- **Aprobado ≥ 75%**

#### Labs Evaluables
- Integración con GitHub
- Ejecución automática con **Maven + Testcontainers**
- Scoring automático:
  - **Funcionalidad** (40%): endpoints/eventos pasan tests
  - **Calidad** (25%): cobertura ≥ 75%, checkstyle/spotbugs sin blockers
  - **Resiliencia** (20%): circuit breaker, retries, timeouts
  - **Operabilidad** (15%): métricas y health checks expuestos

#### Contract Testing
- Validación de OpenAPI publicado
- Verificación de compatibilidad entre servicios

### 4. Observabilidad de Aprendizajes
- Métricas de intentos y tiempo de resolución
- Detección de flaky tests
- SLOs personales de aprendizaje

### 5. Portafolio Público
- Página con demos y métricas verificables
- Badges de logros y certificaciones
- Enlaces a repositorios y evidencias

## 🏗 Arquitectura de la Aplicación

### Módulos Backend (Spring Boot)
```
src/main/java/com/pluto/learning/
├── auth/           # Gestión de usuarios y autenticación
├── content/        # Gestión de contenidos y módulos
├── assessments/    # Sistema de evaluaciones
├── submissions/    # Procesamiento de entregas
└── scoring/        # Cálculo de puntuaciones
```

### Modelo de Datos
- **PostgreSQL**: `User`, `Skill`, `Module`, `Submission`, `Score`, `Badge`, `Rubric`, `Artifact`, `KPI`
- **MongoDB**: `Lesson`, `QuizQuestion`, `Lab` (contenidos versionados)

## 🚀 Contenidos de Aprendizaje

### Java 21 (LTS)
- Virtual Threads y concurrencia moderna
- Records y Sealed Classes
- Pattern Matching avanzado
- Novedades del lenguaje

### Spring Boot 3
- Actuator y configuración
- Micrometer y observabilidad
- Spring Security 6
- Migración desde Spring Boot 2.x

### Apache Kafka
- Patrones de eventos
- Transacciones y consistencia
- Schema evolution
- Consumer groups e idempotencia

### Persistencia
- PostgreSQL avanzado
- MongoDB para documentos
- Patrones de persistencia mixta

### Docker & Kubernetes
- Despliegue de microservicios
- Helm charts
- Ingress y load balancing
- Health checks y probes

### Observabilidad
- Micrometer + Prometheus
- OpenTelemetry tracing
- Dashboards en Grafana
- SLOs y alerting

## 📊 Métricas de Éxito

### Progreso Formativo
- **≥ 85%** de media en quizzes
- **100%** de módulos obligatorios completados
- **3** microservicios funcionales documentados
- Cobertura media **≥ 75%**
- Quality gate **verde** (sin blockers)

### Preparación Profesional
- **2** simulacros de system design aprobados (≥ 70%)
- **1** demo pública desplegada en K8s
- CV y LinkedIn actualizados con evidencias
- Dominio explicativo del stack completo

## 🛠 Instalación y Configuración

### Prerrequisitos
- Java 21
- Maven 3.9+
- Docker & Docker Compose
- PostgreSQL 15+
- MongoDB 7+
- Apache Kafka 3.x

### Configuración de Base de Datos
```bash
# PostgreSQL
createdb pluto_learning

# MongoDB
mongosh --eval "use pluto_learning_content"
```

### Configuración de Kafka
```bash
# Iniciar Kafka con Docker Compose
docker-compose up -d kafka zookeeper
```

### Configuración de Keycloak
```bash
# Iniciar Keycloak
docker run -p 8080:8080 -e KEYCLOAK_ADMIN=admin -e KEYCLOAK_ADMIN_PASSWORD=admin quay.io/keycloak/keycloak:latest start-dev
```

### Ejecutar la Aplicación
```bash
# Backend
mvn spring-boot:run

# La aplicación estará disponible en:
# - API: http://localhost:8081
# - Swagger UI: http://localhost:8081/swagger-ui.html
# - Actuator: http://localhost:8081/actuator
```

## 🧪 Testing

### Ejecutar Tests
```bash
# Tests unitarios e integración
mvn test

# Solo tests unitarios
mvn test -Dtest="**/*Test"

# Solo tests de integración
mvn test -Dtest="**/*IT"

# Reporte de cobertura
mvn jacoco:report
```

### Testcontainers
Los tests de integración utilizan Testcontainers para:
- PostgreSQL (tests de persistencia)
- MongoDB (tests de contenido)
- Kafka (tests de eventos)

## 📈 Observabilidad

### Métricas (Prometheus)
- Endpoint: `http://localhost:8081/actuator/prometheus`
- Métricas de negocio: completions, scores, attempts
- Métricas técnicas: JVM, HTTP, DB connections

### Health Checks
- Endpoint: `http://localhost:8081/actuator/health`
- Verificaciones: DB, MongoDB, Kafka, External APIs

## 🔧 Desarrollo

### Quality Gates
- Cobertura de tests ≥ 75%
- Sin violations de Checkstyle
- Sin bugs críticos en SpotBugs
- Todos los tests pasan

### CI/CD Pipeline
```yaml
# .github/workflows/ci.yml
- Build & Test
- Quality Gate (Sonar)
- Security Scan
- Docker Build
- Deploy to K8s (staging)
```

## 📚 Roadmap de Labs

1. **REST + OpenAPI**: CRUD con PostgreSQL, documentación automática
2. **Kafka Básico**: Productor/consumidor, consumer groups
3. **Kafka Transaccional**: Idempotencia, transacciones
4. **Saga/Outbox**: Consistencia entre PostgreSQL y Kafka
5. **Security**: OAuth2/OIDC con Keycloak
6. **Observabilidad**: Métricas HTTP/DB + traces
7. **Kubernetes**: Despliegue con Helm + Ingress + HPA
8. **Migración SOAP→REST**: Escenario del proyecto Pluto

## 🎯 Capstone: "Pluto-lite"

Flujo completo de **provisión de TV**:
- Orden → Eventos Kafka → Provisioning → Confirmaciones → Notificaciones
- **Exactly-once** donde aplique
- Compensaciones de saga
- Migración de un endpoint SOAP→REST

## 📖 Documentación

### ADRs (Architecture Decision Records)
- `docs/adr/` - Decisiones arquitectónicas documentadas

### API Documentation
- OpenAPI 3.0 disponible en `/api-docs`
- Swagger UI en `/swagger-ui.html`

### Runbooks
- `docs/runbooks/` - Procedimientos operacionales

---

**Estado del Proyecto:** 🚧 En desarrollo activo  
**Próximo Hito:** MVP con módulos básicos y primer lab evaluable

Para más información sobre el proyecto Pluto y contexto del dominio OTT/TV, consultar `prd.md`.
