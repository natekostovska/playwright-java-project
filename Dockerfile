# Use official OpenJDK 11 image
FROM openjdk:11-jdk-slim

# Install dependencies
RUN apt-get update && apt-get install -y \
    curl \
    zip \
    unzip \
    gnupg2 \
    ca-certificates \
    nodejs \
    npm && \
    apt-get clean && \
    rm -rf /var/lib/apt/lists/*

# Install Maven (manually, and properly)
ENV MAVEN_VERSION=3.9.9
RUN curl -fsSL https://downloads.apache.org/maven/maven-3/${MAVEN_VERSION}/binaries/apache-maven-${MAVEN_VERSION}-bin.zip -o maven.zip && \
    unzip maven.zip -d /opt && \
    ln -s /opt/apache-maven-${MAVEN_VERSION} /opt/maven && \
    ln -s /opt/maven/bin/mvn /usr/bin/mvn && \
    rm maven.zip

# Verify mvn works
RUN mvn --version

# Install Playwright and its dependencies
RUN npm install -g playwright && \
    npx playwright install-deps && \
    npx playwright install

# Set working directory
WORKDIR /tests

# Copy test project
COPY . .

# Default command (override in CI if needed)
CMD ["mvn", "test"]
