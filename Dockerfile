# Base image with Node.js, browsers (for Playwright)
FROM mcr.microsoft.com/playwright/java:v1.54.0-noble


RUN mkdir /app
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
