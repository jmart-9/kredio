# Kredio

SaaS para gestión de préstamos, cuotas, moras, pagos, cajas y cobranza. Dirigido a prestamistas, financieras pequeñas y cooperativas en El Salvador.

**Tecnologías:**
- Backend: Spring Boot 3.3.0 + Java 17
- BD: PostgreSQL + JPA
- Seguridad: Spring Security + JWT
- Frontend básico: HTML (en desarrollo)

**Estado actual:** Primera versión funcional (autenticación, usuarios, pruebas iniciales).

**Cómo correr localmente**
1. Clona: `git clone https://github.com/jmart-9/kredio.git`
2. Crea BD PostgreSQL: `kredio`
3. Configura `application.yml` con tus credenciales DB
4. `mvn spring-boot:run`

**Próximos pasos**
- Multi-tenant por subdominio
- Módulo de préstamos + cuotas automáticas
- Cajas y movimientos
- Suscripciones Stripe
- Docker + Nginx wildcard
- Deploy producción

¡Feedback bienvenido!