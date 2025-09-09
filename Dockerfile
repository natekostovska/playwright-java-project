# ✅ Base image with Playwright, Node.js, and all dependencies
FROM mcr.microsoft.com/playwright:v1.53.2-noble

# Disable interactive prompts
ENV DEBIAN_FRONTEND=noninteractive

# Set working directory
WORKDIR /tests

# Copy the entire project
COPY . .

# ✅ Install Java 11 and Maven
RUN apt-get update && \
    apt-get install -y openjdk-11-jdk maven unzip curl && \
    apt-get clean

# Set JAVA_HOME environment variable
ENV JAVA_HOME=/usr/lib/jvm/java-11-openjdk-amd64
ENV PATH="${JAVA_HOME}/bin:${PATH}"

# Install Node.js dependencies (if package.json exists)
RUN if [ -f "package.json" ]; then npm install --force; fi

# ✅ Install Playwright Browsers
RUN npx playwright install --with-deps

# ✅ Pre-fetch Maven dependencies to cache layers
RUN mvn dependency:resolve

# ✅ Default command: run tests and generate Allure report
CMD mvn clean test -DsuiteXmlFile=src/test/resources/suites/smokeTests.xml && \
    mvn allure:report -Dallure.report.directory=allure/reports && \
    echo "Allure report generated at: allure/reports"
