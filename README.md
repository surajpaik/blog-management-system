# Blog Management System

A full-stack **Blog Management System** built using **Spring Boot**, **Spring Security**, **Thymeleaf**, **Hibernate**, **MySQL**, and **Bootstrap**. The application enables users to create and manage blog posts while providing administrators with tools to moderate content through a secure role-based authentication system.

This project demonstrates the implementation of a complete Spring Boot MVC application with authentication, authorization, CRUD operations, image upload, and database integration.

---

# Features

## Authentication & Authorization
- User Registration
- Secure Login & Logout
- Role-Based Access Control (Admin & Guest)
- Password Encryption using BCrypt
- Custom Login Page
- Automatic creation of default Roles and Admin user during application startup

## Admin Features
- Admin Dashboard
- Manage Categories
- View All Blog Posts
- Approve / Reject Blog Posts
- View & Delete Comments

## Guest Features
- Create Blog Posts
- Edit Own Blog Posts
- Delete Own Blog Posts
- Upload Featured Images

## Public Features
- Browse Published Blog Posts
- Search Blog Posts
- View Blog Details
- Add Comments

---

# Tech Stack

## Backend
- Java 17
- Spring Boot
- Spring Security
- Spring Data JPA (Hibernate)

## Frontend
- Thymeleaf
- HTML5
- CSS3
- Bootstrap 5
- Bootstrap Icons

## Database
- MySQL

## Build Tool
- Maven

## Tools
- IntelliJ IDEA
- Git
- GitHub
- Postman

---

# Project Structure

```
BlogManagementSystem
│
├── src
│   ├── main
│   │   ├── java
│   │   │   ├── config
│   │   │   ├── controller
│   │   │   ├── dto
│   │   │   ├── entity
│   │   │   ├── repository
│   │   │   ├── security
│   │   │   ├── service
│   │   │   └── BlogManagementSystemApplication.java
│   │   │
│   │   └── resources
│   │       ├── static
│   │       ├── templates
│   │       └── application.properties
│   │
│   └── test
│
├── pom.xml
├── README.md
└── .gitignore
```

---

# Getting Started

## Prerequisites

Before running the project, make sure you have:

- Java 17 or later
- Maven
- MySQL Server
- IntelliJ IDEA / STS (optional)

---

## Clone the Repository

```bash
git clone https://github.com/surajpaik/blog-management-system.git
```

Move to the project directory.

```bash
cd blog-management-system
```

---

# Database Configuration

Create a MySQL database.

```sql
CREATE DATABASE YOUR_DATABASE_NAME;
```

Open

```
src/main/resources/application.properties
```

Update the following properties:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/YOUR_DATABASE_NAME
spring.datasource.username=YOUR_DATABASE_USERNAME
spring.datasource.password=YOUR_DATABASE_PASSWORD
```

---

# Run the Application

Using Maven

```bash
mvn spring-boot:run
```

or simply run

```
BlogManagementSystemApplication
```

from IntelliJ IDEA or STS.

The application will be available at

```
http://localhost:8080
```

---

# Default Admin Credentials

During the first application startup, the application automatically creates the default administrator.

Email

```
admin@gmail.com
```

Password

```
admin123
```

---

# Security

- Passwords are encrypted using BCrypt.
- Authentication is handled using Spring Security.
- Role-Based Authorization using Admin and Guest roles.
- Unauthorized requests are redirected to a custom error page.

---

# Future Enhancements

- User Profile Management
- Forgot Password
- Email Verification
- Rich Text Editor
- REST API Version
- Docker Support
- Cloud Deployment
- User Activity Dashboard

---

# Learning Outcomes

This project helped me gain hands-on experience with:

- Spring Boot MVC Architecture
- Spring Security Authentication & Authorization
- Spring Data JPA & Hibernate
- Thymeleaf Templating Engine
- File Upload Handling
- Form Validation
- MySQL Database Integration
- Maven Project Management
- Git & GitHub Version Control

---

# Author

**Suraj Paik**

Java | Spring Boot Developer

GitHub

https://github.com/surajpaik

LinkedIn

https://www.linkedin.com/in/surajpaik5718