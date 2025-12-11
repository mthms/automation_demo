package com.automationdemo.helpers;

import com.automationdemo.models.Configs;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class DataHelper {
    private static final String DEFAULT_ENVIRONMENT = "testing";

    public Configs loadConfigs() {
        return loadConfigs(resolveEnvironmentName());
    }

    public Configs loadConfigs(String environmentName) {
        String env = (environmentName == null || environmentName.isBlank())
                ? DEFAULT_ENVIRONMENT
                : environmentName.trim().toLowerCase();

        Properties properties = loadProperties(env);
        Configs configs = new Configs();
        configs.setEnvironmentName(env);

        configs.setBaseUrl(properties.getProperty("base.url", "http://localhost"));

        configs.setTargetBrowserName(properties.getProperty("target.browser", "chrome"));

        configs.setWebRunMode(properties.getProperty("web.run.mode", "local"));

        configs.setSeleniumGridUrl(properties.getProperty("selenium.grid.url", "http://localhost:4444/wd/hub"));

        configs.setChromeDriverPath(properties.getProperty("chrome.driver.path"));

        configs.setChromeBinaryPath(properties.getProperty("chrome.binary.path"));

        configs.setChromeArgs(parseArgs(properties.getProperty("chrome.args")));

        return configs;
    }

    private Properties loadProperties(String envName) {
        Properties properties = new Properties();
        String resourcePath = String.format("environments/environment_%s.properties", envName);
        
        // Load from classpath (works when resources are in target/classes or target/test-classes)
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        InputStream stream = classLoader.getResourceAsStream(resourcePath);
        
        if (stream == null) {
            // Fallback to file system (for IDE or direct execution)
            Path filePath = Paths.get("resources", resourcePath);
            if (Files.exists(filePath)) {
                try {
                    stream = Files.newInputStream(filePath);
                } catch (IOException e) {
                    throw new IllegalStateException("Failed to open config file: " + filePath, e);
                }
            } else {
                throw new IllegalStateException("Environment file not found: " + resourcePath + 
                    ". Set env via: mvn test -Denv=<environment>");
            }
        }
        
        try (InputStream closable = stream) {
            properties.load(closable);
        } catch (IOException e) {
            throw new IllegalStateException("Failed to load config from: " + resourcePath, e);
        }
        
        return properties;
    }


    private static String resolveEnvironmentName() {
        String env = System.getProperty("env");
        return (env != null && !env.isBlank()) ? env : DEFAULT_ENVIRONMENT;
    }

    private static List<String> parseArgs(String argsValue) {
        if (argsValue == null || argsValue.isBlank()) {
            return List.of();
        }
        String[] tokens = argsValue.split(",");
        List<String> args = new ArrayList<>();
        for (String token : tokens) {
            String trimmed = token.trim();
            if (!trimmed.isEmpty()) {
                args.add(trimmed);
            }
        }
        return args;
    }

}


