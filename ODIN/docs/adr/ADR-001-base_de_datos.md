# ADR-001: Elección de Base de Datos

## Estado
Aceptado

## Contexto
El proyecto requiere almacenar datos estructurados (usuarios, skills, submissions, KPIs) y datos no estructurados/versionados (contenidos, quizzes, labs).

## Decisión
- Usar **PostgreSQL** para datos estructurados y relaciones complejas.
- Usar **MongoDB** para contenidos versionados y documentos flexibles.

## Consecuencias
- Se aprovechan transacciones y consistencia de PostgreSQL.
- MongoDB permite flexibilidad y evolución rápida de contenidos.
- Se requiere mantener integridad entre ambos sistemas y sincronización en migraciones.
