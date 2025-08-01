#!/bin/bash

# Dostiship Backend Runner Script

echo "🚀 Starting Dostiship Backend..."
echo "================================"

# Check if Java 17+ is installed
java_version=$(java -version 2>&1 | awk -F '"' '/version/ {print $2}' | cut -d'.' -f1)
if [ "$java_version" -lt 17 ]; then
    echo "❌ Error: Java 17 or higher is required. Current version: $(java -version 2>&1 | head -n 1)"
    exit 1
fi

# Check if Maven is installed
if ! command -v mvn &> /dev/null; then
    echo "❌ Error: Maven is not installed or not in PATH"
    exit 1
fi

# Check if PostgreSQL is running (optional check)
if command -v pg_isready &> /dev/null; then
    if ! pg_isready -h localhost -p 5432 &> /dev/null; then
        echo "⚠️  Warning: PostgreSQL is not running on localhost:5432"
        echo "   Please make sure PostgreSQL is running and the database 'dostiship' exists"
    fi
fi

echo "✅ Prerequisites check passed"
echo ""

# Set default environment variables if not set
export DB_USERNAME=${DB_USERNAME:-dostiship_user}
export DB_PASSWORD=${DB_PASSWORD:-dostiship_password}
export JWT_SECRET=${JWT_SECRET:-dostishipSecretKeyForJWTTokenGenerationThatShouldBeAtLeast256BitsLongForSecurity}
export JWT_EXPIRATION=${JWT_EXPIRATION:-86400000}

echo "🔧 Configuration:"
echo "   Database User: $DB_USERNAME"
echo "   JWT Expiration: $JWT_EXPIRATION ms"
echo ""

# Build and run the application
echo "🏗️  Building application..."
mvn clean compile -q

if [ $? -ne 0 ]; then
    echo "❌ Build failed!"
    exit 1
fi

echo "✅ Build successful"
echo ""
echo "🚀 Starting Dostiship Backend on http://localhost:8080"
echo "📚 API Documentation: http://localhost:8080/swagger-ui.html"
echo "💡 Press Ctrl+C to stop the application"
echo ""

# Run the application
mvn spring-boot:run