package pages;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebDriver;

public class OtusAuthStartPage {
    private WebDriver driver;
    private static final Logger logger = LogManager.getLogger(OtusAuthStartPage.class);

    OtusAuthStartPage(WebDriver driver) {
        this.driver = driver;
        logger.info("URL: {}", driver.getCurrentUrl());
    }

    public OtusCabinetPersonal getAbout() {

        return new OtusCabinetPersonal(driver);
    }
}
