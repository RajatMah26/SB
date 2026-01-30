# Course Platform API

A backend service for a learning platform where users can browse courses, search content, enroll in courses, and track their learning progress.

## 🚀 Features

- **Public Course Browsing**: View all courses and detailed course content without authentication
- **Search Functionality**: Full-text search across courses, topics, subtopics, and content
- **User Authentication**: JWT-based registration and login
- **Course Enrollment**: Authenticated users can enroll in courses
- **Progress Tracking**: Mark subtopics as completed and view progress statistics

## 🛠️ Tech Stack

- **Java 17**
- **Spring Boot 3.2.2**
- **PostgreSQL** - Database
- **Spring Security** - JWT authentication
- **Spring Data JPA** - ORM
- **Swagger/OpenAPI** - API documentation
- **Maven** - Build tool

## 📋 Prerequisites

- Java 17 or higher
- PostgreSQL 15+ installed and running
- Maven (or use the included Maven wrapper)

## 🔧 Setup Instructions

### 1. Clone the Repository

```bash
git clone <repository-url>
cd course-platform-api
```

### 2. Configure PostgreSQL

Create a PostgreSQL database:

```sql
CREATE DATABASE courseplatform;
```

### 3. Configure Application Properties

For local development, update `src/main/resources/application-dev.yml` if needed:

```yaml
spring:
  datasource:
    url: jdbc:postgresql://localhost:5432/courseplatform
    username: postgres
    password: your_password
```

### 4. Build the Project

Using Maven wrapper (Windows):
```bash
mvnw.cmd clean install
```

Using Maven wrapper (Linux/Mac):
```bash
./mvnw clean install
```

Or using Maven:
```bash
mvn clean install
```

### 5. Run the Application

Using Maven wrapper (Windows):
```bash
mvnw.cmd spring-boot:run
```

Using Maven wrapper (Linux/Mac):
```bash
./mvnw spring-boot:run
```

Or using Maven:
```bash
mvn spring-boot:run
```

The application will start on `http://localhost:8080`

### 6. Access Swagger UI

Open your browser and navigate to:
```
http://localhost:8080/swagger-ui.html
```

## 📚 API Documentation

### Public Endpoints (No Authentication Required)

#### Courses
- `GET /api/courses` - List all courses
- `GET /api/courses/{id}` - Get course details

#### Search
- `GET /api/search?q={query}` - Search courses and content

#### Authentication
- `POST /api/auth/register` - Register new user
- `POST /api/auth/login` - Login and receive JWT token

### Authenticated Endpoints (JWT Required)

#### Enrollment
- `POST /api/courses/{courseId}/enroll` - Enroll in a course
- `GET /api/enrollments/{enrollmentId}/progress` - View progress

#### Progress Tracking
- `POST /api/subtopics/{subtopicId}/complete` - Mark subtopic as completed

## 🔐 Authentication Flow

1. **Register**: Create an account using `/api/auth/register`
   ```json
   {
     "email": "student@example.com",
     "password": "securePassword123"
   }
   ```

2. **Login**: Get JWT token using `/api/auth/login`
   ```json
   {
     "email": "student@example.com",
     "password": "securePassword123"
   }
   ```

3. **Use Token**: In Swagger UI, click "Authorize" and enter:
   ```
   Bearer <your-jwt-token>
   ```

## 📊 Seed Data

The application automatically loads seed data on first startup:
- **Physics 101**: Introduction to Physics (Kinematics, Dynamics, Work & Energy)
- **Math 101**: Basic Mathematics (Algebra, Functions, Geometry)

Seed data is located in `src/main/resources/seed-data/courses.json`

## 🧪 Testing with Swagger

1. Open Swagger UI at `http://localhost:8080/swagger-ui.html`
2. Test public endpoints (courses, search) without authentication
3. Register a new user
4. Login to get JWT token
5. Click "Authorize" and enter the token
6. Test authenticated endpoints (enroll, mark complete, view progress)

## 🌐 Deployment

### Environment Variables (Production)

Set the following environment variables:

- `DATABASE_URL` - PostgreSQL connection string
- `JWT_SECRET` - Secret key for JWT signing (minimum 256 bits)
- `SPRING_PROFILE` - Set to `prod`

### Deployment Platforms

Recommended platforms:
- **Railway** - Easy PostgreSQL + Spring Boot deployment
- **Render** - Supports PostgreSQL and Java applications
- **Fly.io** - Containerized deployment
- **Heroku** - Classic platform

## 📁 Project Structure

```
src/main/java/com/courseplatform/
├── config/          # Configuration classes (Security, Swagger)
├── controller/      # REST controllers
├── dto/             # Data Transfer Objects
├── entity/          # JPA entities
├── exception/       # Custom exceptions and global handler
├── repository/      # Spring Data repositories
├── security/        # JWT utilities and filters
├── service/         # Business logic
└── util/            # Utilities (Seed data loader)
```

## 🔍 Search Functionality

The search feature supports:
- **Case-insensitive** matching
- **Partial matches** (e.g., "velo" matches "velocity")
- Searches across:
  - Course titles and descriptions
  - Topic titles
  - Subtopic titles and content

Example searches:
- `velocity` - Returns Physics course
- `Newton` - Returns Physics course (Dynamics topic)
- `rate of change` - Returns Math course (Functions topic)

## ⚠️ Error Handling

The API returns standardized error responses:

```json
{
  "error": "Error Type",
  "message": "Human-readable description",
  "timestamp": "2025-12-21T10:30:00Z"
}
```

HTTP Status Codes:
- `200` - Success
- `201` - Created
- `400` - Bad Request
- `401` - Unauthorized
- `403` - Forbidden
- `404` - Not Found
- `409` - Conflict

## 📝 License

This project is created as part of a backend internship assignment.

## 👥 Contact

For questions or issues, please contact the development team.
