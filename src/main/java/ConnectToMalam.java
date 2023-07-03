import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class ConnectToMalam {

    WebDriver driver;
    String userName;
    String password;
    WebDriverWait wait;

    public ConnectToMalam(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    public void start(String userName, String password) {
        this.userName = userName;
        this.password = password;
        openMalamPage();
        enterCredentials();
        enterAttendanceArea();
    }

    private void openMalamPage() {
        driver.get("https://payroll.malam.com/Salprd5Root/faces/login.jspx?p_index_num=35&_adf.ctrl-state=5l79xr3wb_3&_afrRedirect=179886718974093");
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("useridInput::content")));

    }

    private void enterCredentials() {
        driver.findElement(By.id("useridInput::content")).sendKeys(userName);
        driver.findElement(By.id("it2::content")).sendKeys(password);
        driver.findElement(By.id("loginButtonText")).click();
    }

    private void enterAttendanceArea() {
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//a[contains(.,'נוכחות')]")));
        driver.findElement(By.xpath("//a[contains(.,'נוכחות')]")).click();
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//tr[@id='pt1:timesheet__31410110']/td[2]")));
        driver.findElement(By.xpath("//tr[@id='pt1:timesheet__31410110']/td[2]")).click();
    }
}
