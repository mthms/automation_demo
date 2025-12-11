# GitHub Secrets Setup Guide

This guide explains how to configure GitHub Secrets for environment-specific configuration.

## Overview

The workflow uses GitHub Secrets to populate environment configuration files at runtime. The configuration is generated from `environment.properties.example` and values are injected from secrets via a JSON structure passed to the composite action.

## Secret Naming Convention

Secrets can be named in two ways:

### 1. Environment-Specific Secrets (Recommended)
Format: `{KEY}_{ENVIRONMENT}` (uppercase)

Examples:
- `BASE_URL_TESTING` - Base URL for testing environment
- `BASE_URL_STAGING` - Base URL for staging environment
- `BASE_URL_PRODUCTION` - Base URL for production environment
- `WEB_RUN_MODE_TESTING` - Web run mode for testing
- `SELENIUM_GRID_URL_STAGING` - Selenium Grid URL for staging

### 2. Generic Secrets (Fallback)
Format: `{KEY}` (uppercase)

Examples:
- `BASE_URL` - Base URL (used if environment-specific secret not found)
- `TARGET_BROWSER` - Target browser
- `WEB_RUN_MODE` - Web run mode
- `SELENIUM_GRID_URL` - Selenium Grid URL

**Priority**: Environment-specific secrets take precedence over generic secrets.

## Available Configuration Keys

| Secret Name | Property Key | Description | Example |
|------------|--------------|-------------|---------|
| `BASE_URL_{ENV}` or `BASE_URL` | `base.url` | Application base URL | `https://automationintesting.online` |
| `TARGET_BROWSER_{ENV}` or `TARGET_BROWSER` | `target.browser` | Browser name | `chrome`, `firefox`, `edge` |
| `WEB_RUN_MODE_{ENV}` or `WEB_RUN_MODE` | `web.run.mode` | Execution mode | `local`, `localHeadless`, `remote` |
| `SELENIUM_GRID_URL_{ENV}` or `SELENIUM_GRID_URL` | `selenium.grid.url` | Selenium Grid URL | `http://localhost:4444/wd/hub` |
| `CHROME_ARGS_{ENV}` or `CHROME_ARGS` | `chrome.args` | Additional Chrome arguments (comma-separated) | `--start-maximized,--disable-gpu` |
| `CHROME_DRIVER_PATH_{ENV}` or `CHROME_DRIVER_PATH` | `chrome.driver.path` | ChromeDriver path (optional) | `resources/drivers/webdrivers/chromedriver` |
| `CHROME_BINARY_PATH_{ENV}` or `CHROME_BINARY_PATH` | `chrome.binary.path` | Chrome binary path (optional) | `/usr/bin/google-chrome` |

## Setting Up Secrets in GitHub

### Via GitHub Web Interface

1. Go to your repository on GitHub
2. Navigate to **Settings** → **Secrets and variables** → **Actions**
3. Click **New repository secret**
4. Enter the secret name (e.g., `BASE_URL_TESTING`)
5. Enter the secret value (e.g., `https://automationintesting.online`)
6. Click **Add secret**

### Via GitHub CLI

```bash
# Set environment-specific secret
gh secret set BASE_URL_TESTING --body "https://automationintesting.online"

# Set generic secret (fallback)
gh secret set BASE_URL --body "https://automationintesting.online"
```

## Example Configuration

### Testing Environment

```bash
BASE_URL_TESTING=https://automationintesting.online
TARGET_BROWSER_TESTING=chrome
WEB_RUN_MODE_TESTING=localHeadless
```

### Staging Environment

```bash
BASE_URL_STAGING=https://staging.example.com
TARGET_BROWSER_STAGING=chrome
WEB_RUN_MODE_STAGING=localHeadless
SELENIUM_GRID_URL_STAGING=http://selenium-grid:4444/wd/hub
CHROME_HEADLESS_STAGING=true
```

### Production Environment

```bash
BASE_URL_PRODUCTION=https://prod.example.com
TARGET_BROWSER_PRODUCTION=chrome
WEB_RUN_MODE_PRODUCTION=remote
SELENIUM_GRID_URL_PRODUCTION=http://prod-selenium-grid:4444/wd/hub
CHROME_HEADLESS_PRODUCTION=true
```

## How It Works

1. Workflow starts and determines the target environment (default: `testing`)
2. All repository variables and secrets are automatically collected using `toJson(vars)` and `toJson(secrets)`
3. `generate-env-config` action is called with:
   - Environment name
   - All variables and secrets passed as JSON via environment variables
4. The action copies `environment.properties.example` to `environment_{env}.properties`
5. Python script parses the JSON and for each property checks:
   - First: Environment-specific key (e.g., `BASE_URL_TESTING`)
   - Then: Generic key (e.g., `BASE_URL`)
   - If neither exists: Uses default value from example file
6. The generated file is used by tests during execution

### Automatic Secret/Variable Loading

The workflow uses GitHub's built-in functions to automatically load ALL secrets and variables:

```yaml
env:
  GHA_VARS_JSON: ${{ toJson(vars) }}
  GHA_SECRETS_JSON: ${{ toJson(secrets) }}
```

**Benefits:**
- No need to manually list each secret/variable in the workflow
- Automatically picks up new secrets/variables when added
- Secrets override variables (higher priority)
- Scales automatically as your configuration grows

## Troubleshooting

### Secret not being used

- Verify the secret name matches exactly (case-sensitive)
- Check that the secret is set in the repository settings
- Ensure the secret name follows the naming convention
- Review workflow logs for secret substitution messages

### File not generated

- Check that `environment.properties.example` exists
- Verify the workflow has permissions to write files
- Review the workflow logs for error messages

### Values not updating

- Ensure secrets are updated in GitHub repository settings
- Re-run the workflow to regenerate the config file
- Check that the secret name matches the expected format

## Security Notes

- Never commit actual environment configuration files with secrets
- Always use GitHub Secrets for sensitive values
- Review and rotate secrets regularly
- Use environment-specific secrets when possible to minimize exposure
