package ru.netology.web;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class OrderCardTest {
    private WebDriver driver;
    private WebDriverWait wait;
    private String baseUrl = "http://localhost:9999";

    @BeforeAll
    public static void setupAll() {
        WebDriverManager.chromedriver().setup();
    }

    @BeforeEach
    public void setUp() {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--disable-dev-shm-usage");
        options.addArguments("--no-sandbox");
        options.addArguments("--headless");
        options.addArguments("--remote-allow-origins=*");

        driver = new ChromeDriver(options);
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    @AfterEach
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }

    @Test
    public void shouldSuccessfulSubmitValidData() {
        driver.get(baseUrl);

        WebElement nameField = driver.findElement(By.cssSelector("[data-test-id='name'] input"));
        nameField.sendKeys("Иван Петров-Сидоров");

        WebElement phoneField = driver.findElement(By.cssSelector("[data-test-id='phone'] input"));
        phoneField.sendKeys("+79261234567");

        WebElement checkbox = driver.findElement(By.cssSelector("[data-test-id='agreement'] .checkbox__box"));
        checkbox.click();

        WebElement submitButton = driver.findElement(By.cssSelector("[data-test-id='submit'] button"));
        submitButton.click();

        wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("[data-test-id='order-success']")));

        WebElement successMessage = driver.findElement(By.cssSelector("[data-test-id='order-success']"));
        assertTrue(successMessage.isDisplayed());
        assertEquals("Ваша заявка успешно отправлена! Наш менеджер свяжется с вами в ближайшее время.",
                successMessage.getText());
    }

    @Test
    public void shouldShowErrorWhenNameIsEmpty() {
        driver.get(baseUrl);

        WebElement phoneField = driver.findElement(By.cssSelector("[data-test-id='phone'] input"));
        phoneField.sendKeys("+79261234567");

        WebElement checkbox = driver.findElement(By.cssSelector("[data-test-id='agreement'] .checkbox__box"));
        checkbox.click();

        WebElement submitButton = driver.findElement(By.cssSelector("[data-test-id='submit'] button"));
        submitButton.click();

        wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("[data-test-id='name'] .input__sub")));

        WebElement errorMessage = driver.findElement(By.cssSelector("[data-test-id='name'] .input__sub"));
        assertTrue(errorMessage.isDisplayed());
        assertEquals("Поле обязательно для заполнения", errorMessage.getText());
    }

    @Test
    public void shouldShowErrorWhenPhoneIsEmpty() {
        driver.get(baseUrl);

        WebElement nameField = driver.findElement(By.cssSelector("[data-test-id='name'] input"));
        nameField.sendKeys("Иван Петров");

        WebElement checkbox = driver.findElement(By.cssSelector("[data-test-id='agreement'] .checkbox__box"));
        checkbox.click();

        WebElement submitButton = driver.findElement(By.cssSelector("[data-test-id='submit'] button"));
        submitButton.click();

        wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("[data-test-id='phone'] .input__sub")));

        WebElement errorMessage = driver.findElement(By.cssSelector("[data-test-id='phone'] .input__sub"));
        assertTrue(errorMessage.isDisplayed());
        assertEquals("Поле обязательно для заполнения", errorMessage.getText());
    }

    @Test
    public void shouldShowErrorWhenCheckboxNotChecked() {
        driver.get(baseUrl);

        WebElement nameField = driver.findElement(By.cssSelector("[data-test-id='name'] input"));
        nameField.sendKeys("Иван Петров");

        WebElement phoneField = driver.findElement(By.cssSelector("[data-test-id='phone'] input"));
        phoneField.sendKeys("+79261234567");

        WebElement submitButton = driver.findElement(By.cssSelector("[data-test-id='submit'] button"));
        submitButton.click();

        wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("[data-test-id='agreement'] .checkbox__text")));

        WebElement checkboxError = driver.findElement(By.cssSelector("[data-test-id='agreement'] .checkbox__text"));
        assertTrue(checkboxError.isDisplayed());
    }

    @Test
    public void shouldShowErrorForInvalidName() {
        driver.get(baseUrl);

        WebElement nameField = driver.findElement(By.cssSelector("[data-test-id='name'] input"));
        nameField.sendKeys("John Doe");

        WebElement phoneField = driver.findElement(By.cssSelector("[data-test-id='phone'] input"));
        phoneField.sendKeys("+79261234567");

        WebElement checkbox = driver.findElement(By.cssSelector("[data-test-id='agreement'] .checkbox__box"));
        checkbox.click();

        WebElement submitButton = driver.findElement(By.cssSelector("[data-test-id='submit'] button"));
        submitButton.click();

        wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("[data-test-id='name'] .input__sub")));

        WebElement errorMessage = driver.findElement(By.cssSelector("[data-test-id='name'] .input__sub"));
        assertTrue(errorMessage.isDisplayed());
        assertTrue(errorMessage.getText().contains("Разрешается использовать только русские буквы, пробелы и дефисы"));
    }

    @Test
    public void shouldShowErrorForInvalidPhone() {
        driver.get(baseUrl);

        WebElement nameField = driver.findElement(By.cssSelector("[data-test-id='name'] input"));
        nameField.sendKeys("Иван Петров");

        WebElement phoneField = driver.findElement(By.cssSelector("[data-test-id='phone'] input"));
        phoneField.sendKeys("89261234567");

        WebElement checkbox = driver.findElement(By.cssSelector("[data-test-id='agreement'] .checkbox__box"));
        checkbox.click();

        WebElement submitButton = driver.findElement(By.cssSelector("[data-test-id='submit'] button"));
        submitButton.click();

        wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("[data-test-id='phone'] .input__sub")));

        WebElement errorMessage = driver.findElement(By.cssSelector("[data-test-id='phone'] .input__sub"));
        assertTrue(errorMessage.isDisplayed());
        assertTrue(errorMessage.getText().contains("Телефон должен соответствовать формату"));
    }

    @Test
    public void shouldShowErrorWhenPhoneTooShort() {
        driver.get(baseUrl);

        WebElement nameField = driver.findElement(By.cssSelector("[data-test-id='name'] input"));
        nameField.sendKeys("Иван Петров");

        WebElement phoneField = driver.findElement(By.cssSelector("[data-test-id='phone'] input"));
        phoneField.sendKeys("+792612");

        WebElement checkbox = driver.findElement(By.cssSelector("[data-test-id='agreement'] .checkbox__box"));
        checkbox.click();

        WebElement submitButton = driver.findElement(By.cssSelector("[data-test-id='submit'] button"));
        submitButton.click();

        wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("[data-test-id='phone'] .input__sub")));

        WebElement errorMessage = driver.findElement(By.cssSelector("[data-test-id='phone'] .input__sub"));
        assertTrue(errorMessage.isDisplayed());
    }
}