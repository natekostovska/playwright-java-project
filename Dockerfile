FROM mcr.microsoft.com/playwright:v1.53.2-noble

# Set environment variables to avoid prompts
ENV DEBIAN_FRONTEND=noninteractive

# Set working directory
WORKDIR /tests
COPY . .

# Install required tools: Java, Maven, curl, unzip
RUN apt-get update && \
    apt-get install -y openjdk-11-jdk maven curl unzip && \
    apt-get clean

# Install Node dependencies
RUN npm install --force

# Install Playwright browsers
RUN npx playwright install --with-deps

# Optional: Pre-download Maven dependencies to cache layers
RUN mvn dependency:resolve

# Default command (can be overridden)
CMD ["mvn", "clean", "test", "-DsuiteXmlFile=src/test/resources/suites/smokeTests.xml"]
