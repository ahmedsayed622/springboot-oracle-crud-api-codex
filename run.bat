@echo off
echo ========================================
echo   Spring Boot CRUD API Setup Script
echo ========================================
echo.

echo [1/4] Checking Java installation...
java -version >nul 2>&1
if %errorlevel% neq 0 (
    echo ❌ Java not found! Please install Java 17+
    echo Download from: https://www.oracle.com/java/technologies/downloads/
    pause
    exit /b 1
) else (
    echo ✅ Java found
)

echo.
echo [2/4] Checking Maven installation...
mvn -version >nul 2>&1
if %errorlevel% neq 0 (
    echo ❌ Maven not found! Please install Maven
    echo Download from: https://maven.apache.org/download.cgi
    pause
    exit /b 1
) else (
    echo ✅ Maven found
)

echo.
echo [3/4] Building project...
mvn clean install
if %errorlevel% neq 0 (
    echo ❌ Build failed! Check error messages above
    pause
    exit /b 1
) else (
    echo ✅ Build successful
)

echo.
echo [4/4] Starting Spring Boot application...
echo.
echo 📋 Important Notes:
echo    - Make sure Oracle Database is running on localhost:1521
echo    - Default credentials: HR/123
echo    - Application will start on port 8081
echo    - Press Ctrl+C to stop the application
echo.
echo 🚀 Starting application...
mvn spring-boot:run
