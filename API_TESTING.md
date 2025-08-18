# اختبار Spring Boot CRUD API

## الخطوات لاختبار التطبيق:

### 1. تشغيل التطبيق

✅ **المشروع يعمل بنجاح على:** `http://localhost:8080`

### 2. الوصول لقاعدة البيانات H2

- **URL:** http://localhost:8080/h2-console
- **JDBC URL:** `jdbc:h2:mem:testdb`
- **Username:** `sa`
- **Password:** (فارغ)

### 3. اختبار API Endpoints

#### إنشاء مستخدم جديد

```bash
curl -X POST http://localhost:8080/api/users \
-H "Content-Type: application/json" \
-d '{"name": "أحمد محمد", "email": "ahmed@example.com", "age": 25}'
```

#### جلب جميع المستخدمين

```bash
curl -X GET http://localhost:8080/api/users
```

#### جلب مستخدم بالـ ID

```bash
curl -X GET http://localhost:8080/api/users/1
```

#### تحديث مستخدم

```bash
curl -X PUT http://localhost:8080/api/users/1 \
-H "Content-Type: application/json" \
-d '{"name": "أحمد علي", "email": "ahmed.ali@example.com", "age": 26}'
```

#### حذف مستخدم

```bash
curl -X DELETE http://localhost:8080/api/users/1
```

#### البحث بالإيميل

```bash
curl -X GET http://localhost:8080/api/users/email/ahmed@example.com
```

### 4. اختبار باستخدام المتصفح

يمكن اختبار GET requests مباشرة في المتصفح:

- http://localhost:8080/api/users
- http://localhost:8080/api/users/1

### 5. أمثلة JSON للاختبار

#### إنشاء مستخدمين متعددين:

```json
{"name": "سارة أحمد", "email": "sara@example.com", "age": 28}
{"name": "محمد علي", "email": "mohamed@example.com", "age": 32}
{"name": "فاطمة حسن", "email": "fatma@example.com", "age": 24}
```

### 6. إعادة تشغيل التطبيق

```bash
mvn spring-boot:run
```

أو في VS Code:

- اضغط `Ctrl+Shift+P`
- ابحث عن "Tasks: Run Task"
- اختر "Run Spring Boot Application"
