# Playwright Java Project

This repository contains automated tests using **Playwright with Java** and **TestNG**.  
The project is integrated with **GitHub Actions** for CI/CD, Docker, and test reporting.

---

## CI/CD Status

[![Playwright CI](https://github.com/nate338/playwright-java-project/actions/workflows/playwright-ci.yml/badge.svg)](https://github.com/nate338/playwright-java-project/actions/workflows/playwright-ci.yml)

---

## Test Reports

- **Allure Report** (deployed on GitHub Pages)  
  [![Allure Report](https://img.shields.io/badge/Allure-Report-blue?logo=allure-test-report)](https://github.com/natekostovska/playwright-java-project)

- **Extent Report** (artifact from latest workflow run)  
  [![Extent Report](https://img.shields.io/badge/Extent-Report-blue?logo=github)](https://github.com/natekostovska/playwright-java-project/suites/extent/)

- **TestNG Report** (artifact from latest workflow run)  
  [![TestNG Report](https://img.shields.io/badge/TestNG-Report-green?logo=testng)](https://github.com/natekostovska/playwright-java-project/suites/testng/
  )

> Reports are retained for **4 weeks**.

---

## Project Features

- Run automated Playwright tests in a **Docker container**.
- Build and test on **any branch**, pull requests, scheduled runs, or manually.
- Generate and store Allure, Extent, and TestNG reports.
- Reports are deployed to **GitHub Pages** (Allure only).

---

## How to Run Locally

### 1. Clone the repository:

```bash
git clone https://github.com/natekostovska/playwright-java-project.git
cd playwright-java-project
```
### 2. Build and run tests:
```bash
mvn clean verify
```
### 3. Generate Allure report:
```bash
mvn allure:report
```
### 4. Open allure report in browser
```bash
mvn allure:serve
```
### 5. Download allure report from git
 - Extract zip file open the report by cmd -> allure open path of your report
 - Exit by CTRL + C