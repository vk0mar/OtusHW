
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

import static java.util.concurrent.TimeUnit.SECONDS;


public class OtusCabinetPersonal {

    private WebDriver driver;
    private static final Logger logger = LogManager.getLogger(OtusCabinetPersonal.class);

    WebDriverWait wait;

    OtusCabinetPersonal(WebDriver driver){
        this.driver = driver;
        driver.manage().timeouts().implicitlyWait(4, SECONDS);
        wait = new WebDriverWait(driver, 4, 125);
        driver.get("https://otus.ru/lk/biography/personal/");
        logger.info("URL: {}", driver.getCurrentUrl());
    }

    private By fname         = By.cssSelector("input[name=fname]");
    private By fname_latin   = By.cssSelector("input[name=fname_latin]");
    private By lname         = By.cssSelector("input[name=lname]");
    private By lname_latin   = By.cssSelector("input[name=lname_latin]");
    private By blog_name     = By.cssSelector("input[name=blog_name]");
    private By date_of_birth = By.cssSelector("input[name=date_of_birth]");
    private By contact0      = By.cssSelector("input[name=contact-0-value]");
    private By contact1      = By.cssSelector("input[name=contact-1-value]");


    public OtusCabinetPersonal send() throws InterruptedException {
        BaseHelper.sendData(driver, fname, "Имя");
        BaseHelper.sendData(driver, fname_latin,"Name");
        BaseHelper.sendData(driver, lname,"Фамилия");
        BaseHelper.sendData(driver, lname_latin,"Family");
        BaseHelper.sendData(driver, blog_name,"NickName");
        BaseHelper.sendData(driver, date_of_birth,"29.03.1999");

        // добавить  способ связи
        driver.findElement(By.cssSelector("button.js-lk-cv-custom-select-add")).click();
        driver.findElement(By.cssSelector("button.js-lk-cv-custom-select-add")).click();

        // способ связи
        driver.findElements(By.cssSelector("div.lk-cv-block__input_md-4.js-lk-cv-custom-select")).get(0).click();
        driver.findElements(By.cssSelector("button.lk-cv-block__select-option.js-custom-select-option[title=VK]")).get(0).click();
        BaseHelper.sendData(driver, contact0,"MyVK");

        driver.findElements(By.cssSelector("div.lk-cv-block__input_md-4.js-lk-cv-custom-select")).get(1).click();
        driver.findElements(By.cssSelector("button.lk-cv-block__select-option.js-custom-select-option[data-value=facebook]")).get(1).click();
        BaseHelper.sendData(driver, contact1,"MyFacebook");

        driver.findElement(By.cssSelector("button.js-disable-on-submit")).click();
        return this;
    }
}
