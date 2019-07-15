package pages;

import helpers.BaseHelper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import static java.util.concurrent.TimeUnit.SECONDS;


public class OtusStartPage {
    private WebDriver driver;
    private static final Logger logger = LogManager.getLogger(OtusStartPage.class);
    WebDriverWait wait;
    String login;
    String pass;


    public OtusStartPage(WebDriver driver) {
        this.driver = driver;
        driver.manage().timeouts().implicitlyWait(4, SECONDS);
        wait = new WebDriverWait(driver, 4, 125);
        driver.get("https://otus.ru");
        logger.info("URL: {}", driver.getCurrentUrl());
    }

    private By startAuthButton = By.xpath("//button[@data-modal-id='new-log-reg']");
    private By emailField = By.cssSelector("form.new-log-reg__form.js-login input[name='email']");
    private By passwordField = By.cssSelector("form.new-log-reg__form.js-login input[name='password']");
    private By authButton = By.cssSelector("form.new-log-reg__form.js-login button[type='submit']");

    public OtusAuthStartPage login() {
        login = System.getProperty("login", "seekme@list.ru");
        pass = System.getProperty("pass", "qazqaz123!");

        driver.findElement(startAuthButton).click();
        BaseHelper.sendData(driver, emailField, login);
        BaseHelper.sendData(driver, passwordField, pass);
        driver.findElement(authButton).click();
        wait.until(ExpectedConditions.numberOfElementsToBeMoreThan(By.cssSelector("div.header2-menu__icon-img.ic-blog-default-avatar"), 0));

        return new OtusAuthStartPage(driver);
    }
}
