# Use official OpenJDK 11 image as base
FROM openjdk:11-jdk

# Install dependencies
RUN apt-get update && \
    apt-get install -y curl zip unzip gnupg2 ca-certificates && \
    rm -rf /var/lib/apt/lists/*

# Manually install Maven
RUN curl -fsSL https://downloads.apache.org/maven/maven-3/3.9.9/binaries/apache-maven-3.9.9-bin.zip -o /tmp/maven.zip && \
    unzip /tmp/maven.zip -d /opt && \
    ln -s /opt/apache-maven-3.9.9/bin/mvn /usr/bin/mvn && \
    rm /tmp/maven.zip

# Verify Maven installation
RUN mvn --version

# Install Node.js 16
RUN curl -fsSL https://deb.nodesource.com/setup_16.x | bash - && \
    apt-get install -y nodejs && \
    rm -rf /var/lib/apt/lists/*

# Install Playwright dependencies
RUN npm install -g playwright && \
    npx playwright install-deps && \
    npx playwright install

# Set working directory
WORKDIR /tests

# Copy project files into container
COPY . .

# Default command to run tests
CMD ["mvn", "verify"]
