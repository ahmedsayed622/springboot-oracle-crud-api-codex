# Spring Boot CRUD API Project

مشروع Spring Boot يحتوي على REST API مع عمليات CRUD باستخدام Hibernate وقاعدة بيانات H2.

## المتطلبات

- Java 17+
- Maven 3.6+
- VS Code مع الإضافات المطلوبة

## الإضافات المطلوبة لـ VS Code

```
- Extension Pack for Java
- Spring Boot Extension Pack
- Language Support for Java by Red Hat
- Maven for Java
- Debugger for Java
- Spring Boot Tools
```

## هيكل المشروع

```
src/
├── main/
│   ├── java/com/example/springbootcrudapi/
│   │   ├── SpringbootCrudApiApplication.java    # الكلاس الرئيسي
│   │   ├── controller/
│   │   │   └── UserController.java              # REST Controller
│   │   ├── entity/
│   │   │   └── User.java                        # Entity Model
│   │   ├── repository/
│   │   │   └── UserRepository.java              # Data Repository
│   │   └── service/
│   │       └── UserService.java                 # Business Logic
│   └── resources/
│       └── application.properties               # Configuration
└── test/
    └── java/com/example/springbootcrudapi/
        └── SpringbootCrudApiApplicationTests.java
```

## تشغيل المشروع

### 1. تثبيت Dependencies

```bash
mvn clean install
```

### 2. تشغيل التطبيق

```bash
mvn spring-boot:run
```

التطبيق سيعمل على: `http://localhost:8080`

### 3. الوصول لقاعدة البيانات H2

- URL: `http://localhost:8080/h2-console`
- JDBC URL: `jdbc:h2:mem:testdb`
- Username: `sa`
- Password: (فارغ)

## API Endpoints

### Users CRUD Operations

| Method | Endpoint                   | Description         |
| ------ | -------------------------- | ------------------- |
| GET    | `/api/users`               | جلب جميع المستخدمين |
| GET    | `/api/users/{id}`          | جلب مستخدم بالـ ID  |
| GET    | `/api/users/email/{email}` | جلب مستخدم بالإيميل |
| POST   | `/api/users`               | إنشاء مستخدم جديد   |
| PUT    | `/api/users/{id}`          | تحديث مستخدم        |
| DELETE | `/api/users/{id}`          | حذف مستخدم          |

### أمثلة على الطلبات

#### إنشاء مستخدم جديد

```json
POST /api/users
{
    "name": "أحمد محمد",
    "email": "ahmed@example.com",
    "age": 25
}
```

#### تحديث مستخدم

```json
PUT /api/users/1
{
    "name": "أحمد علي",
    "email": "ahmed.ali@example.com",
    "age": 26
}
```

## تكنولوجيات المستخدمة

- **Spring Boot 3.1.2** - الإطار الأساسي
- **Spring Data JPA** - للتعامل مع قاعدة البيانات
- **Hibernate** - ORM Framework
- **H2 Database** - قاعدة بيانات في الذاكرة
- **Maven** - إدارة Dependencies
- **Java 17** - لغة البرمجة

## إعدادات قاعدة البيانات

المشروع يستخدم قاعدة بيانات H2 في الذاكرة، والتي تعيد إنشاء الجداول مع كل تشغيل.

لاستخدام قاعدة بيانات أخرى مثل MySQL، قم بتعديل `application.properties`:

```properties
# MySQL Configuration
spring.datasource.url=jdbc:mysql://localhost:3306/your_database
spring.datasource.username=your_username
spring.datasource.password=your_password
spring.jpa.hibernate.ddl-auto=update
spring.jpa.database-platform=org.hibernate.dialect.MySQL8Dialect
```
