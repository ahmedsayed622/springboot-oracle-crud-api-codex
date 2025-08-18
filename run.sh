#!/bin/bash

echo "========================================"
echo "  Spring Boot CRUD API Setup Script"
echo "========================================"
echo

echo "[1/4] Checking Java installation..."
if ! command -v java &> /dev/null; then
    echo "❌ Java not found! Please install Java 17+"
    echo "Download from: https://www.oracle.com/java/technologies/downloads/"
    exit 1
else
    echo "✅ Java found"
    java -version
fi

echo
echo "[2/4] Checking Maven installation..."
if ! command -v mvn &> /dev/null; then
    echo "❌ Maven not found! Please install Maven"
    echo "Download from: https://maven.apache.org/download.cgi"
    exit 1
else
    echo "✅ Maven found"
    mvn -version
fi

echo
echo "[3/4] Building project..."
mvn clean install
if [ $? -ne 0 ]; then
    echo "❌ Build failed! Check error messages above"
    exit 1
else
    echo "✅ Build successful"
fi

echo
echo "[4/4] Starting Spring Boot application..."
echo
echo "📋 Important Notes:"
echo "   - Make sure Oracle Database is running on localhost:1521"
echo "   - Default credentials: HR/123"
echo "   - Application will start on port 8081"
echo "   - Press Ctrl+C to stop the application"
echo
echo "🚀 Starting application..."
mvn spring-boot:run
