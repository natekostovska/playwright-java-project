# Use Maven with Java 17 (Eclipse Temurin)
FROM maven:3.9.6-eclipse-temurin-17

# Set working directory
WORKDIR /app

# Install required system packages, Node.js 18, and Playwright
RUN apt-get update && \
    apt-get install -y curl gnupg && \
    curl -fsSL https://deb.nodesource.com/setup_18.x | bash - && \
    apt-get install -y nodejs && \
    npm install -g playwright && \
    rm -rf /var/lib/apt/lists/*

# Install Playwright browsers
RUN playwright install

# Copy your project files into the container
COPY . .

# Build the Maven project (skip test execution)
RUN mvn clean install -DskipTests

# Run tests using TestNG with smokeTests.xml suite
CMD ["mvn", "test", "-DsuiteXmlFile=src/test/resources/suites/smokeTests.xml"]
