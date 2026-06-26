package ru.netology;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.*;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.*;

public class OrderTest {
    private static WebDriver driver;
    private WebDriverWait wait;
    
    @BeforeAll
    public static void setupAll() {
        WebDriverManager.chromedriver().setup();
    }
    
    @BeforeEach
    public void setup() {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--disable-dev-shm-usage");
        options.addArguments("--no-sandbox");
        options.addArguments("--headless");
        
        driver = new ChromeDriver(options);
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }
    
    @AfterEach
    public void teardown() {
        if (driver != null) {
            driver.quit();
        }
    }
    
    @Test
    @DisplayName("Should successfully create order with valid data")
    public void shouldCreateOrderWithValidData() {
        driver.get("http://localhost:8080");
        
        WebElement nameInput = wait.until(ExpectedConditions.elementToBeClickable(
            By.cssSelector("[data-test-id='name'] input")));
        nameInput.sendKeys("Иван Петров");
        
        WebElement phoneInput = driver.findElement(By.cssSelector("[data-test-id='phone'] input"));
        phoneInput.sendKeys("+79261234567");
        
        WebElement continueButton = driver.findElement(By.cssSelector("[data-test-id='continue']"));
        continueButton.click();
        
        WebElement addressInput = wait.until(ExpectedConditions.elementToBeClickable(
            By.cssSelector("[data-test-id='address'] input")));
        addressInput.sendKeys("г. Москва, ул. Тестовая, д. 1");
        
        WebElement dateInput = driver.findElement(By.cssSelector("[data-test-id='date'] input"));
        dateInput.clear();
        dateInput.sendKeys("25.12.2025");
        
        WebElement orderButton = driver.findElement(By.cssSelector("[data-test-id='order']"));
        orderButton.click();
        
        WebElement successMessage = wait.until(ExpectedConditions.visibilityOfElementLocated(
            By.cssSelector("[data-test-id='success']")));
        assertTrue(successMessage.isDisplayed());
    }
    
    @Test
    @DisplayName("Should show validation error for empty name")
    public void shouldShowErrorForEmptyName() {
        driver.get("http://localhost:8080");
        
        WebElement phoneInput = driver.findElement(By.cssSelector("[data-test-id='phone'] input"));
        phoneInput.sendKeys("+79261234567");
        
        WebElement continueButton = driver.findElement(By.cssSelector("[data-test-id='continue']"));
        continueButton.click();
        
        WebElement errorMessage = wait.until(ExpectedConditions.visibilityOfElementLocated(
            By.cssSelector("[data-test-id='name'] .input__sub, [data-test-id='name'] .input_invalid")));
        
        assertTrue(errorMessage.isDisplayed());
    }
    
    @Test
    @DisplayName("Should show validation error for invalid phone")
    public void shouldShowErrorForInvalidPhone() {
        driver.get("http://localhost:8080");
        
        WebElement nameInput = driver.findElement(By.cssSelector("[data-test-id='name'] input"));
        nameInput.sendKeys("Иван Петров");
        
        WebElement phoneInput = driver.findElement(By.cssSelector("[data-test-id='phone'] input"));
        phoneInput.sendKeys("123");
        
        WebElement continueButton = driver.findElement(By.cssSelector("[data-test-id='continue']"));
        continueButton.click();
        
        WebElement errorMessage = wait.until(ExpectedConditions.visibilityOfElementLocated(
            By.cssSelector("[data-test-id='phone'] .input__sub, [data-test-id='phone'] .input_invalid")));
        
        assertTrue(errorMessage.isDisplayed());
    }
    
    @Test
    @DisplayName("Should show validation error for empty address")
    public void shouldShowErrorForEmptyAddress() {
        driver.get("http://localhost:8080");
        
        WebElement nameInput = wait.until(ExpectedConditions.elementToBeClickable(
            By.cssSelector("[data-test-id='name'] input")));
        nameInput.sendKeys("Иван Петров");
        
        WebElement phoneInput = driver.findElement(By.cssSelector("[data-test-id='phone'] input"));
        phoneInput.sendKeys("+79261234567");
        
        WebElement continueButton = driver.findElement(By.cssSelector("[data-test-id='continue']"));
        continueButton.click();
        
        WebElement dateInput = wait.until(ExpectedConditions.elementToBeClickable(
            By.cssSelector("[data-test-id='date'] input")));
        dateInput.clear();
        dateInput.sendKeys("25.12.2025");
        
        WebElement orderButton = driver.findElement(By.cssSelector("[data-test-id='order']"));
        orderButton.click();
        
        WebElement errorMessage = wait.until(ExpectedConditions.visibilityOfElementLocated(
            By.cssSelector("[data-test-id='address'] .input__sub, [data-test-id='address'] .input_invalid")));
        
        assertTrue(errorMessage.isDisplayed());
    }
    
    @Test
    @DisplayName("Should show validation error for past date")
    public void shouldShowErrorForPastDate() {
        driver.get("http://localhost:8080");
        
        WebElement nameInput = wait.until(ExpectedConditions.elementToBeClickable(
            By.cssSelector("[data-test-id='name'] input")));
        nameInput.sendKeys("Иван Петров");
        
        WebElement phoneInput = driver.findElement(By.cssSelector("[data-test-id='phone'] input"));
        phoneInput.sendKeys("+79261234567");
        
        WebElement continueButton = driver.findElement(By.cssSelector("[data-test-id='continue']"));
        continueButton.click();
        
        WebElement addressInput = wait.until(ExpectedConditions.elementToBeClickable(
            By.cssSelector("[data-test-id='address'] input")));
        addressInput.sendKeys("г. Москва, ул. Тестовая, д. 1");
        
        WebElement dateInput = driver.findElement(By.cssSelector("[data-test-id='date'] input"));
        dateInput.clear();
        dateInput.sendKeys("01.01.2020");
        
        WebElement orderButton = driver.findElement(By.cssSelector("[data-test-id='order']"));
        orderButton.click();
        
        WebElement errorMessage = wait.until(ExpectedConditions.visibilityOfElementLocated(
            By.cssSelector("[data-test-id='date'] .input__sub, [data-test-id='date'] .input_invalid")));
        
        assertTrue(errorMessage.isDisplayed());
    }
}
