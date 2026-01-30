# Deployment Guide - Course Platform API

## Quick Start (Local Testing)

### Prerequisites Check

1. **Java 17**: Run `java -version` to verify
2. **PostgreSQL**: Ensure PostgreSQL is installed and running

### Database Setup

```sql
-- Connect to PostgreSQL
psql -U postgres

-- Create database
CREATE DATABASE courseplatform;

-- Exit psql
\q
```

### Running the Application

Since Maven is not installed globally, you have two options:

#### Option 1: Install Maven

Download and install Maven from https://maven.apache.org/download.cgi

Then run:
```bash
mvn clean install
mvn spring-boot:run
```

#### Option 2: Use an IDE

1. Open the project in IntelliJ IDEA or Eclipse
2. Let the IDE download dependencies
3. Run `CoursePlatformApplication.java`

### Verify Application is Running

1. Open browser: http://localhost:8080/swagger-ui.html
2. You should see the Swagger UI interface

## Testing Workflow

### 1. Test Public Endpoints (No Auth Required)

**List Courses:**
- Endpoint: `GET /api/courses`
- Expected: List of 2 courses (Physics 101, Math 101)

**Get Course Details:**
- Endpoint: `GET /api/courses/physics-101`
- Expected: Full course structure with topics and subtopics

**Search:**
- Endpoint: `GET /api/search?q=velocity`
- Expected: Search results from Physics course

### 2. Test Authentication

**Register:**
```json
POST /api/auth/register
{
  "email": "test@example.com",
  "password": "password123"
}
```

**Login:**
```json
POST /api/auth/login
{
  "email": "test@example.com",
  "password": "password123"
}
```
Copy the `token` from response.

**Authorize in Swagger:**
1. Click "Authorize" button
2. Enter: `Bearer <your-token>`
3. Click "Authorize"

### 3. Test Enrollment

**Enroll in Course:**
```
POST /api/courses/physics-101/enroll
```
Note the `enrollmentId` from response.

**Try Duplicate Enrollment:**
```
POST /api/courses/physics-101/enroll
```
Expected: 409 Conflict error

### 4. Test Progress Tracking

**Mark Subtopic Complete:**
```
POST /api/subtopics/velocity/complete
```

**View Progress:**
```
GET /api/enrollments/{enrollmentId}/progress
```
Replace `{enrollmentId}` with the ID from enrollment response.

## Deployment to Railway

### 1. Create Railway Account
- Go to https://railway.app
- Sign up with GitHub

### 2. Create New Project
- Click "New Project"
- Select "Provision PostgreSQL"
- Note the database credentials

### 3. Add Spring Boot Service
- Click "New Service"
- Select "GitHub Repo"
- Connect your repository

### 4. Configure Environment Variables

Add these variables in Railway:

```
SPRING_PROFILE=prod
DATABASE_URL=<provided by Railway PostgreSQL>
JWT_SECRET=your-super-secret-key-at-least-256-bits-long-change-this-in-production
```

### 5. Deploy
- Railway will automatically build and deploy
- Wait for deployment to complete
- Access Swagger UI at: `https://your-app.railway.app/swagger-ui.html`

## Deployment to Render

### 1. Create Render Account
- Go to https://render.com
- Sign up with GitHub

### 2. Create PostgreSQL Database
- Click "New +" → "PostgreSQL"
- Note the Internal Database URL

### 3. Create Web Service
- Click "New +" → "Web Service"
- Connect your GitHub repository
- Configure:
  - **Build Command**: `mvn clean install -DskipTests`
  - **Start Command**: `java -jar target/course-platform-api-1.0.0.jar`

### 4. Environment Variables

```
SPRING_PROFILE=prod
DATABASE_URL=<your Render PostgreSQL URL>
JWT_SECRET=your-super-secret-key-at-least-256-bits-long-change-this-in-production
```

### 5. Deploy
- Click "Create Web Service"
- Wait for deployment
- Access at: `https://your-app.onrender.com/swagger-ui.html`

## Troubleshooting

### Application won't start
- Check PostgreSQL is running: `pg_isready`
- Verify database exists: `psql -U postgres -l`
- Check application logs

### Database connection error
- Verify credentials in `application-dev.yml`
- Ensure PostgreSQL is accepting connections
- Check firewall settings

### Seed data not loading
- Check logs for errors
- Verify `courses.json` exists in `src/main/resources/seed-data/`
- Manually check database: `SELECT COUNT(*) FROM courses;`

### JWT errors
- Ensure JWT_SECRET is at least 256 bits (32 characters)
- Check token is included in Authorization header
- Verify token hasn't expired (24 hours)

## Production Checklist

- [ ] Change JWT_SECRET to a strong random value
- [ ] Set SPRING_PROFILE=prod
- [ ] Configure production database
- [ ] Enable HTTPS
- [ ] Set up monitoring
- [ ] Configure logging
- [ ] Test all endpoints in production
- [ ] Document deployment URL in README

## Support

For issues or questions:
1. Check application logs
2. Review Swagger UI documentation
3. Verify database connectivity
4. Check environment variables
