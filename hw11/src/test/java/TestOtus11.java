import au.com.bytecode.opencsv.CSVWriter;
import net.lightbody.bmp.core.har.Har;

import net.lightbody.bmp.core.har.HarEntry;
import net.lightbody.bmp.proxy.ProxyServer;
import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import org.openqa.selenium.*;

import org.openqa.selenium.Proxy;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static org.openqa.selenium.support.ui.ExpectedConditions.*;


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
    public void setUp() throws Exception {

        bro = System.getProperty("browser", "CHROME").toUpperCase();
        type = DriverType.valueOf(bro);
        logger.info("Before {} browser", bro); //mvn clean test -Dbrowser='cHrOmE'

        driver = WebDriverFactory.create(type);
        driver.manage().timeouts().implicitlyWait(4, TimeUnit.SECONDS); //неявное
        wait = new WebDriverWait(driver, 4, 125);
    }

    private void iWantScreenShot(){
        File file = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
        saveFile(file);
    }

    private void saveFile(File file) {
        String fileName = "target/" + System.currentTimeMillis() + ".png";
        try {
            FileUtils.copyFile(file, new File(fileName));
        } catch (IOException e) {
            logger.error(e);
        }
    }

    @Test
    public void letsGo() throws InterruptedException {
        OtusStartPage osp = new OtusStartPage(driver);
        OtusAuthStartPage oasp = osp.login();
        OtusCabinetPersonal ocp = oasp.getAbout();
        ocp.send();
    }

    @Test
    public void verifyData(){
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
    public void tearDown() throws Exception {
        logger.info("Quit");
        //driver.manage().deleteAllCookies();
        if (null != driver) {
            driver.quit();
            driver = null;
        }
    }
}
