# 🎬 Common Room

---
## ✒️ Autores

- **Delfina Fenoy Rivas**
- **Ian Francano**
- **Lola Pérez**

---

**Common Room** es una red social web pensada para personas apasionadas por el cine. Desarrollada como aplicación full-stack con **Java y Spring Boot**, permite a los usuarios compartir reseñas, puntuar películas, armar listas personalizadas y descubrir recomendaciones a través de una comunidad interactiva.

---

## 🧭 Propósito del Proyecto

Este sistema busca brindar un entorno social, simple e intuitivo, donde los cinéfilos puedan:
- Opinar y descubrir películas.
- Crear sus propias colecciones.
- Interactuar con contenido generado por otros usuarios.

---

## 🌐 Funcionalidades Principales

- Registro y autenticación con manejo de roles (Visitante, Miembro y Moderador).
- Publicación, edición y baja de reseñas.
- Alta y gestión de listas personalizadas de películas.
- Exploración y visualización de fichas de películas vía integración con [TheMovieDB](https://www.themoviedb.org/).
- Interacción con otros usuarios: visualización de perfiles, likeo de reseñas, exploración de listas públicas.
- Gestión de usuarios por parte de Moderadores.
- Envío de correos (bienvenida, notificaciones).
- Documentación técnica automática de la API mediante Swagger / OpenAPI.

---

## 🔧 Tecnologías Utilizadas

### Backend
- **Java 21**
- **Spring Boot 3.4.5**
  - Spring Web (REST)
  - Spring Data JPA
  - Spring Security (JWT y roles)
  - Spring Mail
  - Spring Validation
  - DevTools
- **JJWT** (manejo de tokens)
- **MySQL** (base de datos relacional)
- **SpringDoc OpenAPI** (Swagger)
- **Lombok**

### Frontend
- **Thymeleaf**
- **HTML + CSS (custom sin frameworks)**
- **JavaScript** (interacción básica)

### API externa
- [TheMovieDB](https://www.themoviedb.org/) – para datos dinámicos de películas

---

## 📦 Dependencias en `pom.xml`

Entre las más relevantes se incluyen:

- `spring-boot-starter-web` → Exposición de endpoints REST.
- `spring-boot-starter-security` → Manejo de roles y autenticación JWT.
- `spring-boot-starter-data-jpa` → ORM con Hibernate.
- `spring-boot-starter-validation` → Validación de formularios.
- `spring-boot-starter-mail` → Envío de emails automáticos.
- `springdoc-openapi-starter-webmvc-ui` → Swagger UI para documentación.
- `jjwt-api`, `jjwt-impl`, `jjwt-jackson` → Seguridad JWT.
- `mysql-connector-j` → Conexión a base de datos.
- `lombok` → Reducción de boilerplate.

---

## ✅ Requisitos Funcionales Destacados

- RF01: Registro de nuevos usuarios.
- RF04-RF06: ABM de reseñas.
- RF15-RF17: ABM de listas personalizadas.
- RF12, RF13: Visualización de películas desde la API externa.
- RF28-RF29: Moderación de usuarios y asignación de roles.
- RF30-RF32: Exploración de perfiles de otros usuarios.

> Para ver todos los requisitos, consultar la [documentación completa en PDF](./Documentacion-CommonRoom-PDF.pdf).

---

## 🖥️ Arquitectura General

- Aplicación web con arquitectura **cliente-servidor**.
- Backend expone una **API REST** y gestiona seguridad, lógica y persistencia.
- Frontend renderizado en el servidor con Thymeleaf.
- Integración con servicios externos (TheMovieDB) y base de datos relacional.

---

## 👥 Roles y Permisos

- **Visitante:** puede explorar películas, reseñas y listas públicas.
- **Miembro:** puede crear contenido (reseñas, listas), favear, editar su perfil.
- **Moderador:** puede suspender usuarios, modificar roles y eliminar contenido inapropiado.

---

## 🏫 Contexto Académico

Este proyecto fue desarrollado en el marco de la **Tecnicatura Universitaria en Programación** en la **Universidad Tecnológica Nacional (UTN) – Facultad Regional Mar del Plata**, como trabajo final integrador de la materia Programación 2.

Fecha de entrega: **9 de junio de 2025**  
Docente: **Sofía Galbato**

---

## 📄 Licencia

Proyecto desarrollado con fines académicos. Distribución libre con fines educativos.
