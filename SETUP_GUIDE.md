# 🚀 دليل التشغيل - Spring Boot CRUD API مع Oracle Database

## المتطلبات الأساسية 📋

### 1. Java Development Kit (JDK)
- **الإصدار المطلوب:** Java 17 أو أحدث
- **التحقق من التثبيت:**
```bash
java -version
javac -version
```

### 2. Maven
- **الإصدار المطلوب:** Maven 3.6+ 
- **التحقق من التثبيت:**
```bash
mvn -version
```

### 3. Oracle Database
- **الإصدار:** Oracle 12c أو أحدث
- **المتطلبات:**
  - Database running على `localhost:1521`
  - Service name: `orcl12c`
  - User: `HR` 
  - Password: `123`

### 4. VS Code Extensions (اختيارية)
```
- Extension Pack for Java
- Spring Boot Extension Pack
- Language Support for Java by Red Hat
- Maven for Java
- Debugger for Java
- Spring Boot Tools
```

## خطوات التشغيل 🔧

### الخطوة 1: تحميل المشروع
```bash
git clone [REPOSITORY_URL]
cd springboot-crud-api
```

### الخطوة 2: إعداد قاعدة البيانات
1. **تشغيل Oracle Database**
2. **الاتصال كمستخدم HR:**
```sql
sqlplus HR/123@localhost:1521/orcl12c
```

3. **تشغيل السكريبت (اختياري):**
```sql
-- السكريبت موجود في: src/main/resources/sql/oracle_setup.sql
@src/main/resources/sql/oracle_setup.sql
```

### الخطوة 3: تخصيص إعدادات قاعدة البيانات
**تعديل ملف:** `src/main/resources/application.properties`

```properties
# Oracle Database Configuration
spring.datasource.url=jdbc:oracle:thin:@localhost:1521:orcl12c
spring.datasource.username=HR
spring.datasource.password=123
spring.datasource.driver-class-name=oracle.jdbc.OracleDriver
```

**إذا كانت بيانات قاعدة البيانات مختلفة، عدل القيم التالية:**
- `localhost:1521` ➜ عنوان IP والبورت الخاص بك
- `orcl12c` ➜ اسم الخدمة (Service Name)
- `HR` ➜ اسم المستخدم
- `123` ➜ كلمة المرور

### الخطوة 4: تشغيل المشروع
```bash
# تحميل Dependencies
mvn clean install

# تشغيل التطبيق
mvn spring-boot:run
```

### الخطوة 5: التحقق من التشغيل
**عند نجاح التشغيل ستظهر الرسائل التالية:**
```
✅ Oracle Database Connection Test Successful!
📊 Database URL: jdbc:oracle:thin:@localhost:1521:orcl12c
👤 Database User: HR
🔧 Database Product: Oracle
Tomcat started on port(s): 8081 (http)
```

## اختبار API 🧪

### الطرق المتاحة:

#### 1. جلب جميع المستخدمين
```bash
# PowerShell
Invoke-WebRequest -Uri "http://localhost:8081/api/users" -Method GET

# CMD/Git Bash
curl http://localhost:8081/api/users
```

#### 2. إنشاء مستخدم جديد
```bash
# PowerShell
$body = '{"name": "أحمد محمد", "email": "ahmed@example.com", "age": 25}'
Invoke-WebRequest -Uri "http://localhost:8081/api/users" -Method POST -Body $body -ContentType "application/json"

# CMD/Git Bash  
curl -X POST http://localhost:8081/api/users \
-H "Content-Type: application/json" \
-d '{"name": "أحمد محمد", "email": "ahmed@example.com", "age": 25}'
```

#### 3. تحديث مستخدم
```bash
# PowerShell
$body = '{"name": "أحمد علي", "email": "ahmed.ali@example.com", "age": 26}'
Invoke-WebRequest -Uri "http://localhost:8081/api/users/1" -Method PUT -Body $body -ContentType "application/json"
```

#### 4. حذف مستخدم
```bash
# PowerShell
Invoke-WebRequest -Uri "http://localhost:8081/api/users/1" -Method DELETE
```

## استكشاف الأخطاء 🔍

### مشكلة الاتصال بقاعدة البيانات
**الخطأ:** `jdbcUrl is required with driverClassName`
**الحل:** تأكد من صحة إعدادات قاعدة البيانات في `application.properties`

### مشكلة البورت مشغول
**الخطأ:** `Port 8081 was already in use`
**الحل:** 
1. غير البورت في `application.properties`:
```properties
server.port=8082
```
2. أو أوقف العملية المشغلة للبورت

### مشكلة Oracle Driver
**الخطأ:** `ClassNotFoundException: oracle.jdbc.OracleDriver`
**الحل:** تأكد من وجود Oracle driver في `pom.xml`:
```xml
<dependency>
    <groupId>com.oracle.database.jdbc</groupId>
    <artifactId>ojdbc8</artifactId>
    <scope>runtime</scope>
</dependency>
```

### مشكلة اتصال Oracle
**الخطأ:** `Connection refused` أو `ORA-12514`
**الحل:**
1. تأكد من تشغيل Oracle Database
2. تحقق من صحة Service Name
3. تأكد من أن المستخدم HR له صلاحيات الاتصال

## الملفات المهمة 📁

```
src/
├── main/
│   ├── java/com/example/springbootcrudapi/
│   │   ├── SpringbootCrudApiApplication.java    # الملف الرئيسي
│   │   ├── config/
│   │   │   └── DatabaseConnectionTest.java      # اختبار الاتصال
│   │   ├── controller/
│   │   │   └── UserController.java              # REST API
│   │   ├── entity/
│   │   │   └── User.java                        # نموذج البيانات
│   │   ├── repository/
│   │   │   └── UserRepository.java              # طبقة البيانات
│   │   └── service/
│   │       └── UserService.java                 # منطق العمل
│   └── resources/
│       ├── application.properties               # إعدادات التطبيق ⚠️ مهم
│       └── sql/oracle_setup.sql                 # سكريبت قاعدة البيانات
└── pom.xml                                      # إعدادات Maven
```

## نصائح إضافية 💡

1. **استخدم IDE مناسب:** IntelliJ IDEA أو VS Code مع Java extensions
2. **راجع Logs:** إذا واجهت مشاكل، راجع console output للتفاصيل
3. **اختبر الاتصال:** تأكد من Oracle database قبل تشغيل التطبيق
4. **Port forwarding:** إذا كان Oracle على خادم آخر، تأكد من port forwarding

## التواصل 📞
إذا واجهت أي مشاكل، تواصل مع مطور المشروع مع:
- تفاصيل الخطأ كاملة
- إصدار Java وMaven 
- إعدادات Oracle Database المستخدمة
