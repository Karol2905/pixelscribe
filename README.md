# 🧠 PixelScribe

**PixelScribe** es una plataforma web desarrollada como parte del desafío de hackatón **“Más/Mejor”**, cuyo objetivo es construir una aplicación que combine **inteligencia artificial** y **procesamiento de imágenes**.  

El sistema permite que los usuarios autenticados suban imágenes, las cuales son analizadas por una **API de IA multimodal** para generar descripciones automáticas.  
Toda la información se almacena de manera segura en una base de datos y se muestra luego en un **dashboard personal**.

---

## 👩‍💻 Equipo de Desarrollo

| Integrante | Rol | Responsabilidad Principal |
|-------------|-----|----------------------------|
| **Karol Estupiñan** | Backend / Fullstack | Configuración del backend con Spring Boot, integración de JWT y conexión a MongoDB |
| **Juan Leal** | Frontend Developer | Implementación de la UI con React, manejo de subida de imágenes y consumo del API |
| **Juan Contreras** | DevOps / QA | Configuración de CI/CD con GitHub Actions y despliegue en contenedores Docker |
| **Julian Castiblanco** | Integración de IA | Conexión con API de inteligencia artificial para procesar y describir imágenes |

---

## 🧾 Descripción de la Aplicación

PixelScribe ofrece un flujo simple y robusto:

1. 🔐 **Autenticación:** El usuario se registra o inicia sesión mediante correo y contraseña.
2. 🖼️ **Subida de Imagen:** Desde el dashboard puede subir una imagen local.
3. 🤖 **Procesamiento con IA:** El backend envía la imagen a una API multimodal (por ejemplo, Gemini o GPT Vision).
4. 🗃️ **Almacenamiento:** Se guarda la imagen junto con su descripción generada.
5. 💬 **Dashboard:** El usuario puede ver todas las imágenes que ha analizado y sus resultados.

📌 El enfoque fue construir una solución **funcional, limpia y segura**, priorizando buenas prácticas sobre complejidad excesiva.

---

## 🧱 Arquitectura General

El siguiente diagrama representa la arquitectura de alto nivel del proyecto PixelScribe, organizada bajo un modelo cliente–servidor desacoplado, donde el frontend, el backend y la base de datos operan de forma independiente pero conectada mediante interfaces bien definidas.

- **Descripción de Componentes**
🖥️ Frontend – PixelScribe (React + TypeScript)

Desarrollado con React y TypeScript, este módulo proporciona la interfaz gráfica del sistema.

Se comunica con el backend a través de peticiones HTTP REST utilizando Axios o Fetch API.

Implementa autenticación mediante JWT, almacenando el token en el localStorage.

Ofrece funcionalidades como:

Registro e inicio de sesión.

Subida de imágenes.

Visualización del historial de descripciones generadas por IA.

-**Backend – PixelScribe (Spring Boot)**

Implementado con Java 17 y Spring Boot 3, este módulo expone una API REST segura y escalable.

Administra las operaciones principales de la aplicación:

Autenticación con Spring Security + JWT.

Procesamiento y análisis de imágenes con ayuda de servicios externos de IA.

Persistencia y recuperación de datos desde MongoDB.

Herramientas y librerías clave:

🧩 Maven – gestión de dependencias.

📘 Swagger – documentación interactiva del API.

🧮 JaCoCo – medición de cobertura de pruebas.

🧠 SonarQube – análisis estático de calidad del código.

🐳 Docker – empaquetamiento y despliegue del servicio.

🗄️ Base de Datos – MongoDB (Atlas o Local)

Repositorio NoSQL donde se almacenan los datos de usuarios e imágenes procesadas.
Utiliza un modelo flexible que facilita la gestión de documentos tipo JSON.

Colecciones principales:

User: credenciales y correo electrónico.

ImageRecord: metadatos e información generada por IA.

La conexión se realiza mediante Spring Data MongoDB, lo que permite operaciones CRUD simples y seguras.

🔁 Flujo General de Comunicación

El usuario interactúa con el frontend para registrarse, autenticarse o subir imágenes.

El backend recibe las solicitudes, valida el token JWT y procesa la lógica correspondiente.

Las operaciones de lectura y escritura se gestionan mediante MongoDB.

El backend retorna respuestas JSON al frontend, que las presenta en la interfaz de usuario.

**Diagrama de Componentes**

A continuación se presenta el diagrama general de componentes del sistema PixelScribe:

<p align="center"> <img width="900" alt="Arquitectura General de PixelScribe" src="https://github.com/user-attachments/assets/95c0e84f-cd00-4762-8b0a-9560253c94ac" /> </p>

**Diagrama de Clases**

A continuación se presente el diagrama de clases del sistema PixelScribe:

<img width="1417" height="1063" alt="image" src="https://github.com/user-attachments/assets/3df39434-87ef-447d-93c3-ef44a84c7398" />

