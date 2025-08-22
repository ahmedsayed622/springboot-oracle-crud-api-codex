# 🏗️ System Architecture Documentation

This document provides a comprehensive overview of the Spring Boot JWT Authentication API architecture, design patterns, and implementation details.

## 📐 **System Overview**

### **High-Level Architecture**

```
┌─────────────────────────────────────────────────────────────────┐
│                        CLIENT LAYER                            │
│  ┌─────────────┐  ┌─────────────┐  ┌─────────────┐             │
│  │   Web App   │  │  Mobile App │  │  Postman    │             │
│  │             │  │             │  │             │             │
│  └─────────────┘  └─────────────┘  └─────────────┘             │
└─────────────────────────────────────────────────────────────────┘
                              │
                              │ HTTP/HTTPS + JWT
                              ▼
┌─────────────────────────────────────────────────────────────────┐
│                     APPLICATION LAYER                          │
│                                                                 │
│  ┌─────────────────────────────────────────────────────────┐   │
│  │              Spring Boot Application                    │   │
│  │                                                         │   │
│  │  ┌─────────────┐  ┌─────────────┐  ┌─────────────┐     │   │
│  │  │ Controller  │  │   Service   │  │ Repository  │     │   │
│  │  │    Layer    │  │    Layer    │  │    Layer    │     │   │
│  │  └─────────────┘  └─────────────┘  └─────────────┘     │   │
│  │                                                         │   │
│  │  ┌─────────────────────────────────────────────────┐   │   │
│  │  │            Security Layer                       │   │   │
│  │  │  ┌─────────────┐  ┌─────────────┐              │   │   │
│  │  │  │ JWT Filter  │  │   Spring    │              │   │   │
│  │  │  │             │  │  Security   │              │   │   │
│  │  │  └─────────────┘  └─────────────┘              │   │   │
│  │  └─────────────────────────────────────────────────┘   │   │
│  └─────────────────────────────────────────────────────────┘   │
└─────────────────────────────────────────────────────────────────┘
                              │
                              │ JDBC + HikariCP
                              ▼
┌─────────────────────────────────────────────────────────────────┐
│                     DATA LAYER                                 │
│                                                                 │
│  ┌─────────────────────────────────────────────────────────┐   │
│  │                Oracle Database 12c                     │   │
│  │                                                         │   │
│  │  ┌─────────────┐  ┌─────────────┐  ┌─────────────┐     │   │
│  │  │    USERS    │  │ APP_USERS   │  │MD_TRANS...  │     │   │
│  │  │    Table    │  │    Table    │  │   Table     │     │   │
│  │  └─────────────┘  └─────────────┘  └─────────────┘     │   │
│  └─────────────────────────────────────────────────────────┘   │
└─────────────────────────────────────────────────────────────────┘
```

## 🏛️ **Architectural Patterns**

### **1. Layered Architecture (N-Tier)**

The application follows a **4-layer architecture**:

```
┌─────────────────────────────────────────┐
│           Presentation Layer            │  ← Controllers, DTOs
├─────────────────────────────────────────┤
│             Business Layer              │  ← Services, Business Logic
├─────────────────────────────────────────┤
│           Persistence Layer             │  ← Repositories, Entities
├─────────────────────────────────────────┤
│             Database Layer              │  ← Oracle Database
└─────────────────────────────────────────┘
```

### **2. Repository Pattern**

```java
// Interface
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
}

// Implementation (Auto-generated by Spring Data JPA)
// - Provides CRUD operations
// - Query method generation
// - Transaction management
```

### **3. Service Layer Pattern**

```java
@Service
@Transactional
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public User createUser(User user) {
        // Business logic here
        return userRepository.save(user);
    }
}
```

### **4. Dependency Injection**

```java
@RestController
public class UserController {

    @Autowired  // Constructor injection preferred
    private UserService userService;

    // Controller methods...
}
```

## 🔒 **Security Architecture**

### **JWT Authentication Flow**

```
┌─────────────┐    ┌─────────────┐    ┌─────────────┐    ┌─────────────┐
│   Client    │    │ AuthFilter  │    │   Service   │    │  Database   │
└─────────────┘    └─────────────┘    └─────────────┘    └─────────────┘
       │                   │                   │                   │
       │ 1. Login Request  │                   │                   │
       ├──────────────────▶│                   │                   │
       │                   │ 2. Validate       │                   │
       │                   ├──────────────────▶│                   │
       │                   │                   │ 3. Query User     │
       │                   │                   ├──────────────────▶│
       │                   │                   │ 4. User Data      │
       │                   │                   ◀──────────────────┤
       │                   │ 5. Generate JWT   │                   │
       │                   ◀──────────────────┤                   │
       │ 6. JWT Token      │                   │                   │
       ◀──────────────────┤                   │                   │
       │                   │                   │                   │
       │ 7. API Request    │                   │                   │
       │ + Bearer Token    │                   │                   │
       ├──────────────────▶│                   │                   │
       │                   │ 8. Validate JWT   │                   │
       │                   │ 9. Extract User   │                   │
       │                   │ 10. Set Context   │                   │
       │                   ├──────────────────▶│                   │
       │                   │                   │ 11. Query Data    │
       │                   │                   ├──────────────────▶│
       │                   │                   │ 12. Result        │
       │                   │                   ◀──────────────────┤
       │                   │ 13. Response      │                   │
       │                   ◀──────────────────┤                   │
       │ 14. Data Response │                   │                   │
       ◀──────────────────┤                   │                   │
```

### **Security Filter Chain**

```java
// SecurityConfig.java
@Bean
public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    return http
        .cors()
        .csrf().disable()
        .authorizeHttpRequests(authz -> authz
            .requestMatchers("/api/auth/**").permitAll()
            .requestMatchers("/api/users/**").hasAnyRole("USER", "ADMIN")
            .anyRequest().authenticated()
        )
        .sessionManagement(session ->
            session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        )
        .addFilterBefore(jwtAuthenticationFilter,
            UsernamePasswordAuthenticationFilter.class)
        .build();
}
```

## 📊 **Data Architecture**

### **Entity Relationship Diagram**

```
┌─────────────────────────────┐
│          APP_USERS          │
├─────────────────────────────┤
│ + ID (PK)                   │
│ + USERNAME (UNIQUE)         │
│ + EMAIL (UNIQUE)            │
│ + PASSWORD                  │
│ + FULL_NAME                 │
│ + ROLE                      │
│ + ENABLED                   │
│ + ACCOUNT_NON_EXPIRED       │
│ + ACCOUNT_NON_LOCKED        │
│ + CREDENTIALS_NON_EXPIRED   │
└─────────────────────────────┘

┌─────────────────────────────┐
│           USERS             │
├─────────────────────────────┤
│ + ID (PK)                   │
│ + NAME                      │
│ + EMAIL (UNIQUE)            │
│ + AGE                       │
└─────────────────────────────┘

┌─────────────────────────────┐
│   MD_TRANSACTION_CURRENT    │
├─────────────────────────────┤
│ + ID (PK)                   │
│ + TRANS_ID                  │
│ + TERMINAL_ID               │
│ + MERCHANT_NAME             │
│ + SOURCE_AMOUNT             │
│ + MERCHANT_COMMISSION       │
│ + TRANSACTION_DATE          │
│ + PROCESSING_DATE           │
│ + MASK_PAN                  │
│ + AUTHORIZATION_NUMBER      │
│ + MERCHANT_ACCOUNT_NUMBER   │
│ + OUTLET_CODE               │
└─────────────────────────────┘
```

### **Database Connection Architecture**

```
┌─────────────────────────────────────────────────────────────────┐
│                    Connection Pool (HikariCP)                   │
│                                                                 │
│  ┌─────────┐ ┌─────────┐ ┌─────────┐ ┌─────────┐ ┌─────────┐   │
│  │ Conn 1  │ │ Conn 2  │ │ Conn 3  │ │ Conn 4  │ │ Conn 5  │   │
│  └─────────┘ └─────────┘ └─────────┘ └─────────┘ └─────────┘   │
└─────────────────────────────────────────────────────────────────┘
                              │
                              ▼
┌─────────────────────────────────────────────────────────────────┐
│                      Oracle Database                           │
│                                                                 │
│     ┌─────────────┐  ┌─────────────┐  ┌─────────────┐         │
│     │  Schema HR  │  │ Tablespace  │  │   Indexes   │         │
│     └─────────────┘  └─────────────┘  └─────────────┘         │
└─────────────────────────────────────────────────────────────────┘
```

## 🔄 **Request Processing Flow**

### **1. Authentication Request Flow**

```
HTTP Request → CORS Filter → JWT Filter → Security Context → Controller → Service → Repository → Database
     ↓              ↓            ↓             ↓             ↓           ↓           ↓
  Validate    → Allow Origin → Validate JWT → Set User → Business → Query → Execute SQL
   Headers                      Token        Context     Logic     Method
     ↓              ↓            ↓             ↓             ↓           ↓           ↓
HTTP Response ← JSON Response ← JWT Token ← User Data ← Processed ← Result ← Database
                                                        Data         Set      Result
```

### **2. Protected Endpoint Flow**

```java
// 1. Client Request
GET /api/users
Authorization: Bearer eyJhbGciOiJIUzI1NiJ9...

// 2. JWT Filter Processing
JwtAuthenticationFilter.doFilterInternal()
  ├── Extract token from Authorization header
  ├── Validate token signature and expiration
  ├── Extract username from token claims
  ├── Load UserDetails from database
  ├── Create Authentication object
  └── Set SecurityContext

// 3. Controller Processing
@GetMapping
public ResponseEntity<List<User>> getAllUsers() {
    // SecurityContext.getAuthentication() available here
    List<User> users = userService.getAllUsers();
    return ResponseEntity.ok(users);
}

// 4. Service Layer
@Transactional(readOnly = true)
public List<User> getAllUsers() {
    return userRepository.findAll();
}

// 5. Repository Layer
// Spring Data JPA generates implementation
// Executes: SELECT * FROM USERS

// 6. Response
HTTP 200 OK
[{"id": 1, "name": "Ahmed", "email": "ahmed@example.com", "age": 25}]
```

## 🏗️ **Component Architecture**

### **1. Controller Layer**

```java
@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = "*")
public class UserController {

    // Handles HTTP requests
    // Validates input
    // Calls service layer
    // Returns HTTP responses
}
```

**Responsibilities:**

- HTTP request/response handling
- Input validation
- Error handling
- Response formatting

### **2. Service Layer**

```java
@Service
@Transactional
public class UserService {

    // Business logic
    // Transaction management
    // Data validation
    // Service orchestration
}
```

**Responsibilities:**

- Business logic implementation
- Transaction management
- Data validation
- Service coordination

### **3. Repository Layer**

```java
@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    // Data access methods
    // Query methods
    // CRUD operations
}
```

**Responsibilities:**

- Data access abstraction
- Query execution
- Entity management
- Database transactions

### **4. Security Layer**

```java
// JWT Authentication Filter
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    // Token validation
    // User authentication
    // Security context setup
}

// Security Configuration
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    // Security rules
    // Authentication providers
    // Filter chain configuration
}
```

## 🔧 **Configuration Architecture**

### **Application Properties Structure**

```properties
# Server Configuration
server.port=8081

# Database Configuration
spring.datasource.url=jdbc:oracle:thin:@localhost:1521:orcl12c
spring.datasource.username=HR
spring.datasource.password=123
spring.datasource.driver-class-name=oracle.jdbc.OracleDriver

# JPA/Hibernate Configuration
spring.jpa.database-platform=org.hibernate.dialect.OracleDialect
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true

# Security Configuration
jwt.secret=mySecretKey123456789012345678901234567890ABCDEFGHIJKLMNOPQRSTUVWXYZ
jwt.expiration=86400000

# Logging Configuration
logging.level.com.example.springbootcrudapi=DEBUG
```

### **Bean Configuration**

```java
@Configuration
public class AppConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(
            AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }
}
```

## 📈 **Performance Architecture**

### **Connection Pooling**

```yaml
HikariCP Configuration:
├── Maximum Pool Size: 10
├── Connection Timeout: 30 seconds
├── Idle Timeout: 600 seconds
├── Max Lifetime: 1800 seconds
└── Leak Detection Threshold: 60 seconds
```

### **JPA Optimizations**

```properties
# Batch Processing
spring.jpa.properties.hibernate.jdbc.batch_size=25
spring.jpa.properties.hibernate.order_inserts=true
spring.jpa.properties.hibernate.order_updates=true

# Query Optimizations
spring.jpa.properties.hibernate.use_sql_comments=true
spring.jpa.properties.hibernate.format_sql=true
```

## 🚦 **Error Handling Architecture**

### **Exception Hierarchy**

```
RuntimeException
├── AuthenticationException
├── UsernameNotFoundException
├── BadCredentialsException
└── JwtException
    ├── ExpiredJwtException
    ├── UnsupportedJwtException
    └── MalformedJwtException
```

### **Global Exception Handler**

```java
@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<ErrorResponse> handleAuthenticationException(
            AuthenticationException ex) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(new ErrorResponse("Authentication failed", ex.getMessage()));
    }
}
```

## 🔄 **Deployment Architecture**

### **Development Environment**

```
Developer Machine
├── IDE (VS Code/IntelliJ)
├── Oracle Database 12c
├── Maven 3.8+
└── Java 17
```

### **Production Environment**

```
Production Server
├── Application Server (Tomcat Embedded)
├── Oracle Database Cluster
├── Load Balancer (Optional)
└── Monitoring Tools
```

### **Container Architecture (Optional)**

```dockerfile
# Dockerfile
FROM openjdk:17-jdk-slim
COPY target/springboot-crud-api-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 8081
ENTRYPOINT ["java", "-jar", "/app.jar"]
```

## 📊 **Monitoring & Observability**

### **Logging Architecture**

```
Application Logs
├── Access Logs (HTTP requests)
├── Security Logs (Authentication events)
├── Business Logs (Service operations)
└── Error Logs (Exceptions and errors)
```

### **Health Checks**

```java
@RestController
public class HealthController {

    @GetMapping("/api/health")
    public Map<String, String> health() {
        // Database connectivity check
        // Application status
        // Memory usage
        return healthStatus;
    }
}
```

## 🔮 **Future Architecture Considerations**

### **Scalability Enhancements**

1. **Microservices Architecture**

   - Split into User Service & Transaction Service
   - API Gateway implementation
   - Service discovery

2. **Caching Layer**

   - Redis for session management
   - Database query caching
   - CDN for static content

3. **Message Queues**
   - RabbitMQ/Apache Kafka
   - Asynchronous processing
   - Event-driven architecture

### **Security Enhancements**

1. **OAuth 2.0 / OpenID Connect**
2. **API Rate Limiting**
3. **Request Signing**
4. **Database Encryption**

---

This architecture provides a solid foundation for a scalable, secure, and maintainable Spring Boot application with modern best practices.
