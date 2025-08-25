# Runbook: Rollback de Migración de Base de Datos

## Objetivo
Revertir una migración SQL en PostgreSQL y MongoDB de forma segura.

## Pasos
1. Identificar el ID de la migración a revertir.
2. Realizar backup de las bases de datos.
3. Ejecutar el script de rollback correspondiente en PostgreSQL.
4. Revertir cambios en MongoDB si aplica.
5. Verificar integridad y consistencia de datos.
6. Registrar el rollback en el sistema de auditoría.

## Notas
- Nunca realizar rollback sin backup previo.
- Documentar el motivo y resultado del rollback.
