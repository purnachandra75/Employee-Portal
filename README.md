# Employee Attendance Portal - Spring Boot Application

## Overview
A secure, role-based employee attendance management system built with Spring Boot, Spring Security, and JWT authentication. The application supports two user categories:
- **Admin**: Can view all employees' attendance records and generate statistics
- **Employee**: Can clock in/out and view their own attendance records

---

## Key Features

### Security
- JWT-based authentication with secure token generation and validation
- Spring Security with role-based access control (RBAC)
- Password encryption using BCrypt
- Stateless session management

### Employee Features
- Clock in (Login) and clock out (Logout) functionality
- Automatic working hours calculation
- View personal attendance history
- Real-time login status check

### Admin Features
- View all employees' attendance records
- Filter records by employee ID
- View employee-wise statistics
- Dashboard with summary metrics:
  - Total attendance records
  - Completed vs. in-progress sessions
  - Average working hours

### Database
- MySQL database integration
- JPA/Hibernate ORM for data persistence
- Automatic schema initialization and updates

---

## Prerequisites

1. **Java 17+** installed
2. **Maven 3.6+** installed
3. **MySQL 5.7+** running and accessible
4. A database named `EmployeePortal` created

---

## Installation & Setup

### 1. Create the Database

```sql
CREATE DATABASE EmployeePortal;
```

### 2. Update Database Configuration

Edit `src/main/resources/application.properties`:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/EmployeePortal
spring.datasource.username=root
spring.datasource.password=your_password
```

### 3. Build and Run

```bash
cd student-attendance
mvn clean install
mvn spring-boot:run
```

The application will start on **http://localhost:8080**

---

## Default User Credentials

The application initializes with these default users:

### Admin
- **Username**: `admin`
- **Password**: `admin123`
- **Email**: `admin@company.com`

### Employees
1. **Username**: `emp1` | **Password**: `emp123` | **Name**: John Doe
2. **Username**: `emp2` | **Password**: `emp123` | **Name**: Jane Smith

---

## API Endpoints

### Authentication Endpoints (`/api/auth`)

#### Register
```http
POST /api/auth/register
Content-Type: application/json

{
  "username": "newuser",
  "password": "password123",
  "email": "user@company.com",
  "fullName": "User Name"
}
```

#### Login
```http
POST /api/auth/login
Content-Type: application/json

{
  "username": "emp1",
  "password": "emp123"
}

Response:
{
  "token": "eyJhbGciOiJIUzUxMiJ9...",
  "userId": 2,
  "username": "emp1",
  "email": "emp1@company.com",
  "fullName": "John Doe",
  "roles": [{"id": 2, "name": "ROLE_EMPLOYEE"}]
}
```

### Attendance Endpoints (`/api/attendance`)
*Requires ROLE_EMPLOYEE*

#### Clock In
```http
POST /api/attendance/login
Authorization: Bearer {token}

Response:
{
  "message": "Login recorded successfully.",
  "loginTime": "2024-05-19T10:30:00",
  "recordId": 1
}
```

#### Clock Out
```http
POST /api/attendance/logout
Authorization: Bearer {token}

Response:
{
  "message": "Logout recorded successfully.",
  "logoutTime": "2024-05-19T18:30:00",
  "workingHours": "8.00"
}
```

#### Get My Records
```http
GET /api/attendance/my-records
Authorization: Bearer {token}

Response: Array of AttendanceRecord objects
```

#### Check Current Status
```http
GET /api/attendance/current-status
Authorization: Bearer {token}

Response:
{
  "isLoggedIn": true,
  "loginTime": "2024-05-19T10:30:00",
  "recordId": 1
}
```

### Admin Endpoints (`/api/admin`)
*Requires ROLE_ADMIN*

#### Get All Records
```http
GET /api/admin/all-records
Authorization: Bearer {token}

Response: Array of all AttendanceRecord objects
```

#### Get Employee Records
```http
GET /api/admin/employee/{employeeId}/records
Authorization: Bearer {token}

Response:
{
  "employee": {
    "id": 2,
    "username": "emp1",
    "email": "emp1@company.com",
    "fullName": "John Doe"
  },
  "records": [...]
}
```

#### Get All Employees
```http
GET /api/admin/all-employees
Authorization: Bearer {token}

Response: Array of User objects
```

#### Get Statistics
```http
GET /api/admin/statistics
Authorization: Bearer {token}

Response:
{
  "totalRecords": 15,
  "completedRecords": 12,
  "inProgressRecords": 3,
  "averageWorkingHours": "8.12"
}
```

---

## Frontend Pages

### Login Page (`/login.html`)
- User registration
- User login with email/password
- Redirects to appropriate dashboard based on role

### Employee Dashboard (`/employee.html`)
- Clock in / Clock out buttons
- Current login status display
- Personal attendance history table
- Working hours calculation

### Admin Dashboard (`/admin.html`)
- **Overview Tab**: Dashboard statistics and metrics
- **Employees Tab**: List all employees, search functionality, view individual records
- **Records Tab**: View all attendance records, search by employee

---

## Architecture

### Project Structure
```
src/main/java/com/example/attendance/
├── AttendanceApplication.java          (Main Spring Boot class)
├── config/
│   ├── SecurityConfig.java             (Spring Security configuration)
│   ├── DataInitializer.java            (Default data initialization)
├── controller/
│   ├── AuthController.java             (Login/Register endpoints)
│   ├── AttendanceController.java       (Employee attendance endpoints)
│   ├── AdminController.java            (Admin endpoints)
│   └── HomeController.java             (Page redirects)
├── model/
│   ├── User.java                       (User entity)
│   ├── Role.java                       (Role entity)
│   └── AttendanceRecord.java           (Attendance record entity)
├── repository/
│   ├── UserRepository.java
│   ├── RoleRepository.java
│   └── AttendanceRepository.java
├── security/
│   ├── JwtTokenProvider.java           (JWT token generation/validation)
│   ├── JwtAuthenticationFilter.java    (JWT filter)
│   └── CustomUserDetailsService.java   (User details loading)
└── dto/
    ├── LoginRequest.java
    ├── LoginResponse.java
    └── RegisterRequest.java

src/main/resources/
├── application.properties               (Database & JWT configuration)
├── static/
    ├── login.html                      (Login/Registration page)
    ├── employee.html                   (Employee dashboard)
    ├── admin.html                      (Admin dashboard)
    └── index.html                      (Legacy employee page)
```

### Technology Stack
- **Backend**: Spring Boot 3.2.5, Spring Security, Spring Data JPA
- **Authentication**: JWT (JSON Web Tokens)
- **Database**: MySQL with Hibernate ORM
- **Frontend**: HTML5, CSS3, Vanilla JavaScript
- **Build Tool**: Maven

---

## Security Features

1. **Password Security**: Passwords are encrypted using BCrypt
2. **JWT Tokens**: Stateless authentication with JWT bearer tokens
3. **Role-Based Access Control**: Different endpoints for Admin and Employee roles
4. **CORS**: Cross-Origin Resource Sharing configured for frontend
5. **CSRF Protection**: Disabled for stateless JWT-based authentication
6. **Session Management**: Stateless (no server-side session storage)

---

## Configuration

### JWT Configuration
```properties
app.jwtSecret=mySecretKeyForJWTTokenGenerationAndValidation2024@#$%^&*()_+-=[]{}|;:',.<>?/
app.jwtExpirationMs=86400000  # 24 hours in milliseconds
```

### Database Configuration
```properties
spring.datasource.url=jdbc:mysql://localhost:3306/EmployeePortal
spring.datasource.username=root
spring.datasource.password=root
spring.jpa.hibernate.ddl-auto=update
```

---

## Testing

### Manual Testing with curl/Postman

1. **Register a new user**:
```bash
curl -X POST http://localhost:8080/api/auth/register \
  -H "Content-Type: application/json" \
  -d '{
    "username": "testuser",
    "password": "test123",
    "email": "test@company.com",
    "fullName": "Test User"
  }'
```

2. **Login**:
```bash
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "username": "testuser",
    "password": "test123"
  }'
```

3. **Clock in** (replace TOKEN with the JWT token from login):
```bash
curl -X POST http://localhost:8080/api/attendance/login \
  -H "Authorization: Bearer TOKEN"
```

4. **View my records**:
```bash
curl -X GET http://localhost:8080/api/attendance/my-records \
  -H "Authorization: Bearer TOKEN"
```

---

## Troubleshooting

### Issue: Database Connection Failed
- Ensure MySQL is running
- Check database credentials in `application.properties`
- Verify the `EmployeePortal` database exists

### Issue: JWT Token Invalid
- Ensure token is passed in Authorization header with "Bearer " prefix
- Check token expiration (24 hours by default)
- Verify `app.jwtSecret` is consistent

### Issue: Access Denied (403)
- Verify user has correct role assigned
- Check that JWT token is valid and not expired
- Ensure correct endpoint permissions for the user's role

---

## Future Enhancements

- Email verification for registration
- Forgot password functionality
- Attendance reports (PDF export)
- Real-time attendance notifications
- Geolocation-based check-in
- Biometric authentication
- Mobile application
- Advanced analytics and dashboards

---

## License

This project is licensed under the MIT License.

---

## Support

For issues or questions, contact the development team at support@company.com
