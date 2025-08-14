# 🧠 ForoHub

**ForoHub** es una API REST construida con Spring Boot que permite gestionar foros temáticos, usuarios y cursos. 

---

## 🚀 Tecnologías

- Java 17  
- Spring Boot  
- Spring Security + JWT  
- JPA + Hibernate  
- MySQL 8.4 LTS  
- Flyway (migraciones)  
- Swagger/OpenAPI  
- Insomnia (colección de pruebas)  

---

## 📦 Instalación

1. Clona el repositorio:

```bash
git clone https://github.com/dahernandeze/ForoHub.git
cd ForoHub

2. Configura tu base de datos en application.properties o application.yml.
3. Ejecuta las migraciones con Flyway (automático al iniciar).
4. Levanta el proyecto:


🧪 Flujo de prueba completo:
1. Registrar usuario:

POST /auth/register
{
  "nombre": "Ana García",
  "email": "ana@email.com", 
  "password": "password123"
}

2. Hacer login:
POST /auth/login
{
  "email": "ana@email.com",
  "password": "password123"
}
# Respuesta: { "token": "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9...", ... }

3. Usar token en requests:
GET /api/topicos
Authorization: Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9...

4. Crear tópico autenticado:
POST /api/topicos
Authorization: Bearer TU_TOKEN
{
  "titulo": "Problema con JWT",
  "mensaje": "¿Cómo configurar correctamente Spring Security?",
  "cursoId": 1
}

🔐 Autenticación
La API utiliza JWT para proteger los endpoints. Para obtener un token:
- Endpoint: POST /auth/login


📚 Documentación interactiva
Swagger está disponible en:
http://localhost:8080/swagger-ui/index.html

🧪 Colección de Insomnia
Se incluye una colección de pruebas en el archivo Insomnia_2025-08-14.yaml.
Cómo importar:
- Abre Insomnia
- Ve a File > Import
- Selecciona el archivo Insomnia_2025-08-14.yaml
Incluye login, registro, endpoints protegidos y pruebas de paginación.

👨‍💻 Autor
Diego Hernández
Backend Developer | Java & Spring Boot

📄 Licencia
Este proyecto está bajo la licencia MIT. Puedes usarlo, modificarlo y compartirlo libremente.

- 

