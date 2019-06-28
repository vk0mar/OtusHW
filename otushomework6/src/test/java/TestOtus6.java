import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import org.openqa.selenium.*;

import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.interactions.Coordinates;
import org.openqa.selenium.interactions.Locatable;
import org.openqa.selenium.remote.RemoteWebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.ArrayList;
import java.util.Set;
import java.util.concurrent.TimeUnit;


public class TestOtus6 {
    private WebDriver driver;
    private String href = "http://ya.ru";
    private static final Logger logger = LogManager.getLogger(TestOtus6.class);
    private WebDriverWait wait;
    String bro = "CHROME";

    @Before
    public void setUp() throws Exception {
        if(System.getProperty("browser")!=null ){bro = System.getProperty("browser").toUpperCase();}
        logger.info("Before " + bro); //mvn clean test -Dbrowser='cHrOmE'

        DriverType type = DriverType.valueOf(bro);

        ChromeOptions options = new ChromeOptions();
        driver = WebDriverFactory.create(DriverType.CHROME, options);
        System.out.println(((HasCapabilities)driver).getCapabilities());

        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(4, TimeUnit.SECONDS); //неявное
        wait = new WebDriverWait(driver, 4, 125);
    }


    private void waitWrapper(String locator, String before){
        wait.until(d-> { if (d.findElement(By.cssSelector(locator)).getAttribute("data-reqid-chain") != before)
            return true;  else return false; }  );
    }

    private void waifForPreloader(By locator, int n){
        ExpectedConditions.numberOfElementsToBe(locator, n);
    }

    private void waifForPreloader2(By locator, int n){
        wait.until(ExpectedConditions.numberOfElementsToBe(locator, n));
    }


    private void waitForClicable(WebElement e){
        //wait.until(ExpectedConditions.elementToBeClickable(By.xpath("(//div[@id='brandSlider']/div[1]/div/div/div/img)[50]")))
    }
    public void mouseOver(WebElement element) {
        String code = "var fireOnThis = arguments[0];"
                + "var evObj = document.createEvent('MouseEvents');"
                + "evObj.initEvent( 'mouseover', true, true );"
                + "fireOnThis.dispatchEvent(evObj);";
        ((JavascriptExecutor)driver).executeScript(code, element);
    }

    public static boolean scroll_Page(WebElement webelement, int scrollPoints, WebDriver driver)
    {
        try
        {
            Actions dragger = new Actions(driver);
            // drag downwards
            int numberOfPixelsToDragTheScrollbarDown = 10;
            for (int i = 10; i < scrollPoints; i = i + numberOfPixelsToDragTheScrollbarDown)
            {
                dragger.moveToElement(webelement).clickAndHold().moveByOffset(0, numberOfPixelsToDragTheScrollbarDown).release(webelement).build().perform();
            }
            Thread.sleep(500);
            return true;
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return false;
        }
    }

    @Test
    public void yandexMarket() throws InterruptedException {
        driver.get("https://yandex.ru");
        //driver.manage().addCookie(new Cookie("spravka", "dD0xNTYxNjIxOTk5O2k9MTg4LjIzMy4wLjkwO3U9MTU2MTYyMTk5OTY2MjU3NTMyNztoPTBmZmViODZmZDMwZDdkNjNjNGRkNGU5ZTUwOGFjNzk5"));
        //driver.manage().addCookie(new Cookie("currentRegionId", "38"));
        //driver.manage().addCookie(new Cookie("currentRegionName", "%D0%92%D0%BE%D0%BB%D0%B3%D0%BE%D0%B3%D1%80%D0%B0%D0%B4"));

//        - Открыть в Chrome сайт Яндекс.Маркет - раздел "Мобильные телефоны"
        driver.get("https://market.yandex.ru/catalog--mobilnye-telefony/54726/list");

        // потушить выбор города
        WebElement e2 =driver.findElement(By.cssSelector(".button2_theme_action.button2_width_max.n-region-notification__actions-btn"));
        if(e2.isEnabled()) {
            e2.click();
            waifForPreloader2(By.cssSelector(".preloadable__preloader"), 0);
        }

        //driver.execute_script('scrollBy(0, 250)');
        //Keys.PAGE_DOWN;
        //scroll_Page(driver.findElement(By.cssSelector("span.NVoaOvqe58")), 100, driver);


//        - Отфильтровать список товаров по производителю: RedMi и Xiaomi
        logger.info("filter Xiaomi");
        String s = driver.findElement(By.cssSelector("body")).getAttribute("data-reqid-chain");
        e2 = driver.findElement(By.cssSelector("a[href='/catalog/54726/list?hid=91491&glfilter=7893318%3A7701962']"));
        //new Actions(driver).moveToElement(e2).moveByOffset(0, 2000).build().perform();
        Coordinates cor=((Locatable)e2).getCoordinates();
        cor.inViewPort();

        driver.findElement(By.cssSelector("a[href='/catalog/54726/list?hid=91491&glfilter=7893318%3A7701962']")).click();
        waitWrapper("body", s);
        waifForPreloader(By.cssSelector(".preloadable__preloader"), 0);

//        - Отфильтровать список товаров по производителю: RedMi и Xiaomi
        logger.info("filter RedMi");
        s = driver.findElement(By.cssSelector("body")).getAttribute("data-reqid-chain");
        driver.findElement(By.xpath("//label[@for='12782797_14357428']")).click();
        waitWrapper("body", s);
        waifForPreloader(By.cssSelector(".preloadable__preloader"), 0);


//        - Отсортировать список товаров по цене (от меньшей к большей)
        logger.info("filter по цене");
        driver.findElement(By.linkText("по цене")).click();
        waifForPreloader2(By.cssSelector(".preloadable__preloader"), 0);


// навести курсор на товар и кликнуть на добавление к сравнению
        //WebElement tovar1 = driver.findElements(By.cssSelector("i.image_name_compare")).get(0);
        WebElement tovar1 = driver.findElements(By.cssSelector(".n-user-lists_type_compare")).get(0);
        new Actions(driver).moveToElement(tovar1).build().perform();
//        - Добавить первый в списке RedMi
        tovar1.click();
        // -- Проверить, что отобразилась плашка "Товар {имя товара} добавлен к сравнению"
        wait.until(ExpectedConditions.numberOfElementsToBe(By.cssSelector(".popup-informer__pane"), 1)); // дождаться
        logger.info(driver.findElement(By.cssSelector("div.popup-informer__title")).getAttribute("innerHTML"));

//        new Actions(driver).moveToElement(driver.findElement(By.cssSelector(".popup-informer__close"))).build().perform();
//        driver.findElement(By.cssSelector(".popup-informer__close")).click();

        WebElement element = driver.findElement(By.cssSelector(".popup-informer__close"));
        JavascriptExecutor executor = (JavascriptExecutor)driver;
        executor.executeScript("arguments[0].click();", element);
        wait.until(ExpectedConditions.numberOfElementsToBe(By.cssSelector(".popup-informer__pane"), 0)); // скрыть алерт

        //WebElement tovar2 = driver.findElements(By.cssSelector("i.image_name_compare")).get(1);
        WebElement tovar2 = driver.findElements(By.cssSelector(".n-user-lists_type_compare")).get(1);

//        - Добавить первый в списке Xiaomi
        new Actions(driver).moveToElement(tovar2).build().perform();
        tovar2.click();
        wait.until(ExpectedConditions.numberOfElementsToBe(By.cssSelector(".popup-informer__pane"), 1)); // дождаться
        logger.info(driver.findElement(By.cssSelector("div.popup-informer__title")).getAttribute("innerHTML"));

        element = driver.findElement(By.cssSelector(".popup-informer__close"));
        executor = (JavascriptExecutor)driver;
        executor.executeScript("arguments[0].click();", element);
        wait.until(ExpectedConditions.numberOfElementsToBe(By.cssSelector(".popup-informer__pane"), 0)); // скрыть алерт

//        driver.findElement(By.cssSelector(".popup-informer__close")).click();
//        wait.until(ExpectedConditions.numberOfElementsToBe(By.cssSelector(".popup-informer__pane"), 0)); // скрыть алерт

        waifForPreloader2(By.cssSelector(".preloadable__preloader"), 0);


        //-- Проверить, что отобразилась плашка "Товар {имя товара} добавлен к сравнению"




        // Открыть страницу сравнения
        //driver.findElement(By.cssSelector("a.button.button_size_m.button_theme_normal.i-bem.button_js_inited")).click();
        logger.info("Compare page");
        driver.findElement(By.cssSelector("a.header2-menu__item_type_compare")).click();

//        -- Проверить, что в списке товаров 2 позиции
        logger.info("Compare page contains 2 position");
        waitWrapper("body", s);
        waifForPreloader(By.cssSelector(".preloadable__preloader"), 0);
        ExpectedConditions.numberOfElementsToBe(By.cssSelector("div.n-compare-cell"), 2);

//                - Нажать на опцию "все характеристики"
        logger.info("All characteristics");
        waitWrapper("body", s);
        waifForPreloader(By.cssSelector(".preloadable__preloader"), 0);
        driver.findElement(By.cssSelector("span.link.n-compare-show-controls__all")).click();
//                -- Проверить, что в списке характеристик появилась позиция "Операционная система"
        logger.info("OS enabled");
        waitWrapper("body", s);
        waifForPreloader(By.cssSelector(".preloadable__preloader"), 0);
        WebElement el3 = driver.findElement(By.xpath("//div[text()[contains(.,'Операционная система')]]"));
        logger.info(el3.getAttribute("class"));
        Assert.assertFalse(el3.getAttribute("class").contains("n-compare-row_hidden_yes"));


//                - Нажать на опцию "различающиеся характеристики"
        logger.info("Diff");
        waitWrapper("body", s);
        waifForPreloader(By.cssSelector(".preloadable__preloader"), 0);
        driver.findElement(By.cssSelector("span.n-compare-show-controls__diff")).click();

//                -- Проверить, что позиция "Операционная система" не отображается в списке характеристик
        logger.info("OS absent");
        waitWrapper("body", s);
        waifForPreloader(By.cssSelector(".preloadable__preloader"), 0);
        el3 = driver.findElement(By.xpath("//div[text()[contains(.,'Операционная система')]]/../.."));
        logger.info(el3.getAttribute("class"));// n-compare-row_hidden_yes
        Assert.assertTrue(el3.getAttribute("class").contains("n-compare-row_hidden_yes"));

    }

    public void getPage(){
        logger.info("driver.get " + href);
        //ExpectedConditions.numberOfElementsToBeMoreThan(5);
        //wait.until(d->d.findElement(By.id("555")).isDisplayed());
        driver.get(href);
    }


    public void boots() throws InterruptedException {
        driver.get("https://ng-bootstrap.github.io/#/components/alert/examples");

        ArrayList<WebElement> button2 = (ArrayList<WebElement>)driver.findElements(By.cssSelector("div.card-body button"));
        for(WebElement b:button2){
            logger.info("id    " + ((RemoteWebElement) b).getId());
        }

        WebElement button = driver.findElements(By.cssSelector("div.card-body button")).get(10);
        button.click();
        WebElement element = driver.findElement(By.xpath("//ngb-alert[@type='success']"));
        logger.info("text ---------------- " + button.getText());
        logger.info("id   ---------------- " + button.getAttribute("id"));
        logger.info("id   ---------------- " + ((RemoteWebElement) button).getId());
        logger.info("id   ---------------- " + ((RemoteWebElement) button).getId());

        ExpectedConditions.stalenessOf(element);


        button.click();
        element = driver.findElement(By.xpath("//ngb-alert[@type='success']"));
        logger.info("text ( " + element.getText() + ")");
        logger.info("text ( " + element.getAttribute("innerText") + ")");
        logger.info("id   ( " + element.getAttribute("id") + ")");
        logger.info("id   ---------------- " + ((RemoteWebElement) element).getId());

    }


    @After
    public void tearDown() {
        logger.info("Quit");
        //driver.manage().deleteAllCookies();
        if (null != driver) {
            driver.quit();
            driver = null;
        }
    }
}
