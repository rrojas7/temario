# Proyecto Java - Spring Boot con RxJava y WebFlux

Este proyecto es una aplicación **Java** utilizando **Spring Boot**, **RxJava**, **WebFlux**, **JUnit 5** y **Mockito** para construir una API reactiva y realizar pruebas unitarias efectivas.

---

## 🛠️ Tecnologías y Herramientas

- **Java 11+**
- **Spring Boot 2.6.x**
- **Spring WebFlux** - Programación reactiva con soporte para WebFlux.
- **RxJava 2** - Manejo reactivo de flujos de datos.
- **JUnit 5** - Framework de pruebas unitarias.
- **Mockito** - Mocking y pruebas de integración.
- **Lombok** - Reducción de boilerplate code.
- **Maven/Gradle** - Gestión de dependencias.

---

## 🚀 Configuración del Proyecto

### 1️⃣ Clonar el repositorio

```bash
git clone https://github.com/rrojas7/temario.git
cd temario
```

### 2️⃣ Ejecutar la aplicación

Con **Maven**:

```bash
mvn spring-boot:run
```

Con **Gradle**:

```bash
gradle bootRun
```

La aplicación estará disponible en: [http://localhost:8088](http://localhost:8088)

---

## 📦 Estructura del Proyecto

```
├── src
│   ├── main
│   │   ├── java/com/chapterntt/temario
│   │   │   ├── expose        # Controladores RxJava/WebFlux
│   │   │   ├── business      # Servicios con RxJava/WebFlux
│   │   │   ├── config        # Configuraciones
│   │   │   ├── model         # Modelos y DTOs
│   │   │   ├── proxy         # Consumo de apis
│   │   │   └── repository    # Repositorios
│   └── test
│       └── java/com.chapterntt.temario
│           ├── expose        # Pruebas para los controladores
│           └── business      # Pruebas para los servicios
└── pom.xml / build.gradle    # Dependencias del proyecto
```

---

## 🌐 Endpoints de la API

Utilizar la colección postman `Chapter.postman_collection.json` que se encuentra dentro de la carpeta resources.

---

## 🧪 Ejecución de Pruebas

Para correr las pruebas unitarias:

Con **Maven**:

```bash
mvn test
```

Las pruebas utilizan `JUnit 5`, `Mockito` y `WebTestClient` para verificar la funcionalidad de la API.

---

## 📑 Requisitos

- **Java 11+** instalado.
- **Maven** configurado.
- IDE recomendado: **IntelliJ IDEA**.

---

## 📝 Contribuciones

¡Las contribuciones son bienvenidas! Por favor, abre un **Pull Request** o reporta un **Issue** si encuentras algún problema.

---

## 📄 Licencia

Este proyecto está bajo la licencia MIT. Consulta el archivo [LICENSE](LICENSE) para más detalles.

---

## 🙌 Agradecimientos

Gracias a todos los que contribuyen a mantener este proyecto activo. 🚀

