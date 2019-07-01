import net.lightbody.bmp.core.har.Har;

import net.lightbody.bmp.core.har.HarEntry;
import net.lightbody.bmp.proxy.ProxyServer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;
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
import java.io.IOException;
import java.io.Writer;
import java.util.concurrent.TimeUnit;


public class TestOtus7 {
    private WebDriver driver;
    private String href = "http://ya.ru";
    private static final Logger logger = LogManager.getLogger(TestOtus7.class);
    private WebDriverWait wait;
    String bro = "CHROME";
    ProxyServer server;
    DriverType type;

    @Before
    public void setUp() throws Exception {
        if(System.getProperty("browser")!=null ){bro = System.getProperty("browser").toUpperCase();}
        logger.info("Before " + bro); //mvn clean test -Dbrowser='cHrOmE'
        type = DriverType.valueOf(bro);

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
