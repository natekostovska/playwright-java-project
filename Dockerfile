FROM mcr.microsoft.com/playwright:v1.53.2-noble

# Set working directory
WORKDIR /tests
COPY . .

# Install Playwright
RUN npm install --force
RUN npx playwright install

