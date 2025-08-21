# 📊 Transaction API Documentation

## 🎯 نظرة عامة
هذا API يوفر الوصول لبيانات الـ transactions من جدول `MD_TRANSACTION_CURRENT` في Oracle Database باستخدام Hibernate ORM.

## 🏗️ هيكل البيانات

### Transaction Model
```json
{
  "seq": 1,
  "entityId": "OUTLET001",
  "entityName": "Merchant ABC",
  "terminalId": "T12345",
  "transactionType": "SALE",
  "transactionAmount": 1500.00,
  "transactionDate": "2025-08-21T10:30:00",
  "transactionDateFormatted": "21/08/2025",
  "transactionTimeFormatted": "10:30:00 AM",
  "maskedCardNumber": "****-****-****-1234",
  "authorizationNumber": "AUTH123456",
  "merchantCommission": 15.00,
  "transactionNetAmount": 1485.00,
  "merchantAccountNumber": "ACC123456789",
  "processingDate": "2025-08-21T10:31:00",
  "processingDateFormatted": "21/08/2025 10:31:00 AM"
}
```

## 🔗 API Endpoints

### 1. جلب جميع الـ Transactions
```http
GET /api/transactions
```

**مثال:**
```bash
curl http://localhost:8081/api/transactions
```

**الاستجابة:**
```json
[
  {
    "seq": 1,
    "entityId": "OUTLET001",
    "entityName": "Merchant ABC",
    "terminalId": "T12345",
    "transactionType": "SALE",
    "transactionAmount": 1500.00,
    "maskedCardNumber": "****-****-****-1234",
    "authorizationNumber": "AUTH123456",
    "merchantCommission": 15.00,
    "transactionNetAmount": 1485.00,
    "merchantAccountNumber": "ACC123456789"
  }
]
```

---

### 2. البحث المتقدم مع Query Parameters
```http
GET /api/transactions/search
```

**المعاملات المتاحة:**
- `entityId` - كود المنفذ
- `entityName` - اسم التاجر
- `terminalId` - رقم الـ Terminal
- `transactionType` - نوع المعاملة
- `minAmount` - أقل مبلغ
- `maxAmount` - أعلى مبلغ
- `maskedCardNumber` - رقم البطاقة المقنع
- `authorizationNumber` - رقم التفويض
- `merchantAccountNumber` - رقم حساب التاجر
- `fromDate` - من تاريخ (yyyy-MM-dd HH:mm:ss)
- `toDate` - إلى تاريخ (yyyy-MM-dd HH:mm:ss)

**أمثلة:**

#### البحث بكود المنفذ:
```bash
curl "http://localhost:8081/api/transactions/search?entityId=OUTLET001"
```

#### البحث باسم التاجر:
```bash
curl "http://localhost:8081/api/transactions/search?entityName=merchant"
```

#### البحث بنطاق المبلغ:
```bash
curl "http://localhost:8081/api/transactions/search?minAmount=100&maxAmount=2000"
```

#### البحث بالتاريخ:
```bash
curl "http://localhost:8081/api/transactions/search?fromDate=2025-08-01%2000:00:00&toDate=2025-08-31%2023:59:59"
```

#### البحث المتعدد:
```bash
curl "http://localhost:8081/api/transactions/search?entityName=merchant&terminalId=T12345&minAmount=500"
```

---

### 3. البحث بكود المنفذ
```http
GET /api/transactions/entity/{entityId}
```

**مثال:**
```bash
curl http://localhost:8081/api/transactions/entity/OUTLET001
```

---

### 4. البحث برقم الـ Terminal
```http
GET /api/transactions/terminal/{terminalId}
```

**مثال:**
```bash
curl http://localhost:8081/api/transactions/terminal/T12345
```

---

### 5. البحث برقم التفويض
```http
GET /api/transactions/auth/{authNumber}
```

**مثال:**
```bash
curl http://localhost:8081/api/transactions/auth/AUTH123456
```

---

### 6. البحث المبسط
```http
GET /api/transactions/simple-search
```

**المعاملات:**
- `entity` - كود أو اسم المنفذ
- `terminal` - رقم الـ Terminal
- `amount` - مبلغ معين
- `cardNumber` - رقم البطاقة المقنع
- `authNumber` - رقم التفويض

**مثال:**
```bash
curl "http://localhost:8081/api/transactions/simple-search?entity=merchant&amount=1500"
```

---

### 7. عدد الـ Transactions
```http
GET /api/transactions/count
```

**مثال:**
```bash
curl http://localhost:8081/api/transactions/count
```

**الاستجابة:**
```json
1247
```

---

### 8. ملخص الـ Transactions
```http
GET /api/transactions/summary
```

**مثال:**
```bash
curl http://localhost:8081/api/transactions/summary
```

**الاستجابة:**
```json
{
  "totalTransactions": 1247,
  "totalAmount": 2458750.00,
  "totalCommission": 24587.50,
  "totalNetAmount": 2434162.50,
  "message": "Transaction summary calculated successfully"
}
```

## 🧪 اختبار API بـ PowerShell

### جلب جميع الـ Transactions:
```powershell
Invoke-WebRequest -Uri "http://localhost:8081/api/transactions" -Method GET
```

### البحث بمعاملات متعددة:
```powershell
$uri = "http://localhost:8081/api/transactions/search?entityName=merchant&minAmount=100&maxAmount=2000"
Invoke-WebRequest -Uri $uri -Method GET
```

### الحصول على الملخص:
```powershell
Invoke-WebRequest -Uri "http://localhost:8081/api/transactions/summary" -Method GET
```

## 📋 ملاحظات مهمة

1. **Native Query**: يستخدم الـ SQL الأصلي اللي طلبته بالضبط
2. **Date Formatting**: التواريخ مُنسقة حسب الـ Oracle functions المطلوبة
3. **Calculated Fields**: 
   - `transactionNetAmount` = `SOURCE_AMOUNT - MERCHANT_COMMISSION`
   - `transactionDateFormatted` = تاريخ مُنسق (DD/MM/YYYY)
   - `transactionTimeFormatted` = وقت مُنسق (HH:MI:SS AM)
4. **Error Handling**: جميع الـ endpoints محمية ضد الأخطاء
5. **Cross-Origin**: يدعم CORS للـ frontend applications

## 🔍 استكشاف الأخطاء

إذا واجهت مشاكل:
1. تأكد من تشغيل Oracle Database
2. تحقق من وجود جدول `MD_TRANSACTION_CURRENT`
3. تأكد من صلاحيات المستخدم HR
4. راجع logs الـ Spring Boot للتفاصيل

## 🚀 الخطوات التالية

1. اختبر الـ APIs للتأكد من عملها
2. أضف pagination إذا احتجت
3. أضف authentication إذا طُلب
4. أضف caching للتحسين
