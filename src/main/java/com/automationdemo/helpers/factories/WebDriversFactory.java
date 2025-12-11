package com.automationdemo.helpers.factories;

import com.automationdemo.models.Configs;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class WebDriversFactory {
    private final Configs configs;

    public WebDriversFactory(Configs configs) {
        this.configs = configs;
    }

    public WebDriver createWebDriver() {
        String browser = configs.getTargetBrowserName().toLowerCase();
        if (!"chrome".equals(browser)) {
            throw new UnsupportedOperationException("Unsupported browser: " + browser);
        }

        String mode = configs.getWebRunMode() == null ? "local" : configs.getWebRunMode().toLowerCase();
        return switch (mode) {
            case "remote" -> createRemoteChromeDriver();
            case "localheadless", "headless" -> createLocalChromeDriver(true);
            default -> createLocalChromeDriver(false);
        };
    }

    private WebDriver createLocalChromeDriver(boolean forceHeadless) {
        String driverPath = resolveDriverPath();
        if (driverPath == null || driverPath.isEmpty()) {
            throw new IllegalStateException("Chromedriver path not found. Set chrome.driver.path or ensure driver exists in common locations or PATH.");
        }
        System.setProperty("webdriver.chrome.driver", driverPath);
        
        // Suppress CDP warnings
        suppressCdpWarnings();

        ChromeOptions options = buildChromeOptions(forceHeadless);
        return new ChromeDriver(options);
    }
    
    private void suppressCdpWarnings() {
        // Suppress java.util.logging warnings from Selenium CDP
        java.util.logging.Logger cdpLogger = java.util.logging.Logger.getLogger("org.openqa.selenium.devtools.CdpVersionFinder");
        cdpLogger.setLevel(java.util.logging.Level.SEVERE);
        java.util.logging.Logger chromiumLogger = java.util.logging.Logger.getLogger("org.openqa.selenium.chromium.ChromiumDriver");
        chromiumLogger.setLevel(java.util.logging.Level.SEVERE);
        // Disable parent logger propagation
        cdpLogger.setUseParentHandlers(false);
        chromiumLogger.setUseParentHandlers(false);
    }

    private WebDriver createRemoteChromeDriver() {
        suppressCdpWarnings();
        ChromeOptions options = buildChromeOptions(configs.isChromeHeadless());
        try {
            URL gridUrl = URI.create(configs.getSeleniumGridUrl()).toURL();
            return new RemoteWebDriver(gridUrl, options);
        } catch (IllegalArgumentException | MalformedURLException e) {
            throw new IllegalStateException("Invalid Selenium grid URL: " + configs.getSeleniumGridUrl(), e);
        }
    }

    private ChromeOptions buildChromeOptions(boolean forceHeadless) {
        ChromeOptions options = new ChromeOptions();
        if (configs.getChromeBinaryPath() != null && !configs.getChromeBinaryPath().isEmpty()) {
            options.setBinary(configs.getChromeBinaryPath());
        }
        boolean headless = configs.isChromeHeadless() || forceHeadless;
        if (headless) {
            options.addArguments("--headless=new");
        }
        configs.getChromeArgs().forEach(options::addArguments);
        return options;
    }

    private String resolveDriverPath() {
        // Preferred explicit path from configs/system/environment
        Path explicit = firstExistingPath(
                System.getProperty("webdriver.chrome.driver"),
                configs.getChromeDriverPath(),
                System.getenv("CHROMEDRIVER_PATH")
        );
        if (explicit != null) {
            ensureExecutable(explicit);
            return explicit.toAbsolutePath().toString();
        }

        // Project-local conventional locations (only under webdrivers)
        String baseDir = System.getProperty("user.dir");
        Path[] candidates = new Path[]{
                Paths.get(baseDir, "resources", "drivers", "webdrivers", "chromedriver"),
                Paths.get(baseDir, "resources", "drivers", "webdrivers", "chromedriver.exe")
        };
        for (Path candidate : candidates) {
            if (Files.exists(candidate)) {
                ensureExecutable(candidate);
                return candidate.toAbsolutePath().toString();
            }
        }

        // System PATH fallback
        return findOnSystemPath(isWindows() ? "chromedriver.exe" : "chromedriver");
    }

    private Path firstExistingPath(String... candidates) {
        if (candidates == null) {
            return null;
        }
        for (String candidate : candidates) {
            if (candidate == null || candidate.isBlank()) {
                continue;
            }
            Path path = Paths.get(candidate);
            if (Files.exists(path)) {
                return path;
            }
        }
        return null;
    }

    private void ensureExecutable(Path path) {
        if (isWindows()) {
            return;
        }
        File file = path.toFile();
        if (!file.canExecute()) {
            file.setExecutable(true, false);
        }
    }

    private String findOnSystemPath(String executableName) {
        String pathEnv = System.getenv("PATH");
        if (pathEnv == null || pathEnv.isEmpty()) {
            return null;
        }
        for (String dir : pathEnv.split(File.pathSeparator)) {
            Path candidate = Paths.get(dir, executableName);
            if (Files.exists(candidate) && Files.isExecutable(candidate)) {
                return candidate.toAbsolutePath().toString();
            }
        }
        return null;
    }

    private boolean isWindows() {
        return System.getProperty("os.name").toLowerCase().contains("win");
    }
}
