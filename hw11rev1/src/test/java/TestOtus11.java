import helpers.DriverType;
import helpers.WebDriverFactory;
import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import pages.OtusAuthStartPage;
import pages.OtusCabinetPersonal;
import pages.OtusStartPage;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;


public class TestOtus11 {

    private WebDriver driver;
    private String href;
    private static final Logger logger = LogManager.getLogger(TestOtus11.class);
    private WebDriverWait wait;
    String bro;
    DriverType type;

    public TestOtus11() throws IOException {
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


    @Test
    public void letsGo() {
        OtusStartPage osp = new OtusStartPage(driver);
        OtusAuthStartPage oasp = osp.login();
        OtusCabinetPersonal ocp = oasp.getAbout();
        ocp.send();
    }

    @Test
    public void verifyData() {
        OtusStartPage osp = new OtusStartPage(driver);
        OtusAuthStartPage oasp = osp.login();
        OtusCabinetPersonal ocp = oasp.getAbout();
        Assert.assertTrue(driver.findElement(By.cssSelector("input[name=fname]")).getAttribute("value").equals("Имя"));
        Assert.assertTrue(driver.findElement(By.cssSelector("input[name=fname_latin]")).getAttribute("value").equals("Name"));
        Assert.assertTrue(driver.findElement(By.cssSelector("input[name=lname]")).getAttribute("value").equals("Фамилия"));
        Assert.assertTrue(driver.findElement(By.cssSelector("input[name=lname_latin]")).getAttribute("value").equals("Family"));
        Assert.assertTrue(driver.findElement(By.cssSelector("input[name=blog_name]")).getAttribute("value").equals("NickName"));
        Assert.assertTrue(driver.findElement(By.cssSelector("input[name=date_of_birth]")).getAttribute("value").equals("29.03.1999"));
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
