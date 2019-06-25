import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import org.openqa.selenium.*;

import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.concurrent.TimeUnit;


public class TestOtus5 {
    private WebDriver driver;
    private String href = "http://ya.ru";
    private static final Logger logger = LogManager.getLogger(TestOtus5.class);
    private WebDriverWait wait;
    String bro = "CHROME";

    @Before
    public void setUp() throws Exception {
        if(System.getProperty("browser")!=null ){bro = System.getProperty("browser").toUpperCase();}
        logger.info("Before " + bro); //mvn clean test -Dbrowser='cHrOmE'

        DriverType type = DriverType.valueOf(bro);

        driver = WebDriverFactory.create(type);
        System.out.println(((HasCapabilities)driver).getCapabilities());
        driver.quit();

        ChromeOptions options = new ChromeOptions();
        driver = WebDriverFactory.create(DriverType.CHROME, options);
        System.out.println(((HasCapabilities)driver).getCapabilities());

        driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS); //неявное
        wait = new WebDriverWait(driver, 5);
    }

    @Test
    public void getPage(){
        logger.info("driver.get " + href);
        driver.get(href);

    }

    @After
    public void tearDown() {
        logger.info("Quit");
        if (null != driver) {
            driver.quit();
            driver = null;
        }
    }
}
