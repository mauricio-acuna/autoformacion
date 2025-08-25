# ADR-002: Estrategia de Autenticación

## Estado
Aceptado

## Contexto
El sistema debe soportar autenticación enterprise, integración con sistemas externos y seguridad robusta.

## Decisión
- Usar **Spring Security 6** con OAuth2/OIDC.
- Integrar **Keycloak** como proveedor de identidad.

## Consecuencias
- Permite SSO, roles, y federación de identidades.
- Facilita escalabilidad y cumplimiento de normativas.
- Requiere configuración y mantenimiento de Keycloak.
