import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;

public class Main {

    public static void main(String[] args) throws InterruptedException {
        String userName = "USERNAME" ; // שם משתמש למלמ
        String password = "PASSWORD"; //סיסמא למלמ
        String oldProjectNumber="oldProjectNumber"; //מספר פרוייקט אותו רוצים להחליף
        String newProjectNumber="newProjectNumber"; //מספר פרוייקט שרוצים שיישמר

        changeMalamProject(userName,password,oldProjectNumber,newProjectNumber);

        //**IN DEVELOPMENT calculateHours(userName,password); **IN DEVELOPMENT
    }

    /*
    מתודה שמטרתה להתחבר למלמ ולהיכנס לאזור נוכחות
     */
    private static void connectToMalam(String userName, String password,WebDriver driver) throws InterruptedException {
        // פותחים את מלמ
        driver.get("https://payroll.malam.com/Salprd5Root/faces/login.jspx?p_index_num=35&_adf.ctrl-state=5l79xr3wb_3&_afrRedirect=179886718974093");
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("useridInput::content")));

        //מגדירים שם משתמש וסיסמא ומקלידים בשדות הרלוונטיים
        WebElement username = driver.findElement(By.id("useridInput::content"));
        username.sendKeys(userName);

        WebElement password1 = driver.findElement(By.id("it2::content"));
        password1.sendKeys(password);

        //לחיצה על כניסה
        WebElement submitButton = driver.findElement(By.id("loginButtonText"));
        submitButton.click();

        //לחיצה על נוכחות פעם ראשונה
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//a[contains(.,'נוכחות')]")));
        submitButton = driver.findElement(By.xpath("//a[contains(.,'נוכחות')]"));
        submitButton.click();

        Thread.sleep(500);

        //לוחצים על נוכחות פעם שנייה
        submitButton = driver.findElement(By.xpath("//tr[@id='pt1:timesheet__31410110']/td[2]"));
        submitButton.click();

    }

    /*
    המתודה מקבלת שם משתמש, סיסמא, מספר הפרויקט שצריך להחליף ומספר הפרויקט החדש
    המתודה מתחברת לחשבון הרלוונטי במלמ
    מחליפה את כל הפרוייקטים בפרוייקטים החדשים ועושה שמירה
     */
    public static void changeMalamProject(String userName, String password, String oldProjectNumber,String newProjectNumber) throws  InterruptedException {

        System.setProperty("webdriver.chrome.driver", "C:\\Users\\USER\\ChangeProject\\chromedriver.exe");
        WebDriver driver = new ChromeDriver();
        connectToMalam(userName,password,driver);

        /*
        מחפשים בדף את כל האלמנטים שיש להם בנתיב את המילה
         dataTabel
         וגם את הערך של הפרוייקט הקודם
         זה לצורך הבאת כל השדות שצריך להחליף

         רצים בלולאה על כל אלמנט שנמצא, מוחקים את הערך הנוכחי שיש לו ומחליפים בפרוייקט החדש
         */
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//input[contains(@id, 'dataTable') and @value='" + oldProjectNumber + "']")));
        List<WebElement> elements = driver.findElements(By.xpath("//input[contains(@id, 'dataTable') and @value='" + oldProjectNumber + "']"));
        System.out.println(elements.size());
        for (WebElement element : elements) {
            scrollToElement(driver, element); // בגלל שהמלמ משתמש בחלון גלילה בתוך האתר אז צריך להביא לפוקוס את האמלנט, נבנתה מתודה נוספת לעשות זאת
            element.clear();
            element.sendKeys(newProjectNumber);
        }

        //לוחצים שמירה
        WebElement button = driver.findElement(By.xpath("//td/button[@id='pt1:saveButton']"));
        button.click();

        Thread.sleep(10000); // ממתינים כדי שנוכל לראות שהכל השתנה

        driver.quit(); // סוגרים את הדפדפן
    }

    /*
    מתודה שגוללת את הדפדפן הפנימי של מלמ לאלמנט הנבחר
     */
    private static void scrollToElement(WebDriver driver, WebElement element) {
        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript("arguments[0].scrollIntoView(true);", element);
    }

    /*
    ***עדיין בפיתוח***
    מתודה שמחשבת כמה שעות עשינו עד כה החודש מול כמה צריך היה לעשות החודש עד כה
     */
    public static void calculateHours(String userName,String password) throws InterruptedException {
        System.setProperty("webdriver.chrome.driver", "C:\\Users\\USER\\Desktop\\chromedriver.exe");
        WebDriver driver = new ChromeDriver();
        connectToMalam(userName,password,driver);
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        WebElement element1 = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//span[starts-with(@id, 'pt1:dataTable') and contains(@id, ':hourTotalLabel')]/label")));
        List<WebElement> elements = driver.findElements(By.xpath("//span[starts-with(@id, 'pt1:dataTable') and contains(@id, ':hourTotalLabel')]/label"));
        int totalMinutes = 0;
        for (WebElement element : elements) {
            String hourText = (String) ((JavascriptExecutor) driver).executeScript("return arguments[0].textContent;", element);
            if(!hourText.isEmpty()) {
                int hour = Integer.parseInt(hourText.split(":")[0]);
                int minutes = Integer.parseInt(hourText.split(":")[1]);
                int totalMinutesForElement = hour * 60 + minutes;
                totalMinutes += totalMinutesForElement;
            }
        }

        int totalHours = totalMinutes / 60;
        int remainingMinutes = totalMinutes % 60;

        System.out.println("Your Total time: " + totalHours + " hours " + remainingMinutes + " minutes");

        totalMinutes=0;
        elements = driver.findElements(By.xpath("//td[contains(@class, 'xzx')]/nobr/span[contains(text(), ':')]"));
        System.out.println(elements.size());
        for (WebElement element : elements) {
            String hourText = element.getText();
            if(!hourText.isEmpty()) {
                System.out.println(hourText);
                int hour = Integer.parseInt(hourText.split(":")[0]);
                int minutes = Integer.parseInt(hourText.split(":")[1]);
                int totalMinutesForElement = hour * 60 + minutes;
                totalMinutes += totalMinutesForElement;
            }
        }
        totalHours = totalMinutes / 60;
        remainingMinutes = totalMinutes % 60;
        System.out.println("Needed Total time: " + totalHours + " hours " + remainingMinutes + " minutes");
    }

}