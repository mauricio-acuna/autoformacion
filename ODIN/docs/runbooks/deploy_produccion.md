# Runbook: Despliegue a Producción

## Objetivo
Realizar el despliegue seguro y controlado de la plataforma en entorno productivo.

## Pasos
1. Validar prerequisitos (Java, Docker, K8s, DBs, secrets).
2. Ejecutar migraciones de base de datos.
3. Construir imágenes Docker y subir a registry.
4. Desplegar con Helm en Kubernetes.
5. Verificar health checks y métricas.
6. Monitorear logs y alertas las primeras 24h.
7. Tener plan de rollback y backups listos.

## Notas
- Seguir checklist de calidad y seguridad antes de abrir tráfico.
- Documentar incidencias y acciones tomadas.
