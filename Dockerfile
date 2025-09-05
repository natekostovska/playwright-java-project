# Use official OpenJDK 11 image
FROM openjdk:11-jdk

# Install required dependencies
RUN apt-get update && \
    apt-get install -y curl unzip zip gnupg2 ca-certificates && \
    curl -fsSL https://deb.nodesource.com/setup_16.x | bash - && \
    apt-get install -y nodejs && \
    npm install -g playwright && \
    npx playwright install-deps && \
    npx playwright install

# Install Maven manually
RUN curl -fsSL https://downloads.apache.org/maven/maven-3/3.9.9/binaries/apache-maven-3.9.9-bin.zip -o maven.zip && \
    unzip maven.zip -d /opt && \
    ln -s /opt/apache-maven-3.9.9 /opt/maven && \
    rm maven.zip

# Set Maven environment variables
ENV MAVEN_HOME=/opt/maven
ENV PATH=$MAVEN_HOME/bin:$PATH

# Set working directory
WORKDIR /tests

# Copy project files
COPY . .

# Default command to run tests
CMD ["mvn", "verify"]
