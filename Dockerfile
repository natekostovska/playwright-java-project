# Base image with Node.js, browsers (for Playwright)
FROM mcr.microsoft.com/playwright:v1.53.2-focal

# Install Java 20 and Maven
RUN apt-get update && \
    apt-get install -y openjdk-20-jdk maven && \
    rm -rf /var/lib/apt/lists/*

ENV JAVA_HOME=/usr/lib/jvm/java-20-openjdk-amd64
ENV PATH="$JAVA_HOME/bin:$PATH"

WORKDIR /app

# Copy project files
COPY . .

# Install Node dependencies (Playwright CLI)
RUN npm install --force

# Install Playwright browsers
RUN npx playwright install --with-deps

# Pre-download Maven dependencies to speed up build (optional)
RUN mvn dependency:resolve

# Run tests and generate reports
CMD mvn clean test verify
