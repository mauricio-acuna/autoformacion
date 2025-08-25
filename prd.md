# PRD — Plataforma de Autoformación y Evaluación (Java Developer · Proyecto “Pluto”)

**Autor:** Mauricio Acuña  
**Horizonte:** 12 meses  
**Propósito:** Crear una **web de autoformación y evaluación** que consolide y mida el conocimiento técnico necesario para desempeñarse como **Java Developer** en un entorno similar al proyecto *Pluto* (OTT/TV, microservicios, Kafka, Spring Boot, PostgreSQL/MongoDB), y preparar entrevistas técnicas.

---

## 1) Objetivo principal

Diseñar y desplegar una web que:
1) centralice contenidos, prácticas y evaluaciones del **stack Pluto**;  
2) mida objetivamente el progreso con **rúbricas, exámenes, labs evaluables y CI/CD**;  
3) genere un **portafolio verificable** (repos públicos + evidencias) para entrevistas y procesos de selección.

---

## 2) Perfil actual (hoy)

- **Experiencia:** +15 años desarrollando con Java/J2EE, Spring, JSF; microservicios en banca; frontend con Angular/Ionic; uso de **AWS** desde 2023; herramientas: Git, Jira, Sonar, Bamboo, etc. (fuente: *Profile.pdf*).  
- **Entornos y bases de datos:** Oracle, PostgreSQL, MySQL; servidores Tomcat/WebSphere/JBoss; testing con JUnit/Mockito. (fuente: *Profile.pdf*).  
- **Contexto del proyecto de referencia (Pluto):** dominio TV/OTT en Alemania, **migración monolito→microservicios** dirigidos por eventos con **Spring Boot, Kafka, PostgreSQL, MongoDB**, frontend **Angular/TS**; **migración Java 11→17** y **Spring Boot 2.7→3.x**; coexistencia de **REST** y **SOAP** heredado. (fuente: *ResumenPluton.pdf*).

**Implicación para la web:** la plataforma debe reflejar este stack y prácticas reales (REST, eventos/Kafka, persistencia mixta, despliegue contenedorizado y observabilidad), incluyendo escenarios de migración y mantenimiento.

---

## 3) Mercado actual (2025) — síntesis para el stack Java

- **Versiones Java & LTS:** LTS vigentes **Java 17 y 21**; el **próximo LTS previsto es Java 25 (sept 2025)**. Estándar de facto en ofertas: 17/21.  
- **Spring Boot 3.3.x** es la línea estable y dominante en backend Java; añade mejoras en **observabilidad (Micrometer/Prometheus)** y calidad de vida en dev.  
- **Demand drivers en España/EU:** ofertas resaltan **Spring Boot + microservicios + Kafka + Docker/Kubernetes**; muchas piden **Java 17/21** y experiencia en **cloud** (AWS/GCP/Azure).  
- **Observabilidad moderna:** creciente adopción de **OpenTelemetry** y métricas vía **Micrometer/Prometheus**.  
- **Contenedores:** **Docker** continúa como herramienta estándar de desarrollo y empaquetado.

> Estos puntos se traducen en requisitos para tu evaluación: dominio de **Java 21** (incl. **Virtual Threads**), **Spring Boot 3**, **Kafka**, **SQL/NoSQL**, **Docker/K8s**, **CI/CD** y **observabilidad**.

---

## 4) Gap Analysis (brechas prioritarias)

**Fortalezas actuales**  
- Trayectoria sólida en Java/Spring, microservicios (banca), SQL/NoSQL, y experiencia con Angular/Ionic.  
- Experiencia con herramientas de calidad y CI (Sonar, Bamboo).

**Brechas críticas vs stack Pluto & mercado**  
1. **Java 21 (LTS)**: consolidar novedades (p. ej., **Virtual Threads**), *records*, *sealed classes*, *pattern matching*.  
2. **Spring Boot 3.x**: migración desde 2.x (endpoints/actuators, seguridad 6.x,



## 5) Roadmap de capacitación (12 meses)

> Cada módulo incluye **resultado evaluable** en la web y **evidencia en GitHub**. Tiempo total estimado: 6–8 h/semana.

### Trimestre 1 (Meses 1–3) · Fundamentos actualizados
- **Java 21 LTS**: *virtual threads*, *records*, *sealed*, *switch* con *pattern matching*.
- **Spring Boot 3.3**: Actuator, perfiles, configuración, *error handling*, *springdoc-openapi*.
- **Persistencia**: JPA/Hibernate + PostgreSQL; introducción a **MongoDB** (documentos, índices).
- **Testing**: JUnit 5, **Testcontainers** (Postgres/Mongo), *test pyramids* y cobertura mínima 70%.
- **Entrega**: servicio REST monolítico (**Pluto-catalog**) + OpenAPI, empaquetado con Docker; **examen teórico** + **lab CI verde**.

### Trimestre 2 (Meses 4–6) · Microservicios y eventos
- **Diseño**: hexagonal/DDD ligero, *API versioning*, *saga/outbox pattern*.
- **Kafka**: *topics/partitions*, *consumer groups*, *idempotence*, **transactions**, *DLQ*.  
- **Servicios**: `catalog`, `provisioning`, `notifications` con **REST + eventos**; Postgres (OLTP) y Mongo (perfilamiento).  
- **Seguridad**: OAuth2/OIDC con **Keycloak**.  
- **Entrega**: *compose* de 3 servicios + **Kafka**; **contract tests**; *playbook* de resiliencia. **Quiz** + **rúbrica de diseño**.

### Trimestre 3 (Meses 7–9) · Producción y cloud
- **Kubernetes**: *Deployments/Services/Ingress*, *HPA*, *probes*, **Helm**; *secrets*.  
- **Observabilidad**: **Micrometer/Prometheus** + *tracing* **OTel**; *dashboards*; SLOs.  
- **CI/CD**: pipeline GitHub Actions (build → tests → *quality gate* → *container scan* → deploy a K8s).  
- **Entrega**: *blue/green* o *rolling update*; pruebas de carga (*k6*); reporte de SLOs.

### Trimestre 4 (Meses 10–12) · Capstone “Pluto-lite” & entrevistas
- **Capstone**: flujo completo de **provisión de TV**: orden → *eventos Kafka* → **provisioning** → confirmaciones → *notificaciones*, con **exactly-once** donde aplique, compensaciones de saga y **migración de un endpoint SOAP→REST**.
- **Hardening**: seguridad, *rate-limit*, *circuit breaker*, *backpressure*.  
- **Entrevistas**: *system design*, *code katas*, *mock interviews*, *behavioral*.
- **Entrega**: Demo pública + documentación arquitectónica (ADR/RFC) y **score final** en la web.

---

## 6) Plan de implementación de la web

### 6.1 Arquitectura (MVP → escalable)
- **Frontend:** **Angular** (SPA) para alinear con el stack del proyecto.  
- **Backend:** **Spring Boot 3 (Java 21)** con módulos: *content*, *assessments*, *submissions*, *scoring*, *auth*.  
- **DB:** **PostgreSQL** (progreso, puntuaciones, rúbricas) + **MongoDB** (banco de preguntas, contenidos versionados).  
- **Infra:** Docker (dev), **Kubernetes** (kind/minikube) para producción personal; *observabilidad* con Micrometer/Prometheus; *CI/CD* con GitHub Actions.  
- **Auth:** Keycloak (OIDC).

### 6.2 Modelo de datos (resumen)
`User`, `Skill`, `Module`, `Lesson`, `QuizQuestion`, `Lab`, `Submission`, `Score`, `Badge`, `Rubric`, `Artifact` (enlaces a repos/Docker images), `KPI`.

### 6.3 Módulos funcionales
1. **Dashboard** de progreso (por *Skill* y por *Proyecto*).  
2. **Ruta de aprendizaje** (roadmap 12 meses) con hitos y prerequisitos.  
3. **Evaluaciones**:
   - **Quizzes** (auto-corrección; banco etiquetado por *skill* y dificultad).
   - **Labs evaluables**: integración con GitHub; el backend clona el repo, ejecuta **Maven + Testcontainers** y calcula el **score** (tests, cobertura, estática, performance).  
   - **Contract testing**: validar OpenAPI publicado.  
4. **Observabilidad de aprendizajes**: métricas de intentos, *flaky tests*, TTR de *builds*, SLOs personales.  
5. **Portafolio**: página pública con demos, métricas y badges (capstone + labs).

### 6.4 Secciones de contenido
- **Java 21** (concurrencia con *virtual threads*, novedades del lenguaje).  
- **Spring Boot 3** (Actuator, configuración, *Micrometer*, seguridad).  
- **Kafka** (patrones, transacciones, *schema evolution*).  
- **Persistencia** (PostgreSQL/Mongo).  
- **Docker & Kubernetes** (deploy de los microservicios).  
- **Observabilidad** (Micrometer/Prometheus/OTel).  
- **API-First & Security** (OpenAPI, OAuth2/OIDC).  
- **Migraciones** (2.x→3.x, SOAP→REST).  
- **Entrevistas & system design** (casos del dominio OTT).

### 6.5 Evaluación y scoring (por módulo)
- **Quizzes**: 20–30 preguntas; **aprobado ≥ 75%**.  
- **Labs**:  
  - **Funcionalidad** (40%): endpoints/eventos pasan tests.  
  - **Calidad** (25%): cobertura ≥ 75%, *checkstyle/spotbugs* sin *blockers*.  
  - **Resiliencia** (20%): *circuit breaker*, *retries*, *timeouts*.  
  - **Operabilidad** (15%): métricas y *health checks* expuestos; dashboard listo.
- **Capstone**: rúbrica global (arquitectura, fiabilidad, seguridad, observabilidad, rendimiento).

### 6.6 Requisitos no funcionales
- **Trazabilidad** (todo entregable versionado).  
- **Reproducibilidad** (scripts `make`/`mvnw` para CI local).  
- **Seguridad básica** (secrets, roles).  
- **Accesibilidad** AA.


## 7) Contenidos y recursos recomendados (oficiales)

- **Java 21**: Virtual Threads (Oracle docs).  
- **LTS roadmap**: Oracle (LTS 17/21 y plan LTS 25).  
- **Spring Boot 3.3**: notas de versión y blog oficial; métricas/Actuator.  
- **Kafka**: docs Apache y visión de Confluent.  
- **Observabilidad**: Micrometer + Prometheus + guía Spring; tendencias OTel.  
- **Docker**: Informe 2025 sobre uso y productividad.

> En la web, cada lección enlazará a estos recursos y tendrá **challenge** asociado.

---

## 8) Métricas de éxito (antes de ir a entrevistas)

**Progreso formativo**
- ≥ **85%** de media en quizzes; **100%** de módulos obligatorios completados.
- **3** microservicios funcionales y documentados + **capstone Pluto-lite**.
- Cobertura media **≥ 75%**; *quality gate* **verde** (sin *blockers*).
- **Dashboards** operativos (métricas + alertas básicas).

**Preparación profesional**
- **2** simulacros de *system design* aprobados (rúbrica ≥ 70%).  
- **1** demo pública desplegada en K8s con *readiness/liveness* y *Ingress*.  
- *CV* y *LinkedIn* actualizados con enlaces a repos, *readme* y dashboards.  
- Dominar explicación de **event-driven** (Kafka), **virtual threads**, **observabilidad** y **seguridad** del stack.

---

## 9) Roadmap de construcción de la web (sprints orientativos)

- **Sprint 1 (2 semanas)**: estructura Angular + API Spring Boot; modelos `Skill/Module/Quiz`.  
- **Sprint 2**: motor de quizzes + autenticación Keycloak + base de contenidos Java 21.  
- **Sprint 3**: integración GitHub → *submissions* + **runner** de labs (Maven+Testcontainers).  
- **Sprint 4**: sección Kafka (quizzes + lab productor/consumidor).  
- **Sprint 5**: módulo K8s + despliegue en minikube; observabilidad (Prometheus).  
- **Sprint 6**: sección Capstone + portafolio público + mock interviews.

---

## 10) Backlog de labs (extracto evaluable)

1) **REST + OpenAPI (Pluto-catalog)**: CRUD con Postgres, OpenAPI publicado y tests.  
2) **Kafka Básico**: productor/consumidor, *consumer groups*, DLQ.  
3) **Kafka Transaccional**: *idempotent producer*, transacciones, re-entrega segura.  
4) **Saga/Outbox**: consistencia entre Postgres y Kafka.  
5) **Security**: OAuth2/OIDC con Keycloak.  
6) **Observabilidad**: métricas HTTP/DB + traces; dashboard en Grafana.  
7) **K8s**: despliegue Helm con *Ingress* y *HPA*.  
8) **SOAP→REST**: migración de un endpoint legado (escenario Pluto).

---

## 11) Riesgos y mitigaciones

- **Complejidad de Kafka avanzado** → partir con escenarios guiados y *fixtures* reproducibles.  
- **Entorno K8s local** → usar **kind/minikube** y plantillas Helm prediseñadas.  
- **Tiempo disponible** → priorizar módulos **Kafka, Spring Boot 3, K8s, Observabilidad**.

---

## 12) Resultados esperados al mes 12

- Web operativa con **tracking** de habilidades, **quizzes**, **labs evaluables** y **portafolio**.  
- **Capstone “Pluto-lite”** funcionando y medible (SLOs).  
- Preparación sólida para entrevistas **Java/Spring/Kafka/K8s** y casos de **OTT/TV**.

---

### Anexos — Referencias clave
- Oracle Java SE Support Roadmap (LTS 17/21 y plan LTS 25).  
- Virtual Threads (Oracle Java 21 docs).  
- Spring Boot 3.3 (release notes + blog).  
- Stack Overflow Developer Survey 2025 (popularidad de herramientas/web frameworks).  
- Ofertas de España (Java 17/21 + Spring Boot + Kafka + K8s) en InfoJobs/Tecnoempleo.  
- Docker · State of App Dev 2025.  
- OpenTelemetry/Micrometer/Prometheus (tendencias y docs).


