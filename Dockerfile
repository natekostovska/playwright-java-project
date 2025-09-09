FROM openjdk:11-jdk-slim

# Install dependencies
RUN apt-get update && apt-get install -y \
    curl zip unzip gnupg2 ca-certificates nodejs npm \
    && apt-get clean && rm -rf /var/lib/apt/lists/*

# Install Maven
ENV MAVEN_VERSION=3.9.6

RUN apt-get update && apt-get install -y \
    curl zip unzip gnupg2 ca-certificates nodejs npm && \
    apt-get clean

RUN curl -fsSL https://archive.apache.org/dist/maven/maven-3/${MAVEN_VERSION}/binaries/apache-maven-${MAVEN_VERSION}-bin.zip -o maven.zip && \
    unzip maven.zip -d /opt && \
    ln -s /opt/apache-maven-${MAVEN_VERSION} /opt/maven && \
    rm maven.zip

# Set Maven environment variables
ENV MAVEN_HOME=/opt/maven
ENV PATH="${MAVEN_HOME}/bin:${PATH}"

# Install Node.js 18.x
RUN curl -fsSL https://deb.nodesource.com/setup_18.x | bash - && \
    apt-get install -y nodejs

# Install Playwright
RUN npm install -g playwright && \
    npx playwright install-deps && \
    npx playwright install

# Set working directory
WORKDIR /tests

# Default CMD
CMD ["mvn", "test"]
