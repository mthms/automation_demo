package com.automationdemo.models;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Configs {
    private String environmentName;
    private String baseUrl;
    private String targetBrowserName;
    private String webRunMode;
    private String seleniumGridUrl;
    private String chromeDriverPath;
    private String chromeBinaryPath;
    private boolean chromeHeadless;
    private List<String> chromeArgs = new ArrayList<>();

    public String getEnvironmentName() {
        return environmentName;
    }

    public void setEnvironmentName(String environmentName) {
        this.environmentName = environmentName;
    }

    public String getBaseUrl() {
        return baseUrl;
    }

    public void setBaseUrl(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    public String getTargetBrowserName() {
        return targetBrowserName;
    }

    public void setTargetBrowserName(String targetBrowserName) {
        this.targetBrowserName = targetBrowserName;
    }

    public String getWebRunMode() {
        return webRunMode;
    }

    public void setWebRunMode(String webRunMode) {
        this.webRunMode = webRunMode;
    }

    public String getSeleniumGridUrl() {
        return seleniumGridUrl;
    }

    public void setSeleniumGridUrl(String seleniumGridUrl) {
        this.seleniumGridUrl = seleniumGridUrl;
    }

    public String getChromeDriverPath() {
        return chromeDriverPath;
    }

    public void setChromeDriverPath(String chromeDriverPath) {
        this.chromeDriverPath = chromeDriverPath;
    }

    public String getChromeBinaryPath() {
        return chromeBinaryPath;
    }

    public void setChromeBinaryPath(String chromeBinaryPath) {
        this.chromeBinaryPath = chromeBinaryPath;
    }

    public boolean isChromeHeadless() {
        return chromeHeadless;
    }

    public void setChromeHeadless(boolean chromeHeadless) {
        this.chromeHeadless = chromeHeadless;
    }

    public List<String> getChromeArgs() {
        return Collections.unmodifiableList(chromeArgs);
    }

    public void setChromeArgs(List<String> chromeArgs) {
        this.chromeArgs = chromeArgs == null ? new ArrayList<>() : new ArrayList<>(chromeArgs);
    }
}
