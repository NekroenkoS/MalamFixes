package com.sergeysolutions.malamassistant.Backend;

import com.sergeysolutions.malamassistant.FrontEnd.MalamAssistantUi;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.IOException;
import java.time.Duration;

public class ConnectToMalam {

    WebDriver driver;
    String userName;
    String password;
    WebDriverWait wait;

    public ConnectToMalam(WebDriver driver) throws IOException {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(3));
    }

    public void start(String userName, String password) {
        this.userName = userName;
        this.password = password;
        openMalamPage();
        enterCredentials();
        enterAttendanceArea();
    }

    private void openMalamPage() {
        try {
            driver.get("https://payroll.malam.com/Salprd5Root/faces/login.jspx?p_index_num=35&_adf.ctrl-state=5l79xr3wb_3&_afrRedirect=179886718974093");
        } catch (WebDriverException e) {
            String errorMessage = e.getMessage();
            if (errorMessage.contains("net::ERR_CONNECTION_RESET")) {
                String customMessage = "Assistant was not able to reach Malam, check Malam website.";
                System.out.println(customMessage);
                MalamAssistantUi.getInstance().appendLog(customMessage);
            } else {
                System.out.println("An error occurred: " + errorMessage);
                MalamAssistantUi.getInstance().appendLog("An error occurred: " + errorMessage);
            }
            e.printStackTrace();
        }

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(3));
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("useridInput::content")));

    }

    private void enterCredentials() {
        driver.findElement(By.id("useridInput::content")).sendKeys(userName);
        driver.findElement(By.id("it2::content")).sendKeys(password);
        driver.findElement(By.id("loginButtonText")).click();
    }

    private void enterAttendanceArea() {
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id='pt1:j_id7']/div/table/tbody/tr/td[2]/a")));
        driver.findElement(By.xpath("//*[@id='pt1:j_id7']/div/table/tbody/tr/td[2]/a")).click();
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//tr[@id='pt1:timesheet__31410110']/td[2]")));
        driver.findElement(By.xpath("//tr[@id='pt1:timesheet__31410110']/td[2]")).click();
    }
}
