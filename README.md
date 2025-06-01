# Playlist Management API & Web Application

Este proyecto consiste en una API RESTful desarrollada con Spring Boot para la gestión de listas de reproducción de música, y una aplicación web cliente construida con Angular para interactuar con dicha API.

## Tecnologías Utilizadas

**Backend:**

*   Java 21
*   Spring Boot 3.5
    *   Spring Web
    *   Spring Data JPA
    *   Spring Security 6 (con autenticación JWT)
*   Hibernate
*   H2 Database (Base de datos en memoria)
*   Maven (Gestor de dependencias y construcción)
*   Lombok
*   MapStruct

**Frontend:**

*   Angular v19
*   TypeScript
*   HTML5, CSS3
*   Angular CLI
*   RxJS

## Características del Backend

*   **Gestión de Listas de Reproducción (CRUD):**
    *   Crear nuevas listas de reproducción.
    *   Ver todas las listas de reproducción existentes.
    *   Ver detalles de una lista de reproducción específica por nombre o ID.
    *   Eliminar listas de reproducción por nombre o ID.
*   **Gestión de Canciones:** Las canciones son parte de las listas de reproducción.
*   **Autenticación y Autorización:**
    *   Registro de nuevos usuarios.
    *   Inicio de sesión de usuarios.
    *   Protección de endpoints mediante JSON Web Tokens (JWT).
    *   Roles de usuario (ej. `ROLE_USER`, `ROLE_ADMIN`) para control de acceso.
*   **Validación de Datos.**
*   **Manejo Global de Excepciones.**
*   **Configuración CORS** para permitir la comunicación con el frontend.

## Características del Frontend

*   **Formulario de Registro de Usuarios.**
*   **Formulario de Inicio de Sesión.**
*   **Visualización de Listas de Reproducción:** Muestra las listas existentes.
*   **Creación de Nuevas Listas de Reproducción:** A través de un formulario.
*   **Visualización de Detalles de una Lista:** Muestra las canciones de una lista seleccionada.
*   **Eliminación de Listas de Reproducción.**
*   **Protección de Rutas:** Solo usuarios autenticados pueden acceder a la gestión de listas.
*   **Manejo de Tokens JWT:** Almacenamiento de token y envío en cabeceras de autorización.
*   **Interfaz de Usuario Sencilla e Intuitiva.**

## Estructura del Repositorio

El repositorio está organizado en dos carpetas principales:

*   `backend/`: Contiene el proyecto Spring Boot.
*   `frontend/`: Contiene el proyecto Angular.

## Prerrequisitos

**Backend:**

*   JDK 21 o superior.
*   Maven 3.6.x o superior.

**Frontend:**

*   Node.js (versión LTS recomendada, ej. v18.x, v20.x).
*   npm (usualmente viene con Node.js).
*   Angular CLI (última versión estable, ej. v19.x).

## Configuración y Ejecución

### Backend (Spring Boot)

1.  Navega al directorio `backend/`.
2.  Limpia y empaqueta el proyecto:
    ```bash
    mvn clean install
    ```
3.  Ejecuta la aplicación:
    ```bash
    mvn spring-boot:run
    ```
    La API estará disponible en `http://localhost:8080/quipux-test-playlist`.

### Frontend (Angular)

1.  Navega al directorio `frontend/`.
2.  Instala las dependencias (solo la primera vez o si `package.json` cambia):
    ```bash
    npm install
    ```
3.  Ejecuta el servidor de desarrollo de Angular:
    ```bash
    ng serve --open
    ```
    La aplicación se abrirá automáticamente en tu navegador en `http://localhost:4200/`.

## Endpoints Principales de la API (Backend)

*   **Autenticación:**
    *   `POST /auth/register`: Registrar un nuevo usuario.
    *   `POST /auth/login`: Iniciar sesión y obtener un token JWT.
*   **Listas de Reproducción (requieren token JWT en header `Authorization: Bearer <token>`):**
    *   `GET /lists`: Obtener todas las listas de reproducción.
    *   `POST /lists`: Crear una nueva lista de reproducción.
    *   `GET /lists/id/{id}`: Obtener una lista por su ID.
    *   `GET /lists/{listName}`: Obtener una lista por su nombre.
    *   `GET /lists/{listName}/descripcion`: Obtener solo la descripción de una lista por su nombre.
    *   `DELETE /lists/{listName}`: Eliminar una lista por su nombre.
    *   `DELETE /lists/id/{id}`: Eliminar una lista por su ID.

## Pruebas

*   **Backend:**
    *   Pruebas unitarias para la capa de servicio y controladores implementadas con JUnit 5 y Mockito.
    *   Ejecutar con: `mvn test`
