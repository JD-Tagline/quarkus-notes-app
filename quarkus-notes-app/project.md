# Quarkus Notes App - Project Guide

## Project Overview
This is a RESTful API application built with Quarkus, a Supersonic Subatomic Java Framework. The application provides functionality for managing notes with user authentication.

## Tech Stack
- **Java 17**
- **Quarkus 3.2.2.Final** - A Kubernetes-native Java framework
- **Maven** - Build and dependency management
- **RESTEasy Reactive** - REST API implementation
- **SmallRye JWT** - JWT-based authentication
- **Quarkus Cache** - Application data caching
- **Swagger UI/OpenAPI** - API documentation
- **Lombok** - Reduces boilerplate code
- **REST Client Reactive** - For external API integration
- **JUnit 5** - Testing framework

## Project Structure
```
quarkus-notes-app/
├── src/
│   ├── main/
│   │   ├── docker/ - Docker configuration files
│   │   ├── java/com/quarkus/notes/
│   │   │   ├── controller/ - REST API endpoints
│   │   │   ├── exception/ - Custom exception handlers
│   │   │   ├── model/ - Data models (User, Note, ExternalPost)
│   │   │   ├── repository/ - Data access layer
│   │   │   └── service/ - Business logic
│   │   │       └── impl/ - Service implementations
│   │   └── resources/ - Configuration files
│   │       ├── application.properties - Application configuration
│   │       └── META-INF/ - Additional resources
│   └── test/ - Test classes
├── pom.xml - Maven configuration
└── mvnw/mvnw.cmd - Maven wrapper scripts
```

## Code Organization Guidelines
1. **Package by Feature**: Code is organized by feature/domain (notes, users) rather than by layer
2. **Interface-based Design**: Services are defined by interfaces (I*Service) and implemented separately
3. **Controller-Service-Repository Pattern**: 
   - Controllers handle HTTP requests and responses
   - Services contain business logic
   - Repositories manage data access

## Running the Application

### Development Mode
```shell
./mvnw quarkus:dev
```
This enables live coding with the Quarkus Dev UI available at http://localhost:8080/q/dev/

### Packaging and Running
```shell
./mvnw package
java -jar target/quarkus-app/quarkus-run.jar
```

### Building an Uber-jar
```shell
./mvnw package -Dquarkus.package.jar.type=uber-jar
java -jar target/*-runner.jar
```

### Creating a Native Executable
```shell
./mvnw package -Dnative
# Or with container build
./mvnw package -Dnative -Dquarkus.native.container-build=true
```

## Testing
- Tests are located in `src/test/java/`
- Run tests with: `./mvnw test`
- Integration tests use the *IT.java naming convention

## API Documentation
Swagger UI is available at http://localhost:8080/q/swagger-ui/ when the application is running.

## Docker Support
Multiple Dockerfile options are available in `src/main/docker/`:
- `Dockerfile.jvm` - For JVM mode
- `Dockerfile.native` - For native mode
- `Dockerfile.native-micro` - For native micro mode

## Best Practices
1. **Use Dependency Injection**: Leverage Quarkus CDI for component management
2. **Follow RESTful Principles**: Use appropriate HTTP methods and status codes
3. **Implement Proper Error Handling**: Use exception mappers for consistent error responses
4. **Secure Endpoints**: Use JWT authentication for protected resources
5. **Cache When Appropriate**: Use Quarkus Cache for frequently accessed data
6. **Document APIs**: Use OpenAPI annotations to document endpoints
7. **Write Tests**: Ensure good test coverage for controllers and services
8. **Use Reactive Programming**: Leverage Quarkus reactive features for better performance

## External Resources
- [Quarkus Guides](https://quarkus.io/guides/)
- [Quarkus REST Client Guide](https://quarkus.io/guides/rest-client)
- [Quarkus Security JWT Guide](https://quarkus.io/guides/security-jwt)
- [Quarkus Cache Guide](https://quarkus.io/guides/cache)