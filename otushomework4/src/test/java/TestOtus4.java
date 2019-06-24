import io.github.bonigarcia.wdm.WebDriverManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.awt.*;
import java.time.Instant;
import java.util.Date;
import java.util.concurrent.TimeUnit;


public class TestOtus4 {
    private static final String user = "admin";
    private static final String pass = "verysecretadminpassword";

    private WebDriver driver;
    private String href = "http://192.168.99.100"; //testlink with docker for win 10
    private static final Logger logger = LogManager.getLogger(TestOtus4.class);
    private WebDriverWait wait;

    @Before
    public void setUp() throws Exception {
        logger.info("Before");
        WebDriverManager.chromedriver().setup();
        ChromeOptions options = new ChromeOptions();
        driver = new ChromeDriver(options);
        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS); //неявное
        wait = new WebDriverWait(driver, 5);

    }


    public void authorization(){
        logger.info("driver.get" + href);
        driver.get(href);
        driver.findElement(By.id("tl_login")).sendKeys(user);
        driver.findElement(By.id("tl_password")).sendKeys(pass);
        driver.findElement(By.xpath("//input[@type='submit']")).click();
    }

    @Test
    public void testProjectManagement() throws InterruptedException {
        authorization();
        // на пустом списке

        driver.get("http://192.168.99.100/lib/plan/planView.php");
        driver.findElement(By.xpath("//input[@name='create_testplan']")).click();
        String planname = "Plan "+new Date(System.currentTimeMillis());;
        driver.findElement(By.xpath("//input[@name='testplan_name']")).sendKeys(planname);

        driver.switchTo().frame(driver.findElement(By.className("cke_wysiwyg_frame")));
        WebElement myElement = driver.findElement(By.cssSelector("body"));
        myElement.sendKeys(planname);
        driver.switchTo().defaultContent();

        driver.findElement(By.xpath("//input[@name='is_public']")).click();
        driver.findElement(By.xpath("//input[@name='active']")).click();
        driver.findElement(By.xpath("//input[@name='do_create']")).click();

        Assert.assertEquals(planname, driver.findElement(By.cssSelector("td.sorting_1 a")).getText().trim().substring(0,planname.length()));
        Assert.assertEquals(planname, driver.findElement(By.cssSelector("tr td:nth-child(2)")).getText().trim().substring(0,planname.length()));
        Assert.assertEquals("Public", driver.findElement(By.cssSelector("tr td:nth-child(6) img")).getAttribute("title"));
    }

    @Test
    public void testPassed() throws InterruptedException {
        // Open Execute Tests http://192.168.99.100/lib/general/frmWorkArea.php?feature=executeTest
        // план, тесты и билд должны существовать
        authorization();
        driver.get("http://192.168.99.100/lib/general/frmWorkArea.php?feature=executeTest");
        //Expand and Click Test case
        driver.switchTo().frame("treeframe");
        driver.findElement(By.cssSelector("input#expand_tree")).click();
        Thread.sleep(100); // не успевает на локальном компе
        driver.findElement(By.cssSelector("a span span b")).click();
        driver.switchTo().defaultContent();
        //Click passed #fastExecp_269
        driver.switchTo().frame("workframe");
        driver.findElement(By.cssSelector("img#fastExecp_269")).click();
        driver.findElement(By.cssSelector("input#toggle_history_on_off")).click();
        // проверить зеленый цвет
        //rgba(0, 100, 0, 1) == #006400
        Assert.assertTrue("rgba(0, 100, 0, 1)".equals(driver.findElement(By.cssSelector("div.passed")).getCssValue("background-color")));
        //Click failed #fastExecf_269
        driver.findElement(By.cssSelector("img#fastExecf_269")).click();
        driver.findElement(By.cssSelector("input#toggle_history_on_off")).click();
        //проверить красный цвет
        //rgba(178, 34, 34, 1) == #b22222
        Assert.assertTrue("rgba(178, 34, 34, 1)".equals(driver.findElement(By.cssSelector("div.failed")).getCssValue("background-color")));
    }


    public String createSuite() throws InterruptedException {
        String suiteName = "Test Suite "+new Date(System.currentTimeMillis());
        driver.get("http://192.168.99.100/lib/general/frmWorkArea.php?feature=editTc");
        driver.switchTo().frame("workframe");
        //table = WebDriverWait(self.driver, 5).until(expected_conditions.visibility_of_element_located((By.CSS_SELECTOR, '#items-grid > table > tbody')))
        driver.findElement(By.xpath("//img[@title='Actions']")).click();
        driver.findElement(By.cssSelector("input#new_testsuite")).click();
        driver.findElement(By.cssSelector("input#name")).sendKeys(suiteName );
        driver.findElement(By.xpath("//input[@type='submit']")).click();

        logger.info("TestSuite created \"" + suiteName + "\"");
        return suiteName;
    }

    public void createTestCasewith3steps(String suiteName) throws InterruptedException {
        String casename = "Test Case " + Instant.now().getNano();
        logger.info("TestCase for \"" + suiteName + "\"");
        driver.switchTo().defaultContent();
        driver.switchTo().frame("treeframe");
        driver.findElement(By.partialLinkText(suiteName)).click();
        driver.switchTo().defaultContent();
        driver.switchTo().frame("workframe");
        driver.findElement(By.xpath("//img[@title='Actions']")).click();
        driver.findElement(By.cssSelector("input#create_tc")).click();
        driver.findElement(By.cssSelector("input#testcase_name")).sendKeys(casename);
        driver.findElement(By.cssSelector("input#do_create_button_2")).click();
        logger.info("Test Case created \"" + casename + "\"");

        driver.findElement(By.xpath("//input[@name='create_step']")).click();

        for(int i=0;i<3;i++) {
            driver.findElement(By.cssSelector("input#do_update_step")).click();
            logger.info("Test Case step created \"" + casename + "\"");
            Thread.sleep(100);
        }
    }

    @After
    public void tearDown() {
        logger.info("Quit");
        driver.quit();
    }
}
