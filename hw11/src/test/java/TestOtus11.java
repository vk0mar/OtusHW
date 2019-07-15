import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.After;
import org.junit.Assert;
import org.junit.Test;
import org.openqa.selenium.By;
import pages.OtusAuthStartPage;
import pages.OtusCabinetPersonal;
import pages.OtusStartPage;


public class TestOtus11 extends BaseTest {


    private static final Logger logger = LogManager.getLogger(TestOtus11.class);


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
}
