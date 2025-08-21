# 📤 رفع المشروع على GitHub - الدليل الكامل

## الطريقة الأولى: عبر GitHub Desktop (الأسهل) 🖥️

### 1. تحميل GitHub Desktop

- اذهب إلى: https://desktop.github.com/
- حمل وثبت GitHub Desktop

### 2. إنشاء Repository

1. افتح GitHub Desktop
2. اضغط على "File" → "Add Local Repository"
3. اختر مجلد المشروع: `c:\javaSpringBoot`
4. اضغط "Publish repository"
5. اختر اسم للـ repository مثل: `springboot-oracle-crud-api`
6. اختر "Public" ليتمكن صديقك من تحميله
7. اضغط "Publish repository"

---

## الطريقة الثانية: عبر موقع GitHub (توصى) 🌐

### 1. إنشاء Repository جديد

1. اذهب إلى: https://github.com
2. سجل دخول أو أنشئ حساب جديد
3. اضغط على "+" في الأعلى → "New repository"
4. **Repository name:** `springboot-oracle-crud-api`
5. **Description:** "Spring Boot CRUD API with Oracle Database Integration"
6. اجعل Repository **Public**
7. **لا تختر** "Add README file" (لأنه موجود)
8. اضغط "Create repository"

### 2. ربط المشروع بـ GitHub

**نسخ الأوامر من صفحة GitHub والصقها في PowerShell:**

```powershell
cd c:\javaSpringBoot
git remote add origin https://github.com/[YOUR_USERNAME]/springboot-oracle-crud-api.git
git branch -M main
git push -u origin main
```

**استبدل `[YOUR_USERNAME]` باسم المستخدم الخاص بك على GitHub**

---

## الطريقة الثالثة: GitHub CLI (إذا أردت تثبيته) ⚡

### 1. تثبيت GitHub CLI

```powershell
# عبر Chocolatey (إذا كان مثبت)
choco install gh

# أو حمله من: https://cli.github.com/
```

### 2. تسجيل الدخول وإنشاء Repository

```powershell
cd c:\javaSpringBoot
gh auth login
gh repo create springboot-oracle-crud-api --public --source=. --remote=origin
git push -u origin main
```

---

## بعد رفع المشروع 📋

### 1. الحصول على رابط المشروع

بعد نجاح الرفع، ستحصل على رابط مثل:

```
https://github.com/[YOUR_USERNAME]/springboot-oracle-crud-api
```

### 2. مشاركة المشروع مع صديقك

أرسل له:

1. **رابط المشروع** على GitHub
2. **ملف SETUP_GUIDE.md** (سيكون موجود في Repository)
3. **التنبيهات المهمة** (انظر أدناه)

---

## ⚠️ تنبيهات مهمة لصديقك

### 1. متطلبات Oracle Database

```
- يحتاج Oracle Database 12c أو أحدث
- إعداد User: HR مع password: 123
- Service name: orcl12c على port: 1521
```

### 2. تخصيص إعدادات قاعدة البيانات

**ملف:** `src/main/resources/application.properties`

```properties
spring.datasource.url=jdbc:oracle:thin:@[HOST]:[PORT]:[SERVICE_NAME]
spring.datasource.username=[USERNAME]
spring.datasource.password=[PASSWORD]
```

### 3. أوامر التحميل والتشغيل

```bash
# 1. تحميل المشروع
git clone https://github.com/[YOUR_USERNAME]/springboot-oracle-crud-api.git

# 2. الدخول للمجلد
cd springboot-oracle-crud-api

# 3. تشغيل المشروع
mvn spring-boot:run
```

---

## 📝 رسالة جاهزة لصديقك

```
مرحباً! 👋

أرسل لك مشروع Spring Boot CRUD API مع Oracle Database

🔗 رابط المشروع: https://github.com/[YOUR_USERNAME]/springboot-oracle-crud-api

📋 تعليمات التشغيل:
1. حمل المشروع: git clone [الرابط أعلاه]
2. تأكد من تشغيل Oracle Database على localhost:1521
3. اقرأ ملف SETUP_GUIDE.md للتفاصيل الكاملة
4. شغل: mvn spring-boot:run

⚠️ مهم: ستحتاج Oracle Database مع user HR/123

أي مشكلة تواصل معي! 😊
```

---

## ✅ التحقق من نجاح الرفع

بعد تنفيذ أي من الطرق أعلاه:

1. اذهب إلى رابط Repository على GitHub
2. تأكد من وجود جميع الملفات
3. تأكد من وجود `SETUP_GUIDE.md`
4. جرب الرابط للتحميل: "Code" → "Download ZIP"

---

🎉 **الآن المشروع جاهز للمشاركة!**
