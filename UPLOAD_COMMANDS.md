# 🚀 أوامر رفع المشروع على GitHub

## بعد إنشاء Repository على GitHub، استخدم هذه الأوامر:

```powershell
cd c:\javaSpringBoot

# ربط المشروع بـ GitHub Repository
git remote add origin https://github.com/ahmedsayed622/springboot-oracle-crud-api.git

# تغيير اسم Branch إلى main
git branch -M main

# رفع المشروع على GitHub
git push -u origin main
```

## إذا ظهر خطأ authentication:

### الطريقة 1: Personal Access Token (الأفضل)
1. اذهب إلى GitHub → Settings → Developer settings → Personal access tokens → Tokens (classic)
2. اضغط "Generate new token (classic)"
3. اختر scopes: `repo` و `workflow`
4. انسخ الـ token
5. استخدمه بدلاً من password عند السؤال

### الطريقة 2: GitHub Desktop
1. حمل GitHub Desktop من: https://desktop.github.com/
2. سجل دخول بحسابك
3. اضغط "Add local repository"
4. اختر مجلد: c:\javaSpringBoot
5. اضغط "Publish repository"

## للتحقق من نجاح الرفع:
1. اذهب إلى: https://github.com/ahmedsayed622/springboot-oracle-crud-api
2. يجب أن ترى جميع الملفات
3. شارك الرابط مع صديقك

## رابط المشروع النهائي:
https://github.com/ahmedsayed622/springboot-oracle-crud-api

---
💡 **ملاحظة**: استبدل `ahmedsayed622` باسم المستخدم الصحيح إذا كان مختلف
