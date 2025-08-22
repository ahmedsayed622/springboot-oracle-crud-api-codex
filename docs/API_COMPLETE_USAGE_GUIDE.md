# 🚀 دليل استخدام Spring Boot JWT CRUD API

هذا الدليل يوضح كيفية استخدام جميع endpoints الخاصة بـ API بخطوات واضحة ومفصلة.

## 📋 **جدول المحتويات**

- [متطلبات التشغيل](#متطلبات-التشغيل)
- [تشغيل التطبيق](#تشغيل-التطبيق)
- [endpoints المصادقة](#endpoints-المصادقة)
- [endpoints المستخدمين](#endpoints-المستخدمين)
- [endpoints المعاملات](#endpoints-المعاملات)
- [أمثلة تطبيقية](#أمثلة-تطبيقية)
- [استكشاف الأخطاء](#استكشاف-الأخطاء)

---

## 🔧 **متطلبات التشغيل**

### **المتطلبات الأساسية:**

- Java 17+
- Maven 3.8+
- Oracle Database 12c+
- VS Code أو IDE أخرى

### **قاعدة البيانات:**

```sql
-- الاتصال بقاعدة البيانات Oracle
User: HR
Password: 123
URL: jdbc:oracle:thin:@localhost:1521:orcl12c
```

---

## ▶️ **تشغيل التطبيق**

### **1. تشغيل التطبيق:**

```bash
cd C:\javaSpringBoot
mvn spring-boot:run
```

### **2. التحقق من حالة التطبيق:**

```powershell
Invoke-WebRequest -Uri "http://localhost:8081/api/health" -Method GET
```

**النتيجة المتوقعة:**

```json
{
  "database": "Connected",
  "application": "Spring Boot JWT CRUD API",
  "version": "1.0.0",
  "status": "UP",
  "timestamp": 1755867513092
}
```

---

## 🔐 **endpoints المصادقة**

### **Base URL:** `http://localhost:8081/api/auth`

### **1. تسجيل مستخدم جديد**

**POST** `/register`

```powershell
$registerBody = '{
    "username": "ahmed123",
    "email": "ahmed@example.com",
    "password": "password123",
    "fullName": "أحمد محمد"
}'

$response = Invoke-WebRequest -Uri "http://localhost:8081/api/auth/register" -Method POST -Body $registerBody -ContentType "application/json"
$response.Content | ConvertFrom-Json | ConvertTo-Json -Depth 3
```

**النتيجة المتوقعة:**

```json
{
  "message": "تم تسجيل المستخدم بنجاح",
  "user": {
    "token": "eyJhbGciOiJIUzI1NiJ9...",
    "type": "Bearer",
    "id": 1,
    "username": "ahmed123",
    "email": "ahmed@example.com",
    "fullName": "أحمد محمد",
    "role": "USER"
  },
  "timestamp": 1755867548865
}
```

### **2. تسجيل الدخول**

**POST** `/login`

```powershell
$loginBody = '{
    "usernameOrEmail": "ahmed123",
    "password": "password123"
}'

$loginResponse = Invoke-WebRequest -Uri "http://localhost:8081/api/auth/login" -Method POST -Body $loginBody -ContentType "application/json"
$token = ($loginResponse.Content | ConvertFrom-Json).token
```

### **3. جلب معلومات المستخدم الحالي**

**GET** `/me`

```powershell
$headers = @{ "Authorization" = "Bearer $token" }
$meResponse = Invoke-WebRequest -Uri "http://localhost:8081/api/auth/me" -Method GET -Headers $headers
```

### **4. تسجيل الخروج**

**POST** `/logout`

```powershell
Invoke-WebRequest -Uri "http://localhost:8081/api/auth/logout" -Method POST -Headers $headers
```

---

## 👥 **endpoints المستخدمين**

### **Base URL:** `http://localhost:8081/api/users`

> **ملاحظة:** جميع endpoints المستخدمين تحتاج JWT Token في header

### **تجهيز Headers:**

```powershell
$headers = @{ "Authorization" = "Bearer $token" }
```

### **1. جلب جميع المستخدمين**

**GET** `/`

```powershell
$users = Invoke-WebRequest -Uri "http://localhost:8081/api/users" -Method GET -Headers $headers
$users.Content | ConvertFrom-Json | ConvertTo-Json -Depth 2
```

### **2. جلب مستخدم بواسطة ID**

**GET** `/{id}`

```powershell
$userId = 1
$user = Invoke-WebRequest -Uri "http://localhost:8081/api/users/$userId" -Method GET -Headers $headers
$user.Content | ConvertFrom-Json | ConvertTo-Json -Depth 2
```

### **3. البحث عن المستخدمين بالاسم**

**GET** `/search?name={name}`

```powershell
$searchName = "أحمد"
$searchResults = Invoke-WebRequest -Uri "http://localhost:8081/api/users/search?name=$searchName" -Method GET -Headers $headers
```

### **4. إنشاء مستخدم جديد (مطورين فقط)**

**POST** `/`

```powershell
$newUserBody = '{
    "username": "admin",
    "email": "admin@example.com",
    "password": "admin123",
    "fullName": "Administrator",
    "role": "ADMIN"
}'

$newUser = Invoke-WebRequest -Uri "http://localhost:8081/api/users" -Method POST -Body $newUserBody -ContentType "application/json" -Headers $headers
```

### **5. تحديث مستخدم**

**PUT** `/{id}`

```powershell
$updateUserBody = '{
    "fullName": "أحمد محمد علي",
    "email": "ahmed.new@example.com"
}'

$updatedUser = Invoke-WebRequest -Uri "http://localhost:8081/api/users/$userId" -Method PUT -Body $updateUserBody -ContentType "application/json" -Headers $headers
```

### **6. حذف مستخدم**

**DELETE** `/{id}`

```powershell
$deleteResponse = Invoke-WebRequest -Uri "http://localhost:8081/api/users/$userId" -Method DELETE -Headers $headers
```

---

## 💳 **endpoints المعاملات**

### **Base URL:** `http://localhost:8081/api/transactions`

### **1. جلب جميع المعاملات**

**GET** `/`

```powershell
$transactions = Invoke-WebRequest -Uri "http://localhost:8081/api/transactions" -Method GET -Headers $headers
$transactions.Content | ConvertFrom-Json | ConvertTo-Json -Depth 2
```

### **2. إنشاء معاملة جديدة**

**POST** `/`

```powershell
$transactionBody = '{
    "transId": "TXN001",
    "terminalId": "TERM001",
    "merchantName": "متجر الإلكترونيات",
    "sourceAmount": 150.75,
    "transactionDate": "2025-08-22T15:30:00",
    "maskPan": "****1234",
    "authorizationNumber": "AUTH123",
    "merchantAccountNumber": "ACC123456",
    "outletCode": "OUT001"
}'

$newTransaction = Invoke-WebRequest -Uri "http://localhost:8081/api/transactions" -Method POST -Body $transactionBody -ContentType "application/json" -Headers $headers
```

### **3. جلب معاملة بواسطة ID**

**GET** `/{id}`

```powershell
$transactionId = 1
$transaction = Invoke-WebRequest -Uri "http://localhost:8081/api/transactions/$transactionId" -Method GET -Headers $headers
```

### **4. البحث بواسطة رقم المعاملة**

**GET** `/trans-id/{transId}`

```powershell
$transId = "TXN001"
$transaction = Invoke-WebRequest -Uri "http://localhost:8081/api/transactions/trans-id/$transId" -Method GET -Headers $headers
```

### **5. البحث بواسطة رقم الطرفية**

**GET** `/terminal/{terminalId}`

```powershell
$terminalId = "TERM001"
$terminalTransactions = Invoke-WebRequest -Uri "http://localhost:8081/api/transactions/terminal/$terminalId" -Method GET -Headers $headers
```

### **6. البحث بواسطة اسم التاجر**

**GET** `/search/merchant?merchantName={name}`

```powershell
$merchantName = "إلكترونيات"
$merchantTransactions = Invoke-WebRequest -Uri "http://localhost:8081/api/transactions/search/merchant?merchantName=$merchantName" -Method GET -Headers $headers
```

### **7. البحث بفترة زمنية**

**GET** `/date-range?startDate={start}&endDate={end}`

```powershell
$startDate = "2025-08-22T00:00:00"
$endDate = "2025-08-22T23:59:59"
$dateRangeTransactions = Invoke-WebRequest -Uri "http://localhost:8081/api/transactions/date-range?startDate=$startDate&endDate=$endDate" -Method GET -Headers $headers
```

### **8. البحث بمبلغ أكبر من قيمة معينة**

**GET** `/amount/above/{amount}`

```powershell
$minAmount = 100
$highValueTransactions = Invoke-WebRequest -Uri "http://localhost:8081/api/transactions/amount/above/$minAmount" -Method GET -Headers $headers
```

### **9. البحث بنطاق مبلغ معين**

**GET** `/amount/range?minAmount={min}&maxAmount={max}`

```powershell
$minAmount = 50
$maxAmount = 200
$rangeTransactions = Invoke-WebRequest -Uri "http://localhost:8081/api/transactions/amount/range?minAmount=$minAmount&maxAmount=$maxAmount" -Method GET -Headers $headers
```

### **10. إحصائيات المعاملات لتاريخ معين**

**GET** `/stats/total-amount?date={date}`

```powershell
$date = "2025-08-22T12:00:00"
$stats = Invoke-WebRequest -Uri "http://localhost:8081/api/transactions/stats/total-amount?date=$date" -Method GET -Headers $headers
```

**النتيجة المتوقعة:**

```json
{
  "date": "2025-08-22T12:00:00",
  "totalAmount": 1250.75,
  "transactionCount": 8,
  "timestamp": 1755867890123
}
```

### **11. آخر المعاملات**

**GET** `/latest`

```powershell
$latestTransactions = Invoke-WebRequest -Uri "http://localhost:8081/api/transactions/latest" -Method GET -Headers $headers
```

---

## 🎯 **أمثلة تطبيقية كاملة**

### **سيناريو 1: تسجيل مستخدم جديد والعمل مع المعاملات**

```powershell
# 1. تسجيل مستخدم جديد
$registerBody = '{
    "username": "merchant1",
    "email": "merchant1@shop.com",
    "password": "secure123",
    "fullName": "تاجر المتجر الأول"
}'

$registerResponse = Invoke-WebRequest -Uri "http://localhost:8081/api/auth/register" -Method POST -Body $registerBody -ContentType "application/json"
$token = ($registerResponse.Content | ConvertFrom-Json).user.token

# 2. تجهيز headers
$headers = @{ "Authorization" = "Bearer $token" }

# 3. إنشاء معاملة
$transactionBody = '{
    "transId": "TXN_MERCHANT1_001",
    "terminalId": "TERM_SHOP_001",
    "merchantName": "متجر الإلكترونيات المتقدمة",
    "sourceAmount": 299.99,
    "merchantCommission": 8.99,
    "transactionDate": "2025-08-22T16:00:00",
    "maskPan": "****5678",
    "authorizationNumber": "AUTH789",
    "merchantAccountNumber": "ACC789456",
    "outletCode": "SHOP001"
}'

$transaction = Invoke-WebRequest -Uri "http://localhost:8081/api/transactions" -Method POST -Body $transactionBody -ContentType "application/json" -Headers $headers

# 4. جلب المعاملة المُنشأة
$transactionId = ($transaction.Content | ConvertFrom-Json).transaction.id
$retrievedTransaction = Invoke-WebRequest -Uri "http://localhost:8081/api/transactions/$transactionId" -Method GET -Headers $headers

Write-Host "تم إنشاء المعاملة بنجاح:"
$retrievedTransaction.Content | ConvertFrom-Json | ConvertTo-Json -Depth 2
```

### **سيناريو 2: البحث والتحليل**

```powershell
# البحث عن معاملات تاجر معين
$merchantName = "إلكترونيات"
$merchantTransactions = Invoke-WebRequest -Uri "http://localhost:8081/api/transactions/search/merchant?merchantName=$merchantName" -Method GET -Headers $headers

# جلب المعاملات عالية القيمة
$highValueTransactions = Invoke-WebRequest -Uri "http://localhost:8081/api/transactions/amount/above/200" -Method GET -Headers $headers

# إحصائيات اليوم
$todayStats = Invoke-WebRequest -Uri "http://localhost:8081/api/transactions/stats/total-amount?date=2025-08-22T12:00:00" -Method GET -Headers $headers

Write-Host "إحصائيات اليوم:"
$todayStats.Content | ConvertFrom-Json | ConvertTo-Json -Depth 2
```

---

## ❌ **استكشاف الأخطاء**

### **الأخطاء الشائعة والحلول:**

#### **1. خطأ 401 Unauthorized**

```json
{
  "error": "غير مصرح",
  "message": "المستخدم غير مصادق عليه"
}
```

**الحل:**

- تأكد من وجود JWT Token صحيح في header
- تأكد من أن Token لم تنته صلاحيته (24 ساعة)
- تأكد من كتابة "Bearer " قبل Token

#### **2. خطأ 400 Bad Request**

```json
{
  "error": "فشل في التسجيل",
  "message": "اسم المستخدم موجود بالفعل"
}
```

**الحل:**

- تأكد من أن اسم المستخدم والإيميل غير مستخدمين
- تحقق من صحة بيانات JSON

#### **3. خطأ 500 Internal Server Error**

```json
{
  "error": "خطأ في النظام"
}
```

**الحل:**

- تحقق من اتصال قاعدة البيانات
- راجع logs التطبيق للتفاصيل

#### **4. خطأ في قاعدة البيانات**

**الحل:**

```sql
-- التحقق من الاتصال
SELECT 1 FROM DUAL;

-- التحقق من الجداول
SELECT TABLE_NAME FROM USER_TABLES WHERE TABLE_NAME IN ('APP_USERS', 'MD_TRANSACTION_CURRENT');

-- التحقق من Sequences
SELECT SEQUENCE_NAME FROM USER_SEQUENCES WHERE SEQUENCE_NAME IN ('USER_SEQ', 'TRANSACTION_SEQ');
```

### **اختبار صحة النظام:**

```powershell
# 1. اختبار حالة التطبيق
$health = Invoke-WebRequest -Uri "http://localhost:8081/api/health" -Method GET
Write-Host "حالة التطبيق: " -NoNewline
if ($health.StatusCode -eq 200) { Write-Host "✅ يعمل" -ForegroundColor Green } else { Write-Host "❌ لا يعمل" -ForegroundColor Red }

# 2. اختبار التسجيل
try {
    $testRegister = Invoke-WebRequest -Uri "http://localhost:8081/api/auth/register" -Method POST -Body '{"username":"test123","email":"test123@test.com","password":"test123","fullName":"Test User"}' -ContentType "application/json"
    Write-Host "التسجيل: ✅ يعمل" -ForegroundColor Green
} catch {
    Write-Host "التسجيل: ❌ لا يعمل" -ForegroundColor Red
}

# 3. اختبار تسجيل الدخول
try {
    $testLogin = Invoke-WebRequest -Uri "http://localhost:8081/api/auth/login" -Method POST -Body '{"usernameOrEmail":"test123","password":"test123"}' -ContentType "application/json"
    Write-Host "تسجيل الدخول: ✅ يعمل" -ForegroundColor Green
} catch {
    Write-Host "تسجيل الدخول: ❌ لا يعمل" -ForegroundColor Red
}
```

---

## 📊 **ملاحظات هامة**

### **الأمان:**

- جميع كلمات المرور مُشفرة بـ BCrypt
- JWT Tokens تنتهي صلاحيتها خلال 24 ساعة
- CORS مُفعل للتطوير (origins = "\*")

### **قاعدة البيانات:**

- يتم إنشاء الجداول تلقائياً عند أول تشغيل
- Hibernate يقوم بتحديث schema تلقائياً
- Sequences تُنشأ تلقائياً لـ Primary Keys

### **الأداء:**

- Connection pooling مُفعل مع HikariCP
- Batch processing للاستعلامات
- SQL logging مُفعل للتطوير

---

**🎉 تم إعداد النظام بنجاح! يمكنك الآن استخدام جميع المميزات المتاحة.**
