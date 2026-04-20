FROM ubuntu:22.04

# Avoid interactive prompts
ENV DEBIAN_FRONTEND=noninteractive

# Playwright + CI config
ENV CI=true
ENV PLAYWRIGHT_BROWSERS_PATH=/ms-playwright
ENV TZ=Europe/Skopje

# Install system dependencies
RUN apt-get update && apt-get install -y \
    openjdk-17-jdk maven curl wget unzip gnupg2 git \
    libglib2.0-0 libnspr4 libnss3 libdbus-1-3 libatk1.0-0 libatk-bridge2.0-0 libcups2 \
    libxcb1 libxkbcommon0 libatspi2.0-0 libx11-6 libxcomposite1 libxdamage1 libxext6 \
    libxfixes3 libxrandr2 libgbm1 libcairo2 libpango-1.0-0 libasound2 libx11-xcb1 \
    libxcursor1 libgtk-3-0 libpangocairo-1.0-0 libcairo-gobject2 libgdk-pixbuf-2.0-0 \
    fonts-liberation libayatana-appindicator3-1 xdg-utils \
 && rm -rf /var/lib/apt/lists/*

WORKDIR /workspace

# Copy only pom first (better caching)
COPY pom.xml .

# Pre-download dependencies (Maven cache layer)
RUN mvn dependency:go-offline

# Copy full project
COPY . .

# Install Playwright browsers (more stable approach)
RUN mvn -q exec:java \
    -Dexec.mainClass=com.microsoft.playwright.CLI \
    -Dexec.args="install --with-deps"

# Build project
RUN mvn clean install -DskipTests

# Git config (safe for CI logs/tools)
RUN git config --global user.name "github-actions[bot]" && \
    git config --global user.email "github-actions[bot]@users.noreply.github.com"