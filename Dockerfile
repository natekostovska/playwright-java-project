# Use official OpenJDK 11 image as base
FROM openjdk:11-jdk

# Install system dependencies
RUN apt-get update && \
    apt-get install -y curl zip unzip gnupg2 ca-certificates nodejs npm && \
    rm -rf /var/lib/apt/lists/*

# Install Maven manually (instead of using SDKMAN to simplify path issues)
RUN curl -fsSL https://downloads.apache.org/maven/maven-3/3.9.9/binaries/apache-maven-3.9.9-bin.zip -o maven.zip && \
    unzip maven.zip -d /opt && \
    ln -s /opt/apache-maven-3.9.9 /opt/maven && \
    ln -s /opt/maven/bin/mvn /usr/bin/mvn && \
    rm maven.zip

# Install Playwright and dependencies
RUN npm install -g playwright && \
    npx playwright install-deps && \
    npx playwright install

# Set working directory
WORKDIR /tests

# Copy project files
COPY . .

# Default command (can be overridden)
CMD ["mvn", "verify"]
