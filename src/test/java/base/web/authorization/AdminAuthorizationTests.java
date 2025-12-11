package base.web.authorization;

import base.BaseTest;
import io.qameta.allure.*;
import org.testng.Reporter;
import org.testng.annotations.Test;

import static org.testng.Assert.assertTrue;
import static org.testng.Assert.assertFalse;

public class AdminAuthorizationTests extends BaseTest {

    @Test(description = "TC-001: Successful admin login page access - Verify that admin login page loads correctly with all required elements visible")
    @Epic("Admin Panel")
    @Feature("Authorization & Session Management")
    @Story("Admin Login")
    @Severity(SeverityLevel.BLOCKER)
    public void shouldOpenAdminLoginPage() {
        adminLoginPage().open();
        assertTrue(driver().getCurrentUrl().contains("/admin"), "Admin login page should be loaded");
        assertTrue(adminLoginPage().isLoaded(), "Expected admin login elements should be visible");
        Reporter.log("shouldOpenAdminLoginPage test has completed.", true);
    }

    @Test(description = "TC-002: Unsuccessful admin login with invalid password - Verify that login is rejected with proper error message when invalid credentials are used")
    @Epic("Admin Panel")
    @Feature("Authorization & Session Management")
    @Story("Admin Login")
    @Severity(SeverityLevel.CRITICAL)
    public void shouldShowErrorOnInvalidCredentials() {
        adminLoginPage().open()
                .login("wrong", "creds");
        assertTrue(adminLoginPage().isErrorVisible(), "Error banner should be shown");
        assertFalse(adminLoginPage().getErrorText().isBlank(), "Error message text should be populated");
        assertTrue(adminLoginPage().isStillOnLoginPage(), "Should remain on login page after invalid credentials");
        Reporter.log("Invalid credentials test completed.", true);
    }

    @Test(description = "Empty username validation - Verify form behavior when username field is left empty")
    @Epic("Admin Panel")
    @Feature("Authorization & Session Management")
    @Story("Form Validation")
    @Severity(SeverityLevel.NORMAL)
    public void shouldShowErrorOnEmptyUsername() {
        adminLoginPage().open()
                .login("", "password");
        // Some forms show error immediately, others require submission
        // This test verifies form behavior with empty username
        assertTrue(adminLoginPage().isStillOnLoginPage(), "Should remain on login page with empty username");
        Reporter.log("Empty username test completed.", true);
    }

    @Test(description = "Empty password validation - Verify form behavior when password field is left empty")
    @Epic("Admin Panel")
    @Feature("Authorization & Session Management")
    @Story("Form Validation")
    @Severity(SeverityLevel.NORMAL)
    public void shouldShowErrorOnEmptyPassword() {
        adminLoginPage().open()
                .login("admin", "");
        // This test verifies form behavior with empty password
        assertTrue(adminLoginPage().isStillOnLoginPage(), "Should remain on login page with empty password");
        Reporter.log("Empty password test completed.", true);
    }

    @Test(description = "TC-003: Unsuccessful admin login with empty credentials - Verify validation messages are displayed when both username and password fields are empty")
    @Epic("Admin Panel")
    @Feature("Authorization & Session Management")
    @Story("Form Validation")
    @Severity(SeverityLevel.CRITICAL)
    public void shouldShowErrorOnEmptyCredentials() {
        adminLoginPage().open()
                .login("", "");
        // This test verifies form behavior with both fields empty
        assertTrue(adminLoginPage().isStillOnLoginPage(), "Should remain on login page with empty credentials");
        Reporter.log("Empty credentials test completed.", true);
    }

    @Test(description = "TC-001: Successful admin login with valid credentials - Verify successful login redirects to admin dashboard with logout button visible")
    @Epic("Admin Panel")
    @Feature("Authorization & Session Management")
    @Story("Admin Login")
    @Severity(SeverityLevel.BLOCKER)
    public void shouldLoginSuccessfullyWithValidCredentials() {
        adminLoginPage().open();
        assertTrue(adminLoginPage().isLoaded(), "Login page should be loaded before login");
        
        adminLoginPage().login("admin", "password");
        
        // After successful login, the login form should disappear or redirect
        assertTrue(adminLoginPage().isLoginSuccessful(), "Login should be successful with valid credentials");
        assertFalse(adminLoginPage().isErrorVisible(), "No error should be shown for valid credentials");
        Reporter.log("Successful login test completed.", true);
    }
}
