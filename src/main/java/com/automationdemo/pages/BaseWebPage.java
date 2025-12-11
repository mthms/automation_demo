package com.automationdemo.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;

import java.time.Duration;

public abstract class BaseWebPage {
    protected final WebDriver driver;

    protected BaseWebPage(WebDriver driver) {
        this.driver = driver;
    }

    public void openSubPage(String relativePath) {
        String currentUrl = driver.getCurrentUrl();
        if (relativePath == null) {
            return;
        }
        // Remove any hash fragment from current URL
        if (currentUrl.contains("#")) {
            currentUrl = currentUrl.substring(0, currentUrl.indexOf("#"));
        }
        String normalized = relativePath.startsWith("/") ? relativePath.substring(1) : relativePath;
        String newUrl = currentUrl.endsWith("/") ? currentUrl + normalized : currentUrl + "/" + normalized;
        driver.navigate().to(newUrl);
    }

    /**
     * Fluent wait wrapper to check if an element is displayed.
     */
    protected boolean isElementDisplayed(By locator) {
        try {
            WebElement element = fluentWait().until(ExpectedConditions.visibilityOfElementLocated(locator));
            return element.isDisplayed();
        } catch (TimeoutException e) {
            return false;
        }
    }

    /**
     * Fluent wait wrapper to find an element for interaction.
     */
    protected WebElement waitUntilElementIsDisplayed(By locator) {
        return fluentWait().until(ExpectedConditions.visibilityOfElementLocated(locator));
    }

    /**
     * Waits until an element is dismissed (not visible or removed from DOM).
     * Returns true if element becomes invisible within the timeout, false otherwise.
     */
    protected boolean waitUntilElementIsDismissed(By locator) {
        try {
            fluentWait().until(ExpectedConditions.invisibilityOfElementLocated(locator));
            return true;
        } catch (TimeoutException e) {
            return false;
        }
    }

    private FluentWait<WebDriver> fluentWait() {
        return new FluentWait<>(driver)
                .withTimeout(Duration.ofSeconds(10))
                .pollingEvery(Duration.ofMillis(500))
                .ignoring(NoSuchElementException.class);
    }
}
