# Base image with Playwright and dependencies
FROM mcr.microsoft.com/playwright:v1.53.2-noble

# Disable interactive prompts
ENV DEBIAN_FRONTEND=noninteractive

# Set working directory
WORKDIR /tests

# Copy project files
COPY . .

# Install Java, Maven, and other tools
RUN apt-get update && \
    apt-get install -y openjdk-11-jdk maven unzip curl && \
    apt-get clean

# Set JAVA_HOME environment variable
ENV JAVA_HOME=/usr/lib/jvm/java-11-openjdk-amd64
ENV PATH="${JAVA_HOME}/bin:${PATH}"

# Install Node.js dependencies (Playwright CLI etc.)
RUN npm install --force

# Install Playwright browsers
RUN npx playwright install --with-deps

# Optional: Pre-fetch Maven dependencies to cache them
RUN mvn dependency:resolve

# Default command to run tests and generate Allure report
CMD mvn clean test -DsuiteXmlFile=src/test/resources/suites/smokeTests.xml && \
    mvn allure:report && \
    echo "Allure report generated at: /tests/allure/reports"
