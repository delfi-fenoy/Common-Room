
# 🎬 The Common Room

**The Common Room** es una red social desarrollada en Java con Spring Boot, orientada a personas apasionadas por el cine. Los usuarios pueden compartir reseñas, puntuaciones, listas personalizadas y descubrir recomendaciones de películas a través de una comunidad interactiva.

---

## 🌟 Características Principales

- Registro e inicio de sesión con Spring Security.
- Publicación de reseñas y puntuaciones de películas.
- Creación de listas personalizadas.
- Exploración de películas usando la API de [TheMovieDB](https://www.themoviedb.org/).
- Envío de correos (notificaciones, bienvenida, etc).
- Documentación automática con Swagger/OpenAPI.

---

## 🔧 Tecnologías Utilizadas

- **Java 21**
- **Spring Boot 3.4.5**
  - Spring Web
  - Spring Data JPA
  - Spring Security
  - Spring Validation
  - Spring Mail
- **Thymeleaf**
- **MySQL 8.0+**
- **Lombok**
- **SpringDoc OpenAPI**
- **HTML + CSS**

---

## ⚙️ Dependencias en `pom.xml`

Todas las dependencias necesarias se encuentran en el archivo `pom.xml`, entre ellas:

- `spring-boot-starter-web` → para exponer controladores y crear una API REST.
- `spring-boot-starter-data-jpa` → para persistencia con base de datos.
- `mysql-connector-j` → driver JDBC para MySQL.
- `spring-boot-starter-security` → autenticación y roles.
- `spring-boot-starter-validation` → validación de datos.
- `spring-boot-starter-mail` → envío de correos desde el backend.
- `lombok` → para evitar boilerplate code en las entidades.
- `springdoc-openapi-starter-webmvc-ui` → para documentación automática de la API (Swagger).
- `spring-boot-devtools` → recarga automática en desarrollo.

---

## 🛠️ Configuración Relevante (`application.properties`)

```properties
# Conexión a base de datos
spring.datasource.url=jdbc:mysql://localhost:3306/tu_base_de_datos
spring.datasource.username=usuario
spring.datasource.password=contraseña

spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.MySQL8Dialect

# Swagger (SpringDoc)
springdoc.api-docs.path=/api-docs
springdoc.swagger-ui.path=/swagger-ui.html

# Mail
spring.mail.host=smtp.gmail.com
spring.mail.port=587
spring.mail.username=tu_correo@gmail.com
spring.mail.password=tu_contraseña_o_contraseña_app
spring.mail.properties.mail.smtp.auth=true
spring.mail.properties.mail.smtp.starttls.enable=true

# Seguridad (solo si se usa user/pass en memoria)
spring.security.user.name=admin
spring.security.user.password=admin123
spring.security.user.roles=ADMIN
```

---

## 👨‍💻 Autores

- **Ian Francano**
- **Delfina Fenoy**
- **Lola Perez**
