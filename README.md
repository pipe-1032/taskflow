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
Funcionalidades implementadas
1. Gestión de usuarios
El sistema permite registrar e iniciar sesión de usuarios mediante los siguientes procesos:

registro de nuevos usuarios;
validación de correo duplicado;
cifrado de la contraseña con BCrypt antes de almacenar el usuario;
autenticación por correo y contraseña;
generación de token JWT en el proceso de inicio de sesión;
eliminación controlada de usuarios;
restricción de eliminación cuando existen tareas asociadas;
eliminación automática de notificaciones antes de eliminar el usuario, siempre que no existan tareas pendientes asociadas.

2. Gestión de tareas
El sistema permite realizar operaciones CRUD completas sobre tareas:

creación de tareas;
consulta de todas las tareas;
consulta de tarea por identificador;
actualización de tareas;
eliminación de tareas.

Cada tarea se encuentra asociada a un usuario específico.
3. Gestión de notificaciones
El sistema permite:

crear notificaciones manuales;
listar todas las notificaciones;
listar notificaciones por usuario;
generar notificaciones automáticas cuando ocurre alguno de los siguientes eventos:

creación de cuenta;
creación de tarea;
actualización de tarea.




Seguridad implementada
La seguridad del sistema se desarrolló con Spring Security y JWT.
Características principales

cifrado de contraseñas mediante BCrypt;
autenticación basada en token JWT;
uso de filtro JWT para validar solicitudes protegidas;
endpoints públicos para registro e inicio de sesión;
endpoints protegidos para tareas y notificaciones.

Endpoints públicos
Los siguientes endpoints son accesibles sin token:
