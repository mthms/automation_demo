package com.automationdemo.pages.adminpanel;

import com.automationdemo.pages.BaseWebPage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class AdminLoginPOM extends BaseWebPage {

    private static final String ADMIN_PATH = "/admin";

    // Admin login form locators
    private static final By USERNAME_INPUT = By.id("username");
    private static final By PASSWORD_INPUT = By.id("password");
    private static final By LOGIN_BUTTON = By.id("doLogin");
    private static final By ERROR_BANNER = By.cssSelector(".alert.alert-danger");
    private static final By ERROR_TEXT = By.cssSelector(".alert.alert-danger p");

    public AdminLoginPOM(WebDriver driver) {
        super(driver);
    }

    /**
     * Opens the admin login page using the already loaded base URL.
     * Relies on BaseWebPage.openSubPage to append the route to the current URL.
     */
    public AdminLoginPOM open() {
        openSubPage(ADMIN_PATH);
        return this;
    }

    public boolean isLoaded() {
        return isElementDisplayed(USERNAME_INPUT)
                && isElementDisplayed(PASSWORD_INPUT)
                && isElementDisplayed(LOGIN_BUTTON);
    }

    public AdminLoginPOM login(String username, String password) {
        waitUntilElementIsDisplayed(USERNAME_INPUT).clear();
        waitUntilElementIsDisplayed(USERNAME_INPUT).sendKeys(username);
        waitUntilElementIsDisplayed(PASSWORD_INPUT).clear();
        waitUntilElementIsDisplayed(PASSWORD_INPUT).sendKeys(password);
        waitUntilElementIsDisplayed(LOGIN_BUTTON).click();
        return this;
    }

    public boolean isErrorVisible() {
        return isElementDisplayed(ERROR_BANNER);
    }

    public String getErrorText() {
        if (!isErrorVisible()) {
            return "";
        }
        try {
            return waitUntilElementIsDisplayed(ERROR_TEXT).getText();
        } catch (Exception e) {
            // If paragraph doesn't exist, get text from alert directly
            return waitUntilElementIsDisplayed(ERROR_BANNER).getText();
        }
    }

    /**
     * Checks if login was successful by verifying the login form is no longer visible.
     * After successful login, the form typically disappears or redirects to a different page.
     * Waits until the username input field is dismissed to confirm successful login.
     */
    public boolean isLoginSuccessful() {
        // Wait until the login form (username field) is dismissed
        // If it disappears, login was successful
        return waitUntilElementIsDismissed(USERNAME_INPUT);
    }

    /**
     * Checks if the user is still on the login page (login form is visible).
     */
    public boolean isStillOnLoginPage() {
        return isElementDisplayed(USERNAME_INPUT) 
                && isElementDisplayed(PASSWORD_INPUT) 
                && isElementDisplayed(LOGIN_BUTTON);
    }
}
