import au.com.bytecode.opencsv.CSVWriter;
import net.lightbody.bmp.core.har.Har;

import net.lightbody.bmp.core.har.HarEntry;
import net.lightbody.bmp.proxy.ProxyServer;
import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.After;
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


public class TestOtus8 {

    private WebDriver driver;
    private String href = "http://ya.ru";
    private static final Logger logger = LogManager.getLogger(TestOtus8.class);
    private WebDriverWait wait;
    String bro;
    ProxyServer server;
    DriverType type;
    List<WebElement> cats = new ArrayList<>();
    List<String> catsUrl = new ArrayList<>();
    String csv = "data.csv";
    CSVWriter writer = new CSVWriter(new FileWriter(csv));

    public TestOtus8() throws IOException {
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


    public void getAllCats(){
        driver.get("https://www.drive2.ru/cars/?sort=selling");
        cats = driver.findElements(By.cssSelector("span.c-makes__item a.c-link.c-link--text"));
        logger.info("cats size \n{}", cats.size());

        for (WebElement cat : cats) { //все категории машин
            catsUrl.add(cat.getAttribute("href"));
        }
    }

    public String safeFindLocator(By locator){
        String s = "*** no found";
        try {
            s = driver.findElement(locator).getText();
        }catch (Exception e)
        {
            logger.info("No such parameter {}", e.getStackTrace());
        }
        return s;
    };

    public void openEachAuto(List<String> cars) throws IOException {
        for (String a : cars) {

            driver.get(a);

            String p1 = safeFindLocator(By.cssSelector("ul.c-car-forsale__info li:nth-child(5)"));
            String p2 = safeFindLocator(By.cssSelector("div.c-car-forsale__price strong"));
            String p3 = safeFindLocator(By.cssSelector("a[data-ym-target='car2brand']"));
            String p4 = safeFindLocator(By.cssSelector("h1.c-car-info__caption"));
            String p5 = safeFindLocator(By.cssSelector("ul.c-car-forsale__info li:nth-child(2)"));

            logger.info("URL    : {}", a);
            logger.info("year   : {}", p1);
            logger.info("price  : {}", p2);
            logger.info("brend  : {}", p3);
            logger.info("model  : {}", p4);
            logger.info("engine : {}", p5);

            String [] record = {p1,p2,p3,p4,p5};
            //в csv-файл (ссылка, год автомобиля, цена, марка, модель, объем двигателя)
            writer.writeNext(record);
            writer.flush();
        }
    }

    public List<String> findCarListAndGet(String s) throws IOException {

            logger.info("get cat \"{}\"", s);
            driver.get(s);

            List<WebElement> cards = new ArrayList<>();
            int currentSize = -1;

            while (currentSize < cards.size()) {
                currentSize = cards.size();
                ((JavascriptExecutor) driver).executeScript("window.scrollBy(0, 5000)");
                try {
                    wait.until(and(
                            invisibilityOfElementLocated(By.cssSelector("button.[data-action='catalog.morecars']")),
                            numberOfElementsToBeMoreThan(By.cssSelector(".c-car-card-sa__caption"), currentSize)
                    ));
                } catch (TimeoutException ex) {
                    logger.info("all cars loaded");
                }
                iWantScreenShot();
                cards = driver.findElements(By.cssSelector(".c-car-card-sa"));
            }
            logger.info("--------------------------\n{}", cards.size());
            List<String> cars = new ArrayList<>();
            for (WebElement c : cards) {
                cars.add(getURL(c)); // список объявлений внутри каждой марки
            }
        return cars;
    }

    @Test
    public void getNames() throws IOException {
        getAllCats();
        for (String s : catsUrl) { // список марок
            openEachAuto(findCarListAndGet(s));
        }
        writer.close();
    }

    public String getCaption(WebElement card) {
        return card.findElement(By.cssSelector(".c-car-card-sa__caption")).getText();
    }

    private String getURL(WebElement card) {
        return card.findElement(By.cssSelector("a.u-link-area")).getAttribute("href");
    }
















    @Test
    public void testProxy() throws Exception {

        // запуск прокси сервера
        server = new ProxyServer(4444);
        //server.autoBasicAuthorization("example.com", "username", "password");
        server.start();

        // получение Selenium proxy
        Proxy proxy = server.seleniumProxy();

        // конфигурация FirefoxDriver для использования прокси
        DesiredCapabilities capabilities = new DesiredCapabilities();
        capabilities.setCapability(CapabilityType.PROXY, proxy);

        //ChromeOptions options = new ChromeOptions();

        driver = WebDriverFactory.create(DriverType.CHROME, capabilities);
        driver.manage().timeouts().implicitlyWait(4, TimeUnit.SECONDS); //неявное
        wait = new WebDriverWait(driver, 4, 125);
        logger.info(((HasCapabilities)driver).getCapabilities());



        // создание HAR с меткой
        server.newHar("tender.pro");

        // открытие страницы
        driver.get("http://www.tender.pro/index.shtml");

        // получение данных HAR
        Har har = server.getHar();

        har.writeTo(new File("f://Test.har"));
        for (HarEntry entry : har.getLog().getEntries()) {

            logger.info(entry.getRequest().getUrl());
            // время ожидания ответа от сервера в миллисекундах
            logger.info(entry.getTimings().getWait());
            // время чтения ответа от сервера в миллисекундах
            logger.info(entry.getTimings().getReceive());
        }

        driver.quit();
        server.stop();

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
