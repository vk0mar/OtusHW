import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;

public class BaseHelper {
    public static void sendData(WebDriver driver, By b, String s){
        driver.findElement(b).sendKeys(Keys.HOME,Keys.chord(Keys.SHIFT,Keys.END), s);
        //Keys.chord(Keys.CONTROL, "a")
    }
}
