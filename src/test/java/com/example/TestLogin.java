import org.junit.jupiter.api.*;
import org.openqa.selenium.*;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.net.URL;
import java.time.Duration;

public class TestLogin {

    private WebDriver driver;

    @BeforeEach
    public void setUp() throws Exception {
        System.out.println("Setting up the test environment...");

        // Set the URL of the Selenium Grid Hub
        String seleniumGridUrl = System.getenv("SELENIUM_GRID_URL"); // URL from Jenkins environment variable

        // Initialize ChromeOptions
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--headless"); // Run in headless mode
        options.addArguments("--disable-gpu"); // Disable GPU (optional)
        options.addArguments("--no-sandbox"); // For Linux environments
        options.addArguments("--remote-debugging-port=9222");
        options.addArguments("--disable-dev-shm-usage");  // This can help on low-memory environments.
        options.addArguments("--remote-allow-origins=*"); // Prevent connection issues with newer Chrome versions

        // Initialize RemoteWebDriver to connect to Selenium Grid Hub
        driver = new RemoteWebDriver(new URL(seleniumGridUrl), options);

        // Set implicit wait time
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));

        System.out.println("Test environment setup complete.");
    }

    @Test
    public void testLogin() {
        System.out.println("Starting testLogin...");

        // Load the login page
        System.out.println("Loading the login page...");
        driver.get("file:///var/lib/jenkins/workspace/test/login.html");

        // Find and fill the username field
        System.out.println("Entering username...");
        WebElement usernameField = driver.findElement(By.id("username"));
        usernameField.sendKeys("testuser");

        // Find and fill the password field
        System.out.println("Entering password...");
        WebElement passwordField = driver.findElement(By.id("password"));
        passwordField.sendKeys("password123");

        // Find and click the login button
        System.out.println("Clicking the login button...");
        WebElement loginButton = driver.findElement(By.id("loginButton"));
        loginButton.click();

        // Wait for the welcome message to become visible
        System.out.println("Waiting for the welcome message to appear...");
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(15));
        WebElement welcomeMessage = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("welcomeMessage")));

        // Assert that the welcome message is displayed
        System.out.println("Asserting that the welcome message is displayed...");
        Assertions.assertTrue(welcomeMessage.isDisplayed(), "Welcome message should be displayed after login.");

        System.out.println("testLogin completed successfully!");
    }

    @AfterEach
    public void tearDown() {
        System.out.println("Tearing down the test environment...");
        // Close the browser
        if (driver != null) {
            driver.quit();
        }
        System.out.println("Test environment cleanup complete.");
    }
}
