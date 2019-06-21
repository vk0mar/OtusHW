import io.github.bonigarcia.wdm.WebDriverManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Instant;
import java.util.Date;
import java.util.concurrent.TimeUnit;


public class TestOtus {
    private static final String user = "admin";
    private static final String pass = "verysecretadminpassword";

    private WebDriver driver;
    private String href = "http://192.168.99.100"; //testlink with docker for win 10
    private static final Logger logger = LogManager.getLogger(TestOtus.class);
    private WebDriverWait wait;

    @Before
    public void setUp() throws Exception {
        logger.info("Before");
        WebDriverManager.chromedriver().setup();
        ChromeOptions options = new ChromeOptions();
        driver = new ChromeDriver(options);
        driver.manage().window().maximize();
    }

    @Test
    public void test101() throws InterruptedException {
        logger.info("driver.get" + href);
        driver.get(href);
        driver.findElement(By.id("tl_login")).sendKeys(user);
        driver.findElement(By.id("tl_password")).sendKeys(pass);
        driver.findElement(By.xpath("//input[@type='submit']")).click();
        driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS); //неявное
        wait = new WebDriverWait(driver, 5);
        String suite = createSuite();
        createTestCasewith3steps(suite);
        createTestCasewith3steps(suite);

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
