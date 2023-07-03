import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;

public class ChangeProjectNumber {

    public ChangeProjectNumber(String userName, String password, String oldProjectNumber, String newProjectNumber) throws InterruptedException {

        WebDriver driver = new Utils().webDriverInit();
        new ConnectToMalam(userName, password, driver).start();

        WebElement malamTable = driver.findElement(By.cssSelector("div[id='pt1:dataTable::db']"));
        new Utils().scrollToElement(driver, malamTable);
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//input[contains(@id, 'dataTable') and @value='" + oldProjectNumber + "']")));
        List<WebElement> elements = driver.findElements(By.xpath("//input[contains(@id, 'dataTable') and @value='" + oldProjectNumber + "']"));
        System.out.println(elements.size());
        for (WebElement element : elements) {
            new Utils().scrollToElement(driver, element); // בגלל שהמלמ משתמש בחלון גלילה בתוך האתר אז צריך להביא לפוקוס את האמלנט, נבנתה מתודה נוספת לעשות זאת
            element.clear();
            element.sendKeys(newProjectNumber);
        }
        //לוחצים שמירה
        WebElement button = driver.findElement(By.xpath("//td/button[@id='pt1:saveButton']"));
        button.click();
        Thread.sleep(10000); // ממתינים כדי שנוכל לראות שהכל השתנה
        driver.quit(); // סוגרים את הדפדפן
    }

}
