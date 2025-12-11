# Automation Testing Framework

A robust, enterprise-grade automation testing framework built with Java, Selenium WebDriver, TestNG, and Allure Reporting. This framework follows the Page Object Model (POM) design pattern and supports parallel test execution with comprehensive CI/CD integration via GitHub Actions.

## 📋 Table of Contents

- [Overview](#overview)
- [Architecture](#architecture)
- [Project Structure](#project-structure)
- [Technologies](#technologies)
- [Prerequisites](#prerequisites)
- [Setup](#setup)
- [Running Tests](#running-tests)
- [Configuration](#configuration)
- [GitHub Actions CI/CD](#github-actions-cicd)
- [Allure Reporting](#allure-reporting)
- [Test Coverage](#test-coverage)
- [Contributing](#contributing)

## 🎯 Overview

This automation framework is designed to test web applications using Selenium WebDriver with a focus on:

- **Maintainability**: Page Object Model pattern for clean separation of concerns
- **Scalability**: Parallel execution support via TestNG
- **Reliability**: Thread-safe WebDriver management with ThreadLocal
- **Reporting**: Rich Allure reports with detailed test execution information
- **CI/CD Integration**: Automated test execution on GitHub Actions
- **Environment Management**: Dynamic configuration via environment-specific properties

### Current Test Coverage

The framework currently covers **Admin Panel Authorization** flows, with test cases tracking available in `hotel_reservations_test_cases.csv`.

## 🏗️ Architecture

### Design Patterns

- **Page Object Model (POM)**: Encapsulates web page elements and interactions
- **Factory Pattern**: Centralized WebDriver instance creation
- **ThreadLocal Pattern**: Thread-safe WebDriver management for parallel execution

### Key Components

```
┌─────────────────────────────────────────────────────────────┐
│                    Test Execution Layer                      │
│  (TestNG Test Classes extending BaseTest)                    │
└───────────────────────┬─────────────────────────────────────┘
                        │
┌───────────────────────▼─────────────────────────────────────┐
│                    Base Test Layer                           │
│  (Setup/Teardown, ThreadLocal WebDriver Management)         │
└───────────────────────┬─────────────────────────────────────┘
                        │
┌───────────────────────▼─────────────────────────────────────┐
│                   Page Object Layer                          │
│  (Page Classes extending BaseWebPage)                        │
└───────────────────────┬─────────────────────────────────────┘
                        │
┌───────────────────────▼─────────────────────────────────────┐
│                   Helper Layer                               │
│  (DataHelper, WebDriversFactory, Configs)                   │
└───────────────────────┬─────────────────────────────────────┘
                        │
┌───────────────────────▼─────────────────────────────────────┐
│                   Configuration Layer                        │
│  (Environment Properties, GitHub Secrets/Variables)          │
└─────────────────────────────────────────────────────────────┘
```

## 📁 Project Structure

```
automationdemo/
│
├── .github/                          # GitHub Actions workflows and composite actions
│   ├── workflows/
│   │   └── run-tests.yml            # Main CI/CD workflow
│   ├── actions/                      # Reusable composite actions
│   │   ├── setup-java-maven/        # Java & Maven setup
│   │   ├── setup-chrome-driver/     # Chrome & ChromeDriver setup
│   │   ├── generate-env-config/     # Environment config generation
│   │   └── publish-allure/          # Allure report generation & upload
│   └── GITHUB_SECRETS_SETUP.md      # Secrets configuration guide
│
├── resources/                        # Test resources
│   ├── environments/
│   │   └── environment.properties.example  # Environment config template
│   └── drivers/
│       └── webdrivers/              # WebDriver binaries (gitignored)
│
├── src/
│   ├── main/java/com/automationdemo/
│   │   ├── helpers/                 # Helper classes
│   │   │   ├── DataHelper.java      # Configuration loader
│   │   │   └── factories/           # Factory classes
│   │   │       ├── WebDriversFactory.java    # WebDriver factory
│   │   │       └── MobileDriversFactory.java # Mobile driver factory
│   │   ├── models/                  # Data models
│   │   │   └── Configs.java         # Configuration model
│   │   └── pages/                   # Page Object Model classes
│   │       ├── BaseWebPage.java     # Base page with common methods
│   │       └── adminpanel/
│   │           └── AdminLoginPOM.java  # Admin login page
│   │
│   └── test/java/
│       └── base/
│           ├── BaseTest.java        # Base test class
│           └── web/
│               └── authorization/
│                   └── AdminAuthorizationTests.java  # Test classes
│
├── testng.xml                       # TestNG suite configuration
├── pom.xml                          # Maven project configuration
├── hotel_reservations_test_cases.csv  # Test cases tracking
└── README.md                        # This file
```

## 🛠️ Technologies

| Technology | Version | Purpose |
|------------|---------|---------|
| **Java** | 25 | Programming language |
| **Maven** | Latest | Build tool and dependency management |
| **Selenium WebDriver** | 4.20.0 | Web automation framework |
| **TestNG** | 7.10.2 | Testing framework with parallel execution |
| **Allure** | 2.24.0 | Test reporting framework |
| **SLF4J** | 2.0.12 | Logging facade |

## 📦 Prerequisites

Before running the tests, ensure you have the following installed:

- **JDK 25** or later
- **Maven 3.6+**
- **Chrome Browser** (latest stable version)
- **ChromeDriver** (will be downloaded automatically via GitHub Actions; for local runs, ensure it matches your Chrome version)
- **Git** (for cloning and version control)

### Verify Installation

```bash
java -version    # Should show Java 25
mvn -version     # Should show Maven 3.6+
google-chrome --version  # Should show Chrome version (Linux)
# or
/Applications/Google\ Chrome.app/Contents/MacOS/Google\ Chrome --version  # macOS
```

## 🚀 Setup

### 1. Clone the Repository

```bash
git clone <repository-url>
cd automationdemo
```

### 2. Configure Environment

Copy the example environment file and customize it:

```bash
cp resources/environments/environment.properties.example \
   resources/environments/environment_testing.properties
```

Edit `environment_testing.properties` with your test environment details.

### 3. Install Dependencies

Maven will download all required dependencies automatically:

```bash
mvn clean install
```

### 4. Download ChromeDriver (Local Runs)

For local execution, you may need ChromeDriver. The framework will attempt to locate it automatically, but you can also:

**Linux/macOS:**
```bash
# Download matching ChromeDriver version
# Place it in resources/drivers/webdrivers/chromedriver
chmod +x resources/drivers/webdrivers/chromedriver
```

**Windows:**
- Download ChromeDriver and place it in `resources\drivers\webdrivers\chromedriver.exe`

## ▶️ Running Tests

### Run All Tests

```bash
mvn clean test
```

### Run Tests for Specific Environment

```bash
mvn clean test -Denv=testing
```

### Run Tests with Specific Web Run Mode

```bash
mvn clean test -Denv=testing -Dweb.run.mode=localHeadless
```

### Run Tests in Parallel

Tests are configured to run in parallel via `testng.xml` with:
- **Parallel execution**: `methods`
- **Thread count**: `5`
- **Data provider thread count**: `3`

Modify `testng.xml` to adjust parallel execution settings.

### Run Specific Test Class

```bash
mvn test -Dtest=AdminAuthorizationTests
```

### Generate Allure Report Locally

After running tests:

```bash
# Generate report
mvn allure:report

# Serve report (opens in browser)
allure serve target/allure-results
```

**Note**: Install Allure CLI for serving reports locally:
- **macOS**: `brew install allure`
- **Linux**: Download from [Allure Releases](https://github.com/allure-framework/allure2/releases)
- **Windows**: `choco install allure-commandline`

## ⚙️ Configuration

### Environment Configuration

The framework supports multiple environments through property files:

```
resources/environments/
├── environment.properties.example    # Template (committed)
├── environment_testing.properties    # Testing environment (gitignored)
├── environment_staging.properties    # Staging environment (gitignored)
└── environment_production.properties # Production environment (gitignored)
```

### Configuration Properties

| Property | Description | Example Values |
|----------|-------------|----------------|
| `base.url` | Application base URL | `https://automationintesting.online` |
| `target.browser` | Browser to use | `chrome`, `firefox`, `edge` |
| `web.run.mode` | Execution mode | `local`, `localHeadless`, `remote`, `remoteHeadless` |
| `selenium.grid.url` | Selenium Grid URL (for remote mode) | `http://localhost:4444/wd/hub` |
| `chrome.args` | Additional Chrome arguments (comma-separated) | `--start-maximized,--disable-gpu` |
| `chrome.driver.path` | ChromeDriver path (optional, auto-detected) | `resources/drivers/webdrivers/chromedriver` |
| `chrome.binary.path` | Chrome binary path (optional) | `/usr/bin/google-chrome` |

### Web Run Modes

- **`local`**: Run tests locally with visible browser
- **`localHeadless`**: Run tests locally in headless mode (no UI)
- **`remote`**: Run tests on Selenium Grid with visible browser
- **`remoteHeadless`**: Run tests on Selenium Grid in headless mode

### Environment Selection

The framework selects the environment based on the `env` system property:

1. System property `-Denv=<environment>` (highest priority)
2. Default: `testing`

Example:
```bash
mvn test -Denv=staging  # Uses environment_staging.properties
```

## 🔄 GitHub Actions CI/CD

The project includes a comprehensive CI/CD pipeline that automatically runs tests on push and pull requests.

### Workflow Overview

```
┌──────────────────────────────────────────────────────────┐
│  Trigger: push/pull_request/workflow_dispatch            │
└──────────────────────┬───────────────────────────────────┘
                       │
        ┌──────────────▼──────────────┐
        │  Checkout Code               │
        └──────────────┬───────────────┘
                       │
        ┌──────────────▼──────────────┐
        │  Setup Java & Maven          │
        │  (Composite Action)          │
        └──────────────┬───────────────┘
                       │
        ┌──────────────▼──────────────┐
        │  Setup Chrome & ChromeDriver │
        │  (Composite Action)          │
        └──────────────┬───────────────┘
                       │
        ┌──────────────▼──────────────┐
        │  Generate Environment Config │
        │  (Composite Action)          │
        │  - Loads GitHub Secrets/Vars │
        │  - Generates .properties file│
        └──────────────┬───────────────┘
                       │
        ┌──────────────▼──────────────┐
        │  Cache Maven Dependencies    │
        └──────────────┬───────────────┘
                       │
        ┌──────────────▼──────────────┐
        │  Run Tests                   │
        │  mvn clean test              │
        └──────────────┬───────────────┘
                       │
        ┌──────────────▼──────────────┐
        │  Publish Allure Report       │
        │  (Composite Action)          │
        │  - Generate HTML report      │
        │  - Upload as artifact        │
        └──────────────────────────────┘
```

### Workflow File

Located at: `.github/workflows/run-tests.yml`

### Manual Workflow Trigger

You can manually trigger the workflow via GitHub Actions UI:

1. Go to **Actions** tab in GitHub
2. Select **Run Automation Tests** workflow
3. Click **Run workflow**
4. Select environment: `testing`, `staging`, or `production`
5. Click **Run workflow**

### Composite Actions

The workflow uses reusable composite actions:

#### 1. `setup-java-maven`
- Installs JDK 25 (Temurin distribution)
- Configures Maven with dependency caching
- Verifies installation

#### 2. `setup-chrome-driver`
- Installs Google Chrome (Linux/macOS)
- Detects Chrome version
- Downloads matching ChromeDriver from Chrome for Testing
- Handles architecture-specific downloads (arm64/x64)

#### 3. `generate-env-config`
- Copies `environment.properties.example` to environment-specific file
- Loads all GitHub repository variables and secrets via `toJson(vars)` and `toJson(secrets)`
- Injects values into properties file using `sed`
- Supports environment-specific and generic secrets/variables

#### 4. `publish-allure`
- Installs Allure CLI
- Generates HTML report from test results
- Uploads report and results as workflow artifacts
- Configurable retention periods

### GitHub Secrets & Variables Setup

See `.github/GITHUB_SECRETS_SETUP.md` for detailed instructions.

**Quick Setup:**

1. Go to **Settings** → **Secrets and variables** → **Actions**
2. Add repository variables/secrets:
   - `BASE_URL_TESTING`: Your testing environment URL
   - `TARGET_BROWSER`: `chrome`
   - `WEB_RUN_MODE`: `localHeadless`

**Secret Naming:**
- Environment-specific: `{KEY}_{ENVIRONMENT}` (e.g., `BASE_URL_TESTING`)
- Generic: `{KEY}` (e.g., `BASE_URL`)

### Workflow Artifacts

After workflow execution, the following artifacts are available:

- **allure-report**: HTML Allure report (30 days retention)
- **allure-results**: Raw Allure results (7 days retention)

Download artifacts from the workflow run page.

## 📊 Allure Reporting

### Viewing Reports

#### Local

After running tests:

```bash
allure serve target/allure-results
```

#### GitHub Actions

1. Go to the workflow run
2. Scroll to **Artifacts** section
3. Download `allure-report`
4. Extract and open `index.html` in a browser

### Report Features

- **Test Execution Overview**: Summary of passed/failed/skipped tests
- **Test Details**: Step-by-step execution with screenshots (if configured)
- **Timeline**: Test execution timeline
- **Behaviors**: Test grouping by Epic/Feature/Story
- **Suites**: Test organization by test suites
- **Graphs**: Visual representation of test results

### Allure Annotations

The framework uses Allure annotations for rich reporting:

```java
@Epic("Admin Panel")
@Feature("Authorization & Session Management")
@Story("Admin Login")
@Severity(SeverityLevel.BLOCKER)
@Test(description = "TC-001: Successful admin login...")
public void shouldLoginSuccessfullyWithValidCredentials() {
    // Test implementation
}
```

## 📈 Test Coverage

Test cases are tracked in `hotel_reservations_test_cases.csv` with an `Automated` column indicating implementation status.

### Current Coverage

**Implemented:**
- ✅ TC-001: Successful admin login with valid credentials
- ✅ TC-002: Unsuccessful admin login with invalid password
- ✅ TC-003: Unsuccessful admin login with empty credentials

**Planned:**
- 🔄 Additional authorization flows (TC-004 to TC-006)
- 🔄 Rooms management flows (TC-007 to TC-017)
- 🔄 Reservation handling flows (TC-018 to TC-030)
- 🔄 UI/UX validation flows (TC-031 to TC-036)

## 🤝 Contributing

### Adding New Tests

1. **Create Page Object** (if new page):
   ```java
   // src/main/java/com/automationdemo/pages/...
   public class NewPagePOM extends BaseWebPage {
       // Page elements and methods
   }
   ```

2. **Create Test Class**:
   ```java
   // src/test/java/base/web/...
   public class NewTests extends BaseTest {
       @Test
       public void testMethod() {
           // Test implementation
       }
   }
   ```

3. **Update testng.xml** to include new test class

4. **Update CSV** with automation status

### Code Style

- Follow Java naming conventions
- Use meaningful variable and method names
- Add Javadoc comments for public methods
- Keep page objects focused and single-responsibility

### Pull Request Process

1. Create a feature branch
2. Make your changes
3. Ensure all tests pass locally
4. Update documentation if needed
5. Create a pull request with a clear description

## 📝 License

[Specify your license here]

## 👥 Authors

[Specify authors/contributors here]

## 🔗 Resources

- [Selenium Documentation](https://www.selenium.dev/documentation/)
- [TestNG Documentation](https://testng.org/doc/documentation-main.html)
- [Allure Documentation](https://docs.qameta.io/allure/)
- [Page Object Model Pattern](https://www.selenium.dev/documentation/test_practices/encouraged/page_object_models/)

---

**Happy Testing! 🚀**
