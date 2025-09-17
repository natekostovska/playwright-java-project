# Use Maven with Java 17
FROM maven:3.9.6-eclipse-temurin-17

# Set working directory
WORKDIR /app

# Install required system packages (Playwright browser dependencies)
RUN apt-get update && apt-get install -y \
    wget curl unzip gnupg2 \
    libglib2.0-0 \
    libnspr4 \
    libnss3 \
    libdbus-1-3 \
    libatk1.0-0 \
    libatk-bridge2.0-0 \
    libcups2 \
    libxcb1 \
    libxkbcommon0 \
    libatspi2.0-0 \
    libx11-6 \
    libxcomposite1 \
    libxdamage1 \
    libxext6 \
    libxfixes3 \
    libxrandr2 \
    libgbm1 \
    libcairo2 \
    libpango-1.0-0 \
    libasound2 \
    && rm -rf /var/lib/apt/lists/*

# Copy your project files into the container
COPY . .

# Install Playwright browsers for Java
RUN mvn exec:java -Dexec.mainClass="com.microsoft.playwright.CLI" -Dexec.args="install"

# Pre-build Maven project (skip tests to speed up image)
RUN mvn clean install -DskipTests

# Run TestNG suite (e.g. smoke tests)
CMD ["mvn", "test", "-DsuiteXmlFile=src/test/resources/suites/smokeTests.xml"]
