# 🚀 Spring Boot JWT Authentication System

A complete Spring Boot application with JWT authentication, Oracle database integration, and comprehensive CRUD operations.

## 🎯 **Project Overview**

This is a production-ready Spring Boot REST API application that provides:

- **JWT Authentication** with Spring Security
- **User Management** with role-based access control
- **Transaction Management** with advanced search capabilities
- **Oracle Database** integration with HikariCP connection pooling
- **Comprehensive API Documentation** and testing examples

## 🛠️ **Technology Stack**

| Technology | Version | Purpose |
|------------|---------|---------|
| **Spring Boot** | 3.1.2 | Main framework |
| **Spring Security** | 6.x | Authentication & Authorization |
| **JWT (JJWT)** | 0.11.5 | Token-based authentication |
| **Oracle Database** | 12c+ | Primary database |
| **Hibernate JPA** | 6.x | ORM framework |
| **HikariCP** | Latest | Connection pooling |
| **Maven** | 3.8+ | Build management |
| **Java** | 17+ | Programming language |

## 🔑 **Key Features**

### **Authentication & Security**
- ✅ JWT-based authentication
- ✅ Role-based access control (USER/ADMIN)
- ✅ BCrypt password encryption
- ✅ CORS configuration
- ✅ Stateless session management

### **User Management**
- ✅ User registration and login
- ✅ Profile management
- ✅ Search users by name
- ✅ Role assignment

### **Transaction Management**
- ✅ Complete CRUD operations
- ✅ Advanced search with query parameters
- ✅ Date range filtering
- ✅ Amount range filtering
- ✅ Merchant-based search
- ✅ Terminal-based search
- ✅ Statistics and reporting

### **Database Integration**
- ✅ Oracle Database 12c support
- ✅ Automatic schema generation
- ✅ Connection pooling with HikariCP
- ✅ Sequence-based ID generation

## 📋 **Prerequisites**

Before running this application, make sure you have:

- **Java 17+** installed
- **Maven 3.8+** installed
- **Oracle Database 12c+** running
- **Git** for version control

## ⚙️ **Configuration**

### **Database Configuration**

Update `src/main/resources/application.properties`:

```properties
# Oracle Database Configuration
spring.datasource.url=jdbc:oracle:thin:@localhost:1521:orcl12c
spring.datasource.username=HR
spring.datasource.password=123
spring.datasource.driver-class-name=oracle.jdbc.OracleDriver

# HikariCP Configuration
spring.datasource.hikari.maximum-pool-size=20
spring.datasource.hikari.minimum-idle=5
spring.datasource.hikari.idle-timeout=300000
spring.datasource.hikari.max-lifetime=1200000

# JPA/Hibernate Configuration
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.format_sql=true
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.Oracle12cDialect

# JWT Configuration
app.jwt.secret=mySecretKey12345678901234567890
app.jwt.expiration=86400000
```

## 🚀 **Getting Started**

### **1. Clone the Repository**
```bash
git clone https://github.com/ahmedsayed622/springBootWithjwtOuth.git
cd springBootWithjwtOuth
```

### **2. Build the Project**
```bash
mvn clean compile
```

### **3. Run the Application**
```bash
mvn spring-boot:run
```

The application will start on **http://localhost:8081**

### **4. Verify Installation**
```bash
curl http://localhost:8081/api/health
```

Expected response:
```json
{
  "database": "Connected",
  "application": "Spring Boot JWT CRUD API",
  "status": "UP"
}
```

## 📚 **API Documentation**

### **Authentication Endpoints**

| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/api/auth/register` | Register new user |
| POST | `/api/auth/login` | Login user |
| GET | `/api/auth/me` | Get current user info |
| POST | `/api/auth/logout` | Logout user |

### **User Management Endpoints**

| Method | Endpoint | Description | Access |
|--------|----------|-------------|---------|
| GET | `/api/users` | Get all users | USER/ADMIN |
| GET | `/api/users/{id}` | Get user by ID | USER/ADMIN |
| POST | `/api/users` | Create new user | ADMIN |
| PUT | `/api/users/{id}` | Update user | ADMIN |
| DELETE | `/api/users/{id}` | Delete user | ADMIN |
| GET | `/api/users/search?name={name}` | Search users by name | USER/ADMIN |

### **Transaction Endpoints**

| Method | Endpoint | Description | Access |
|--------|----------|-------------|---------|
| GET | `/api/transactions` | Get all transactions | USER/ADMIN |
| GET | `/api/transactions/{id}` | Get transaction by ID | USER/ADMIN |
| POST | `/api/transactions` | Create transaction | USER/ADMIN |
| PUT | `/api/transactions/{id}` | Update transaction | ADMIN |
| DELETE | `/api/transactions/{id}` | Delete transaction | ADMIN |
| GET | `/api/transactions/filter?{params}` | Advanced search | USER/ADMIN |
| GET | `/api/transactions/search?q={query}` | Global search | USER/ADMIN |

## 🔍 **Advanced Search Examples**

### **Query Parameter Search**
```bash
# Search by specific criteria
GET /api/transactions/filter?merchantName=متجر&minAmount=100&maxAmount=500

# Search by transaction ID
GET /api/transactions/filter?transId=TXN001

# Search by date range and terminal
GET /api/transactions/filter?terminalId=TERM001&minAmount=50
```

### **Global Search**
```bash
# Search across all fields
GET /api/transactions/search?q=TXN001
GET /api/transactions/search?q=متجر
GET /api/transactions/search?q=150
```

## 🧪 **Testing with PowerShell**

### **1. User Registration**
```powershell
$registerBody = '{
    "username": "testuser",
    "email": "test@example.com",
    "password": "password123",
    "fullName": "Test User"
}'

$response = Invoke-WebRequest -Uri "http://localhost:8081/api/auth/register" -Method POST -Body $registerBody -ContentType "application/json"
$token = ($response.Content | ConvertFrom-Json).user.token
```

### **2. User Login**
```powershell
$loginBody = '{
    "usernameOrEmail": "testuser",
    "password": "password123"
}'

$loginResponse = Invoke-WebRequest -Uri "http://localhost:8081/api/auth/login" -Method POST -Body $loginBody -ContentType "application/json"
$token = ($loginResponse.Content | ConvertFrom-Json).token
$headers = @{ "Authorization" = "Bearer $token" }
```

### **3. Create Transaction**
```powershell
$transactionBody = '{
    "transId": "TXN001",
    "terminalId": "TERM001",
    "merchantName": "Test Merchant",
    "sourceAmount": 150.75,
    "transactionDate": "2025-08-22T15:30:00",
    "maskPan": "****1234",
    "authorizationNumber": "AUTH123",
    "merchantAccountNumber": "ACC123456",
    "outletCode": "OUT001"
}'

$newTransaction = Invoke-WebRequest -Uri "http://localhost:8081/api/transactions" -Method POST -Body $transactionBody -ContentType "application/json" -Headers $headers
```

### **4. Search Transactions**
```powershell
# Advanced search
$searchResponse = Invoke-WebRequest -Uri "http://localhost:8081/api/transactions/filter?merchantName=Test&minAmount=100" -Method GET -Headers $headers

# Global search
$globalSearch = Invoke-WebRequest -Uri "http://localhost:8081/api/transactions/search?q=TXN001" -Method GET -Headers $headers
```

## 📊 **Database Schema**

### **Users Table (APP_USERS)**
| Column | Type | Description |
|--------|------|-------------|
| id | NUMBER | Primary key |
| username | VARCHAR2(50) | Unique username |
| email | VARCHAR2(100) | Unique email |
| password | VARCHAR2(255) | BCrypt encrypted |
| full_name | VARCHAR2(100) | Full name |
| role | VARCHAR2(20) | USER or ADMIN |
| enabled | NUMBER(1) | Account status |
| created_date | TIMESTAMP | Creation date |

### **Transactions Table (MD_TRANSACTION_CURRENT)**
| Column | Type | Description |
|--------|------|-------------|
| id | NUMBER | Primary key |
| trans_id | VARCHAR2(50) | Transaction ID |
| terminal_id | VARCHAR2(20) | Terminal ID |
| merchant_name | VARCHAR2(100) | Merchant name |
| source_amount | NUMBER(10,2) | Transaction amount |
| transaction_date | TIMESTAMP | Transaction date |
| mask_pan | VARCHAR2(20) | Masked PAN |
| authorization_number | VARCHAR2(50) | Auth number |
| merchant_account_number | VARCHAR2(50) | Merchant account |
| outlet_code | VARCHAR2(20) | Outlet code |

## 🔒 **Security Features**

- **JWT Tokens** with 24-hour expiration
- **BCrypt password hashing** with strength 12
- **Role-based access control** (USER/ADMIN)
- **CORS configuration** for cross-origin requests
- **Stateless authentication** with Spring Security
- **SQL injection protection** with JPA queries
- **Input validation** and error handling

## 📈 **Performance Optimizations**

- **HikariCP connection pooling** for database connections
- **JPA query optimization** with proper indexing
- **Lazy loading** for entity relationships
- **Caching strategies** for frequently accessed data
- **Batch processing** for bulk operations

## 🛡️ **Error Handling**

The application includes comprehensive error handling:

- **Global exception handler** for consistent error responses
- **Validation errors** with detailed messages
- **Authentication errors** with proper HTTP status codes
- **Database errors** with transaction rollback
- **Logging** with SLF4J for debugging

## 📝 **Logging Configuration**

```properties
# Logging Configuration
logging.level.root=INFO
logging.level.com.example.springbootcrudapi=DEBUG
logging.level.org.springframework.security=DEBUG
logging.level.org.hibernate.SQL=DEBUG
logging.level.org.hibernate.type.descriptor.sql.BasicBinder=TRACE
```

## 🚀 **Deployment**

### **Production Configuration**
```properties
# Production settings
spring.profiles.active=prod
spring.jpa.hibernate.ddl-auto=validate
spring.jpa.show-sql=false
logging.level.org.hibernate.SQL=WARN
```

### **Docker Support** (Coming Soon)
```dockerfile
FROM openjdk:17-jdk-slim
COPY target/springboot-crud-api-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 8081
ENTRYPOINT ["java","-jar","/app.jar"]
```

## 🤝 **Contributing**

1. Fork the repository
2. Create your feature branch (`git checkout -b feature/AmazingFeature`)
3. Commit your changes (`git commit -m 'Add some AmazingFeature'`)
4. Push to the branch (`git push origin feature/AmazingFeature`)
5. Open a Pull Request

## 📄 **License**

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## 👨‍💻 **Author**

**Ahmed Sayed**
- GitHub: [@ahmedsayed622](https://github.com/ahmedsayed622)
- Email: ahmedsayed622@example.com

## 🙏 **Acknowledgments**

- Spring Boot team for the excellent framework
- Oracle for the robust database system
- JWT.io for token standards
- The open-source community

---

## 📞 **Support**

If you have any questions or need help with the project, please:

1. Check the [Issues](https://github.com/ahmedsayed622/springBootWithjwtOuth/issues) page
2. Create a new issue with detailed description
3. Contact the author directly

**Made with ❤️ and ☕ by Ahmed Sayed**
