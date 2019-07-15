import helpers.DriverType;
import helpers.WebDriverFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.After;
import org.junit.Before;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.concurrent.TimeUnit;

public class BaseTest {

    String bro;
    DriverType type;
    private static final Logger logger = LogManager.getLogger(BaseTest.class);
    WebDriver driver;
    private WebDriverWait wait;

    protected WebDriver getDriver() {
        return driver;
    }

    @Before
    public void setUp() {

        bro = System.getProperty("browser", "CHROME").toUpperCase();
        type = DriverType.valueOf(bro);
        logger.info("Before {} browser", bro); //mvn clean test -Dbrowser='cHrOmE'

        driver = WebDriverFactory.create(type);
        driver.manage().timeouts().implicitlyWait(4, TimeUnit.SECONDS); //неявное
        wait = new WebDriverWait(driver, 4, 125);
    }

    @After
    public void tearDown() {
        logger.info("Quit");
        driver.manage().deleteAllCookies();
        if (null != driver) {
            driver.quit();
            driver = null;
        }
    }
}
