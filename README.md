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

🧱 Arquitectura del Sistema

El siguiente diagrama representa la arquitectura de alto nivel del proyecto PixelScribe, organizada bajo un modelo cliente–servidor desacoplado:

🔹 Descripción de Componentes
🖥️ Frontend – PixelScribe (React + TypeScript)

Desarrollado con React y TypeScript, este módulo proporciona la interfaz gráfica del sistema.

Se comunica con el backend a través de peticiones HTTP REST utilizando Axios o Fetch.

Implementa autenticación mediante JWT, almacenando el token en el localStorage.

Ofrece funcionalidades como:

Registro e inicio de sesión.

Subida de imágenes.

Visualización del historial de descripciones generadas por IA.

⚙️ Backend – PixelScribe (Spring Boot)

Implementado con Java 17 y Spring Boot 3, expone una API REST segura y escalable.

Administra las operaciones principales de la aplicación:

Autenticación con Spring Security + JWT.

Procesamiento y análisis de imágenes con ayuda de servicios externos de IA.

Persistencia y recuperación de datos desde MongoDB.

Herramientas y librerías clave:

Maven – gestión de dependencias.

Swagger – documentación interactiva del API.

JaCoCo – cobertura de pruebas.

SonarQube – análisis estático de código.

Docker – empaquetamiento y despliegue.

🗄️ Base de Datos – MongoDB (Atlas o Local)

Repositorio NoSQL donde se almacenan los datos de usuarios e imágenes procesadas.

Modelo flexible que facilita la gestión de documentos tipo JSON:

User: credenciales y correo electrónico.

ImageRecord: metadatos e información generada por IA.

Conexión establecida mediante el driver oficial de Spring Data MongoDB.

🔁 Flujo General de Comunicación

El usuario interactúa con el frontend para registrarse, autenticarse o subir imágenes.

El backend recibe las solicitudes, valida el token JWT y procesa la lógica correspondiente.

Las operaciones de lectura/escritura se gestionan mediante MongoDB.

El backend retorna respuestas JSON al frontend, que las representa de forma visual.

<img width="1400" height="1326" alt="Blank diagram" src="https://github.com/user-attachments/assets/95c0e84f-cd00-4762-8b0a-9560253c94ac" />

