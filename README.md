# ForoHub - AluraCursos

Proyecto backend para un foro académico desarrollado con Spring Boot y MySQL.

## Características principales
- Autenticación con JWT
- CRUD de tópicos
- Listado público de tópicos
- Seguridad personalizada
- Migraciones con Flyway

## Requisitos
- Java 17
- MySQL 8+
- Maven

## Instalación
1. Clona el repositorio.
2. Configura la base de datos en `src/main/resources/application.properties`.
3. Ejecuta las migraciones con Flyway (automático al iniciar).
4. Compila y ejecuta:
   ```
   mvn clean install
   mvn spring-boot:run
   ```

## Endpoints principales
- `POST /login` - Autenticación y obtención de token JWT
- `GET /topicos` - Listar tópicos (público)
- `POST /topicos` - Crear tópico (requiere autenticación)
- `PUT /topicos/{id}` - Actualizar tópico (requiere autenticación)
- `DELETE /topicos/{id}` - Eliminar tópico (requiere autenticación)

## Pruebas
Puedes usar Postman para probar los endpoints. Recuerda incluir el token JWT en el header `Authorization` para las operaciones protegidas.

## Objetivo
Desarrollado para el reto AluraCursos.
