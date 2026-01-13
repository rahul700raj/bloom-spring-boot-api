# Bloom Spring Boot API

Complete REST API with Admin, Authorization, Blog, Category, SubCategory, List, and User management built with Spring Boot 3.2.0 and MySQL.

## Features

- **User Management**: Complete user CRUD operations with role-based access
- **Authentication & Authorization**: JWT-based authentication with Spring Security
- **Blog Management**: Create, read, update, delete blogs with categories and subcategories
- **Category & SubCategory**: Hierarchical content organization
- **List Management**: Personal task/list management for users
- **Admin Dashboard**: Administrative operations and statistics
- **Role-Based Access Control**: ADMIN and USER roles with different permissions

## Tech Stack

- **Java 17**
- **Spring Boot 3.2.0**
- **Spring Security** with JWT
- **Spring Data JPA**
- **MySQL Database**
- **Lombok**
- **Maven**

## Database Schema

The application uses a MySQL database named `bloom` with the following tables:

- `users` - User accounts with authentication
- `categories` - Blog categories
- `sub_categories` - Blog subcategories
- `blogs` - Blog posts with author, category, and subcategory relationships
- `list_items` - Personal task/list items for users

## Setup Instructions

### Prerequisites

- Java 17 or higher
- MySQL 8.0 or higher
- Maven 3.6 or higher

### Database Configuration

1. Create a MySQL database named `bloom`:
```sql
CREATE DATABASE bloom;
```

2. Update database credentials in `src/main/resources/application.properties`:
```properties
spring.datasource.url=jdbc:mysql://localhost:3306/bloom?createDatabaseIfNotExist=true&useSSL=false&serverTimezone=UTC
spring.datasource.username=your_username
spring.datasource.password=your_password
```

### Running the Application

1. Clone the repository:
```bash
git clone https://github.com/rahul700raj/bloom-spring-boot-api.git
cd bloom-spring-boot-api
```

2. Build the project:
```bash
mvn clean install
```

3. Run the application:
```bash
mvn spring-boot:run
```

The application will start on `http://localhost:8080`

## API Endpoints

### Authentication (`/api/auth`)

- `POST /api/auth/register` - Register new user
- `POST /api/auth/login` - Login and get JWT token
- `GET /api/auth/validate` - Validate JWT token

### User Management (`/api/users`)

- `GET /api/users` - Get all users (Admin only)
- `GET /api/users/{id}` - Get user by ID
- `PUT /api/users/{id}` - Update user
- `DELETE /api/users/{id}` - Delete user (Admin only)
- `PATCH /api/users/{id}/activate` - Activate user (Admin only)
- `PATCH /api/users/{id}/deactivate` - Deactivate user (Admin only)

### Admin (`/api/admin`)

- `GET /api/admin/dashboard` - Get dashboard statistics
- `GET /api/admin/users` - Get all users
- `GET /api/admin/blogs` - Get all blogs
- `GET /api/admin/categories` - Get all categories
- `PATCH /api/admin/users/{id}/role` - Update user role
- `DELETE /api/admin/blogs/{id}` - Delete blog
- `DELETE /api/admin/categories/{id}` - Delete category

### Categories (`/api/categories`)

- `GET /api/categories` - Get all categories
- `GET /api/categories/{id}` - Get category by ID
- `GET /api/categories/slug/{slug}` - Get category by slug
- `POST /api/categories` - Create category (Admin only)
- `PUT /api/categories/{id}` - Update category (Admin only)
- `DELETE /api/categories/{id}` - Delete category (Admin only)

### SubCategories (`/api/subcategories`)

- `GET /api/subcategories` - Get all subcategories
- `GET /api/subcategories/{id}` - Get subcategory by ID
- `GET /api/subcategories/category/{categoryId}` - Get subcategories by category
- `GET /api/subcategories/slug/{slug}` - Get subcategory by slug
- `POST /api/subcategories` - Create subcategory (Admin only)
- `PUT /api/subcategories/{id}` - Update subcategory (Admin only)
- `DELETE /api/subcategories/{id}` - Delete subcategory (Admin only)

### Blogs (`/api/blogs`)

- `GET /api/blogs` - Get all blogs
- `GET /api/blogs/{id}` - Get blog by ID
- `GET /api/blogs/slug/{slug}` - Get blog by slug
- `GET /api/blogs/author/{authorId}` - Get blogs by author
- `GET /api/blogs/category/{categoryId}` - Get blogs by category
- `GET /api/blogs/status/{status}` - Get blogs by status
- `POST /api/blogs` - Create blog
- `PUT /api/blogs/{id}` - Update blog
- `DELETE /api/blogs/{id}` - Delete blog (Admin only)

### Lists (`/api/lists`)

- `GET /api/lists` - Get all list items
- `GET /api/lists/{id}` - Get list item by ID
- `GET /api/lists/user/{userId}` - Get list items by user
- `GET /api/lists/user/{userId}/completed` - Get completed items
- `GET /api/lists/user/{userId}/pending` - Get pending items
- `POST /api/lists` - Create list item
- `PUT /api/lists/{id}` - Update list item
- `PATCH /api/lists/{id}/complete` - Mark as completed
- `PATCH /api/lists/{id}/uncomplete` - Mark as pending
- `DELETE /api/lists/{id}` - Delete list item

## Authentication

The API uses JWT (JSON Web Token) for authentication. To access protected endpoints:

1. Register or login to get a JWT token
2. Include the token in the Authorization header:
```
Authorization: Bearer <your_jwt_token>
```

## Example Requests

### Register User
```bash
curl -X POST http://localhost:8080/api/auth/register \
  -H "Content-Type: application/json" \
  -d '{
    "username": "john_doe",
    "email": "john@example.com",
    "password": "password123",
    "firstName": "John",
    "lastName": "Doe",
    "role": "USER"
  }'
```

### Login
```bash
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "username": "john_doe",
    "password": "password123"
  }'
```

### Create Blog
```bash
curl -X POST http://localhost:8080/api/blogs \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer <your_token>" \
  -d '{
    "title": "My First Blog",
    "content": "This is the content of my blog",
    "excerpt": "A short excerpt",
    "status": "PUBLISHED",
    "author": {"id": 1},
    "category": {"id": 1}
  }'
```

## Security Configuration

- Public endpoints: `/api/auth/**`
- Admin-only endpoints: `/api/admin/**`
- User & Admin endpoints: Most other endpoints require authentication
- JWT token expiration: 24 hours (configurable in application.properties)

## Contributing

1. Fork the repository
2. Create your feature branch (`git checkout -b feature/amazing-feature`)
3. Commit your changes (`git commit -m 'Add some amazing feature'`)
4. Push to the branch (`git push origin feature/amazing-feature`)
5. Open a Pull Request

## License

This project is open source and available under the MIT License.

## Contact

Rahul Mishra - rm2778643@gmail.com

Project Link: https://github.com/rahul700raj/bloom-spring-boot-api