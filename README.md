
# Sistema de Jardinería

Sistema de gestión desarrollado en Java para administrar la información de una empresa de jardinería.

## Tecnologías
- Java 21
- MySQL 8.4
- Maven
- Docker
- Swing

## Arquitectura
El proyecto utiliza una estructura basada en:
- Domain
- Application
- Infrastructure
- Presentation

## Ejecución

1. Iniciar la base de datos con Docker Compose.
2. Configurar la conexión en `config.properties`.
3. Ejecutar:

```bash
mvn clean compile exec:java
```

## Módulos
- Clientes
- Empleados
- Productos
- Pedidos
- Pagos

Proyecto académico desarrollado como práctica de Java y bases de datos.
