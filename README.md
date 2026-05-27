# TaskFlow

## Descripción general

TaskFlow es una API REST desarrollada con Spring Boot para la gestión de usuarios, tareas y notificaciones. El sistema permite registrar usuarios, autenticarlos mediante JWT, administrar tareas asociadas a cada cuenta y generar notificaciones automáticas relacionadas con eventos del sistema. La aplicación utiliza PostgreSQL como sistema de persistencia de datos y fue desplegada en Railway para su ejecución en entorno de producción.

El proyecto fue desarrollado bajo una arquitectura por capas, siguiendo una separación clara entre controladores, servicios, repositorios, entidades, componentes de seguridad y objetos de transferencia de datos. Asimismo, incorpora pruebas unitarias y de controladores con cobertura total del código.

---

## Objetivo del proyecto

El objetivo principal de TaskFlow es ofrecer una solución backend para la administración de tareas, incorporando mecanismos de autenticación, persistencia, notificaciones internas y pruebas automatizadas. El proyecto busca aplicar buenas prácticas de desarrollo de software en un entorno académico y técnico, incluyendo:

- diseño estructurado por capas;
- seguridad con JWT;
- cifrado de contraseñas;
- manejo de base de datos relacional;
- pruebas automatizadas con cobertura completa;
- despliegue en la nube.

---

## Tecnologías utilizadas

Las principales tecnologías, herramientas y dependencias empleadas en el desarrollo del proyecto son las siguientes:

- Java 17
- Spring Boot
- Spring Web MVC
- Spring Data JPA
- Spring Security
- JSON Web Token (JWT)
- PostgreSQL
- Maven
- JUnit 5
- Mockito
- Git y GitHub
- Railway

---

## Arquitectura del sistema

El proyecto fue estructurado bajo una arquitectura por capas con el fin de mantener una separación adecuada de responsabilidades y facilitar el mantenimiento del código.

### Controladores (`controller`)
Se encargan de recibir las solicitudes HTTP, invocar la lógica correspondiente en la capa de servicio y devolver respuestas estructuradas al cliente.

### Servicios (`service`)
Contienen la lógica de negocio principal del sistema. En esta capa se implementan las validaciones, reglas funcionales y automatizaciones de eventos.

### Repositorios (`repository`)
Permiten el acceso a la base de datos mediante Spring Data JPA.

### Entidades (`entity`)
Representan las tablas del modelo relacional en PostgreSQL.

### Seguridad (`security`)
Gestiona la autenticación y autorización mediante JWT, así como el cifrado de contraseñas con BCrypt.

### DTO (`dto`)
Permiten estructurar adecuadamente las solicitudes y respuestas, evitando exponer directamente entidades internas del sistema.

### Manejo global de excepciones (`exception`)
Centraliza el tratamiento de errores con respuestas consistentes y controladas.

---

## Estructura general del proyecto

```text
src
 ├── main
 │   ├── java/com/taskflow/taskflow
 │   │   ├── controller
 │   │   ├── dto
 │   │   ├── entity
 │   │   ├── exception
 │   │   ├── repository
 │   │   ├── security
 │   │   └── service
 │   └── resources
 │       └── application.properties
 └── test
     └── java/com/taskflow/taskflow
         ├── controller
         ├── dto
         ├── entity
         ├── security
         └── service

## Funcionalidades implementadas

El sistema desarrollado incorpora tres módulos funcionales principales: gestión de usuarios, gestión de tareas y gestión de notificaciones. Cada uno de estos módulos fue diseñado para cubrir necesidades básicas de una API orientada a la administración de actividades, con autenticación segura y eventos automáticos del sistema.

### Gestión de usuarios

El módulo de usuarios permite administrar el ciclo básico de autenticación y control de cuentas dentro de la aplicación. Entre las funcionalidades implementadas se encuentran las siguientes:

- registro de nuevos usuarios;
- validación de correos duplicados;
- cifrado de contraseñas mediante BCrypt antes de su almacenamiento;
- autenticación por correo electrónico y contraseña;
- generación de token JWT en el proceso de inicio de sesión;
- eliminación controlada de usuarios;
- restricción de eliminación cuando existen tareas asociadas al usuario;
- eliminación automática de notificaciones antes de eliminar un usuario, siempre que no existan tareas vinculadas.

### Gestión de tareas

El módulo de tareas permite realizar operaciones completas sobre las actividades registradas por los usuarios. Las funcionalidades desarrolladas son las siguientes:

- creación de tareas;
- consulta de todas las tareas registradas;
- consulta de tareas por identificador;
- actualización de tareas existentes;
- eliminación de tareas;
- asociación de cada tarea a un usuario específico.

### Gestión de notificaciones

El módulo de notificaciones permite tanto el registro manual como la generación automática de mensajes del sistema. Las funcionalidades principales son:

- creación manual de notificaciones;
- consulta de todas las notificaciones registradas;
- consulta de notificaciones por usuario;
- generación automática de notificaciones al crear una cuenta;
- generación automática de notificaciones al crear una tarea;
- generación automática de notificaciones al actualizar una tarea.

---

## Seguridad implementada

La seguridad del sistema fue construida utilizando Spring Security y JSON Web Token (JWT). Con esta configuración se logró proteger las rutas sensibles de la aplicación y limitar el acceso a usuarios autenticados.

### Características principales de seguridad

- cifrado de contraseñas con BCrypt;
- autenticación basada en tokens JWT;
- filtro de validación JWT para proteger rutas restringidas;
- separación entre endpoints públicos y privados;
- protección de módulos de tareas y notificaciones.

### Endpoints públicos

Los endpoints públicos disponibles sin autenticación son los siguientes:

```http
POST /users/register
POST /users/login
