# Spring Security API 📝  
API RESTful desarrollada con Spring Boot, Spring Security y JWT para el registro y autenticacion de nuevos usuarios.

## 🚀 Características  
✔️ Creacion de nuevos usuarios y metodo para autenticarse que devuelve JWT.
✔️ Utilizacion de base de datos relacional para guardar informacion de los usuarios.
✔️ Configuracion personalizada de Spring Security para funcionar con JSON Web Token.
✔️ Restriccion de acceso a endpoints en base a roles y permisos de los usuarios registrados.


## 🛠️ Tecnologías utilizadas  
- Java 17  
- Spring Boot 3
- Spring Security
- JWT
- Spring Validation
- Spring Data JPA (PostgreSQL)  

## 📌 Endpoints principales

### Endpoints de Autenticacion

| Método | Endpoint       | Descripción                 |
|--------|----------------|-----------------------------|
| GET    | `/auth/signup` | Registro de nuevo usuario   |
| GET    | `/auth/login`  | Inicio de sesion            |


### Endpoints de Control

| Método | Endpoint      | Descripción                 |
|--------|---------------|-----------------------------|
| GET    | `/endpoint/client`  | Obtiene un mensaje sobre los permisos del cliente |
| GET    | `/endpoint/employee` | Obtiene un mensaje sobre los permisos del empleado |
| PUT    | `/endpoint/admin` | Obtiene un mensaje sobre los permisos del administrador |


## 📫 Contacto  
LinkedIn: [Eric Alessandrini](https://www.linkedin.com/in/eric-alessandrini29)  
GitHub: [@EricAlessandrini](https://github.com/EricAlessandrini)  
Correo: eric.alessandrini1@gmail.com  


