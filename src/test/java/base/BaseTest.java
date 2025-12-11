package base;

import com.automationdemo.helpers.DataHelper;
import com.automationdemo.helpers.factories.WebDriversFactory;
import com.automationdemo.models.Configs;
import com.automationdemo.pages.adminpanel.AdminLoginPOM;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.openqa.selenium.WebDriver;

import java.util.logging.Logger;

public abstract class BaseTest {
    protected static final ThreadLocal<Configs> CONFIGS = new ThreadLocal<>();
    protected static final ThreadLocal<WebDriver> DRIVER = new ThreadLocal<>();
    private static final ThreadLocal<AdminLoginPOM> ADMIN_LOGIN_POM = new ThreadLocal<>();
    
    static {
        // Suppress Selenium CDP warnings at class load time
        Logger.getLogger("org.openqa.selenium.devtools.CdpVersionFinder").setLevel(java.util.logging.Level.SEVERE);
        Logger.getLogger("org.openqa.selenium.chromium.ChromiumDriver").setLevel(java.util.logging.Level.SEVERE);
    }

    @BeforeMethod
    public void setUp() {
        CONFIGS.set(new DataHelper().loadConfigs());
        DRIVER.set(new WebDriversFactory(CONFIGS.get()).createWebDriver());
        driver().get(configs().getBaseUrl());
        ADMIN_LOGIN_POM.set(new AdminLoginPOM(driver()));
    }

    @AfterMethod
    public void tearDown() {
        if (driver() != null) {
            driver().quit();
        }
        ADMIN_LOGIN_POM.remove();
        DRIVER.remove();
        CONFIGS.remove();
    }

    protected WebDriver driver() {
        return DRIVER.get();
    }

    protected Configs configs() {
        return CONFIGS.get();
    }

    protected AdminLoginPOM adminLoginPage() {
        return ADMIN_LOGIN_POM.get();
    }
}
