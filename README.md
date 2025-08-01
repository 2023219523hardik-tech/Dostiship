# Dostiship Backend

A complete, secure, and scalable Spring Boot backend for the Dostiship social platform. This application enables users to connect based on shared interests, facilitates real-time communication, and provides event management capabilities.

## 🚀 Features

### 🔐 Authentication & User Management
- **JWT-based Authentication**: Secure stateless authentication using JSON Web Tokens
- **User Registration & Login**: Complete user authentication flow
- **Profile Management**: Update profiles, interests, and personal information
- **Role-based Security**: Support for user roles (USER, ADMIN)

### 🤝 Intelligent Matching Engine
- **Interest-based Matching**: Connect users with shared interests
- **Smart Recommendations**: Algorithm-driven user suggestions
- **Paginated Results**: Efficient data loading with pagination

### 💬 Real-time Messaging
- **WebSocket Support**: Real-time chat using Spring WebSocket with STOMP
- **Private Messaging**: Secure one-on-one conversations
- **Message History**: Persistent message storage and retrieval
- **Conversation Management**: Track chat participants and recent messages

### 🎉 Events & Activities
- **Event Creation**: Users can create and manage events
- **Event Discovery**: Browse upcoming events with filtering options
- **RSVP System**: Join or leave events with attendance tracking
- **Smart Recommendations**: Event suggestions based on user interests

### 👥 Team Information
- **Team Management**: Dynamic team member information
- **Public API**: Accessible team information for frontend display

### 📧 Contact System
- **Contact Form**: Handle user inquiries and feedback
- **Submission Tracking**: Store and manage contact form submissions

## 🛠 Technology Stack

- **Framework**: Spring Boot 3.2.1
- **Language**: Java 17+
- **Build Tool**: Maven
- **Database**: PostgreSQL with Spring Data JPA
- **Security**: Spring Security with JWT
- **Real-time**: Spring WebSocket with STOMP
- **Documentation**: OpenAPI 3 (Springdoc)
- **Password Encryption**: BCrypt

## 📋 Prerequisites

- Java 17 or higher
- Maven 3.6+
- PostgreSQL 12+
- IDE (IntelliJ IDEA, Eclipse, or VS Code)

## 🚀 Quick Start

### 1. Clone the Repository
```bash
git clone <repository-url>
cd dostiship-backend
```

### 2. Database Setup
Create a PostgreSQL database:
```sql
CREATE DATABASE dostiship;
CREATE USER dostiship_user WITH PASSWORD 'dostiship_password';
GRANT ALL PRIVILEGES ON DATABASE dostiship TO dostiship_user;
```

### 3. Configuration
Copy the environment variables template and configure:
```bash
# Database Configuration
DB_USERNAME=dostiship_user
DB_PASSWORD=dostiship_password

# JWT Configuration
JWT_SECRET=your-super-secret-jwt-key-here-must-be-at-least-256-bits
JWT_EXPIRATION=86400000

# Mail Configuration (Optional)
MAIL_HOST=smtp.gmail.com
MAIL_USERNAME=your-email@gmail.com
MAIL_PASSWORD=your-app-password
```

### 4. Build and Run
```bash
# Build the project
mvn clean compile

# Run the application
mvn spring-boot:run
```

The application will be available at `http://localhost:8080`

## 📚 API Documentation

Once the application is running, access the interactive API documentation:

- **Swagger UI**: http://localhost:8080/swagger-ui.html
- **OpenAPI JSON**: http://localhost:8080/v3/api-docs

## 🔗 API Endpoints

### Authentication
- `POST /api/auth/register` - Register a new user
- `POST /api/auth/login` - User login

### User Management
- `GET /api/users/me` - Get current user profile
- `PUT /api/users/me` - Update current user profile
- `GET /api/users/{id}` - Get user profile by ID

### Matching Engine
- `GET /api/matches` - Get recommended users based on interests

### Messaging
- `GET /api/chats` - Get user conversations
- `GET /api/chats/{userId}` - Get conversation with specific user
- `POST /api/chats/send` - Send a message
- WebSocket endpoint: `/ws` for real-time messaging

### Events
- `POST /api/events` - Create a new event
- `GET /api/events` - Get all upcoming events (with filtering)
- `GET /api/events/{id}` - Get event details
- `POST /api/events/{id}/rsvp` - RSVP to an event
- `DELETE /api/events/{id}/rsvp` - Cancel RSVP
- `GET /api/events/recommendations` - Get recommended events

### Team & Contact
- `GET /api/team` - Get team members information
- `POST /api/contact` - Submit contact form

## 🔌 WebSocket Usage

### Connection
Connect to WebSocket endpoint: `ws://localhost:8080/ws`

### Sending Messages
Send to destination: `/app/chat`
```json
{
  "recipientId": 123,
  "content": "Hello there!",
  "senderId": 456
}
```

### Receiving Messages
Subscribe to: `/user/queue/messages`

## 🏗 Project Structure

```
src/main/java/com/dostiship/
├── config/           # Configuration classes
├── controller/       # REST controllers
├── dto/             # Data Transfer Objects
├── exception/       # Custom exceptions and handlers
├── model/           # JPA entities
├── repository/      # Data access layer
├── security/        # Security configuration and JWT
└── service/         # Business logic layer
```

## 🔒 Security Features

- **JWT Authentication**: Stateless authentication with configurable expiration
- **Password Encryption**: BCrypt hashing for secure password storage
- **Role-based Access**: Method-level security with roles
- **CORS Configuration**: Configurable cross-origin resource sharing
- **Input Validation**: Comprehensive request validation
- **Error Handling**: Standardized error responses

## 🗄 Database Schema

The application uses JPA with Hibernate for database management. Key entities:

- **User**: User profiles with interests and authentication data
- **ChatMessage**: Private messages between users
- **Event**: Event information with attendee management
- **TeamMember**: Team information for public display
- **ContactSubmission**: Contact form submissions

## 🚀 Deployment

### Environment Variables
Configure the following environment variables for production:

```bash
# Database
DB_USERNAME=production_user
DB_PASSWORD=secure_password

# JWT
JWT_SECRET=your-production-jwt-secret-key
JWT_EXPIRATION=86400000

# Mail (Optional)
MAIL_HOST=your-smtp-host
MAIL_USERNAME=your-email
MAIL_PASSWORD=your-password
```

### Building for Production
```bash
mvn clean package -DskipTests
java -jar target/dostiship-backend-1.0.0.jar
```

## 🧪 Testing

Run the test suite:
```bash
mvn test
```

## 📈 Monitoring

The application includes Spring Boot Actuator endpoints:

- **Health Check**: `GET /actuator/health`
- **Application Info**: `GET /actuator/info`
- **Metrics**: `GET /actuator/metrics`

## 🤝 Contributing

1. Fork the repository
2. Create a feature branch (`git checkout -b feature/AmazingFeature`)
3. Commit your changes (`git commit -m 'Add some AmazingFeature'`)
4. Push to the branch (`git push origin feature/AmazingFeature`)
5. Open a Pull Request

## 📝 License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## 🆘 Support

For support, email contact@dostiship.com or create an issue in the repository.

## 🙏 Acknowledgments

- Spring Boot team for the excellent framework
- PostgreSQL community for the robust database
- JWT.io for JWT implementation guidelines
- OpenAPI for comprehensive API documentation standards