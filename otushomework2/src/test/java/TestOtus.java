import io.github.bonigarcia.wdm.WebDriverManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import java.util.List;

public class TestOtus {
    private WebDriver driver;
    private String href = "https://otus.ru/";
    private static final Logger logger = LogManager.getLogger(TestOtus.class);

    @Before
    public void setUp() throws Exception {
        logger.info("Before");
        WebDriverManager.chromedriver().setup();
        ChromeOptions options = new ChromeOptions();
        driver = new ChromeDriver(options);
    }

    @Test
    public void test101() {
        logger.info("driver.get" + href);
        driver.get(href);
    }


    @After
    public void tearDown() {
        logger.info("Quit");
        driver.quit();
    }


}
