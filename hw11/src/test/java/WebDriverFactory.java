
import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.remote.DesiredCapabilities;

import java.sql.Driver;

public class WebDriverFactory {
    private static WebDriver driver;


    public static WebDriver create(DriverType type) {

        switch (type) {
            case CHROME:
                WebDriverManager.chromedriver().setup();
                driver = new ChromeDriver();
                break;
            case FIREFOX:
                WebDriverManager.firefoxdriver().setup();
                driver = new FirefoxDriver();
                break;
        }

        return driver;
    }

    public static WebDriver create(DriverType type, Object op) {

        switch (type) {
            case CHROME:
                WebDriverManager.chromedriver().setup();
                driver = new ChromeDriver((DesiredCapabilities) op);
                break;
            case FIREFOX:
                WebDriverManager.firefoxdriver().setup();
                driver = new FirefoxDriver((FirefoxOptions)op);
                break;
        }

        return driver;
    }

}
