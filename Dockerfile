# Use official Playwright image with Node and browsers
FROM mcr.microsoft.com/playwright:v1.53.2-java

# Set working directory inside container
WORKDIR /app

# Copy Maven config and source code
COPY pom.xml .
COPY src ./src

# Install Maven (already included in this image)
# Run Maven to download dependencies
RUN mvn dependency:go-offline -B

# Install Playwright browsers (needed for Playwright Java)
RUN npx playwright install --with-deps

# Run tests
CMD ["mvn", "clean", "verify", "allure:report"]
