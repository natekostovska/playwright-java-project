# Use official OpenJDK 11 image as base
FROM openjdk:11-jdk

# Install dependencies
RUN apt-get update && \
    apt-get install -y curl zip unzip gnupg2 ca-certificates && \
    rm -rf /var/lib/apt/lists/*

# Install SDKMAN and Maven 3.9.9
RUN curl -s "https://get.sdkman.io" | bash && \
    bash -c "source $HOME/.sdkman/bin/sdkman-init.sh && sdk install maven 3.9.9"

# Set environment variables for Maven
ENV MAVEN_HOME=/root/.sdkman/candidates/maven/current
ENV PATH=$MAVEN_HOME/bin:$PATH

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
