import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;
import java.util.Objects;

public class Main {

    public static void main(String[] args) throws InterruptedException {
        String userName = "317149953"; // שם משתמש למלמ
        String password = "z@27A7h3*ruD!a"; //סיסמא למלמ
        String oldProjectNumber = "300530732"; //מספר פרוייקט אותו רוצים להחליף
        String newProjectNumber = "300536419"; //מספר פרוייקט שרוצים שיישמר

        //changeMalamProject(userName,password,oldProjectNumber,newProjectNumber);
        calculateHours(userName, password);
    }

    /*
    מתודה מאתחלת את הדרייבר של כרום
     */
    private static WebDriver webDriverInit() {
        String projectPath = System.getProperty("user.dir");
        String driverPath = projectPath + "\\chromedriver.exe";
        System.setProperty("webdriver.chrome.driver", driverPath);
        return new ChromeDriver();
    }

    /*
    מתודה שמטרתה להתחבר למלמ ולהיכנס לאזור נוכחות
     */
    private static void connectToMalam(String userName, String password, WebDriver driver) throws InterruptedException {
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
    private static void changeMalamProject(String userName, String password, String oldProjectNumber, String newProjectNumber) throws InterruptedException {

        WebDriver driver = webDriverInit();
        connectToMalam(userName, password, driver);

        /*
        מחפשים בדף את כל האלמנטים שיש להם בנתיב את המילה
         dataTabel
         וגם את הערך של הפרוייקט הקודם
         זה לצורך הבאת כל השדות שצריך להחליף

         רצים בלולאה על כל אלמנט שנמצא, מוחקים את הערך הנוכחי שיש לו ומחליפים בפרוייקט החדש
         */
        WebElement malamTable = driver.findElement(By.cssSelector("div[id='pt1:dataTable::db']"));
        scrollToElement(driver, malamTable);

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

    private static void calculateHours(String userName, String password) throws InterruptedException {
        WebDriver driver = webDriverInit();

        connectToMalam(userName, password, driver);

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id=\"pt1:dataTable::db\"]/table/tbody/tr[1]")));

        WebElement button = driver.findElement(By.xpath("//*[@id=\"pt1:prevMonth\"]"));
        button.click();

         new WebDriverWait(driver, Duration.ofSeconds(10))
                .until(ExpectedConditions.elementToBeClickable(By.xpath("//*[@id=\"pt1:prevMonth\"]")));

        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id=\"pt1:dataTable::db\"]/table/tbody/tr[1]")));
        button.click();

         new WebDriverWait(driver, Duration.ofSeconds(10))
                .until(ExpectedConditions.elementToBeClickable(By.xpath("//*[@id=\"pt1:prevMonth\"]")));

        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id=\"pt1:dataTable::db\"]/table/tbody/tr[1]")));

        List<WebElement> elements = driver.findElements(By.xpath("//*[@id=\"pt1:dataTable::db\"]/table/tbody/tr"));
        System.out.println(elements.size());
        int myTotalWorkHours = 0;
        int myTotalWorkMiuntes = 0;
        int totalWorkHoursNeeded = 0;
        int totalWorkMinutesNeeded=0;
        int row=0;
        for (WebElement element : elements) {
            scrollToElement(driver, element);
            WebElement yourDailyWorkHours = element.findElement(By.xpath("//*[@id=\"pt1:dataTable:"+row+":hourTotalLabel\"]"));

                String label = yourDailyWorkHours.getText();
                if (!Objects.equals(label, "")) {
                    System.out.println(row + ". This is your work hours: " + label);

                    // Extract the hh:mm values from the label
                    String[] timeParts = label.split(":");
                    int hours = Integer.parseInt(timeParts[0]);
                    int minutes = Integer.parseInt(timeParts[1]);

                    // Sum the hours and minutes
                    myTotalWorkHours += hours;
                    myTotalWorkMiuntes += minutes;

                    // Adjust the minutes if it exceeds 59
                    if (myTotalWorkMiuntes >= 60) {
                        myTotalWorkHours += myTotalWorkMiuntes / 60;
                        myTotalWorkMiuntes %= 60;
                    }

                    WebElement workHoursNeededElement=element.findElement(By.xpath("//*[@id=\"pt1:dataTable::db\"]/table/tbody/tr["+(row+1)+"]/td[15]/nobr/span"));
                    String workHoursNeeded = workHoursNeededElement.getAttribute("innerHTML").trim();
                    if (!Objects.equals(workHoursNeeded, "")) {
                        System.out.println(row +". This is work hours needed: "+workHoursNeeded);
                        String[] totalWorkParts = workHoursNeeded.split(":");
                        int totalWorkHours = Integer.parseInt(totalWorkParts[0]);
                        int totalWorkMinutes = Integer.parseInt(totalWorkParts[1]);

                        // Sum the total work hours and minutes
                        totalWorkHoursNeeded += totalWorkHours;
                        totalWorkMinutesNeeded += totalWorkMinutes;

                        // Adjust the minutes if it exceeds 59
                        if (totalWorkMinutesNeeded >= 60) {
                            totalWorkHoursNeeded += totalWorkMinutesNeeded / 60;
                            totalWorkMinutesNeeded %= 60;
                        }
                    }
                }
        row++;
        }
        System.out.println("Total Work Hours: "+totalWorkHoursNeeded+":"+totalWorkMinutesNeeded);
        System.out.println("Total Hours Worked: "+myTotalWorkHours+":"+myTotalWorkMiuntes);
        // Calculate the difference between myTotalWorkHours and totalWorkHoursNeeded
        int differenceHours = myTotalWorkHours - totalWorkHoursNeeded;
        int differenceMinutes = myTotalWorkMiuntes - totalWorkMinutesNeeded;
        String differenceSign = (differenceHours >= 0) ? "+" : "-";
        differenceHours = Math.abs(differenceHours);
        differenceMinutes = Math.abs(differenceMinutes);

        // Display the difference
        String differenceTime = String.format("%02d:%02d", differenceHours, differenceMinutes);
        if (differenceHours > 0 || differenceMinutes > 0) {
            String differenceMessage = "You are " + differenceSign + " " + differenceTime + " ";
            if (differenceHours > 0) {
                differenceMessage += "hours ";
            }
            if (differenceMinutes > 0) {
                differenceMessage += "and minutes ";
            }
            if (differenceSign.equals("+")) {
                differenceMessage += "above the needed hours.";
            } else {
                differenceMessage += "below the needed hours.";
            }
            System.out.println(differenceMessage);
        } else {
            System.out.println("You have fulfilled the needed hours.");
        }
    }
}