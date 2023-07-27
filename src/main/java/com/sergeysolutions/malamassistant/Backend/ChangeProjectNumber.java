package com.sergeysolutions.malamassistant.Backend;

import com.sergeysolutions.malamassistant.FrontEnd.MalamAssistantUi;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.IOException;
import java.time.Duration;
import java.util.List;

public class ChangeProjectNumber {
    WebDriver driver;
    String oldProjectNumber;
    String newProjectNumber;

    public ChangeProjectNumber(String userName, String password, boolean isHeadless) throws IOException {
        this.driver = new Utils().webDriverInit(isHeadless);
        new ConnectToMalam(driver).start(userName, password);
    }

    public void start(String oldProjectNumber, String newProjectNumber, boolean keepAlive){
        this.oldProjectNumber = oldProjectNumber;
        this.newProjectNumber = newProjectNumber;
        waitForMalamVisibility();
        updateProjectNumber(oldProjectNumber, newProjectNumber);
        clickSave();
        if (!keepAlive){driver.quit();}
    }

    private void waitForMalamVisibility() {
        WebElement malamTable = driver.findElement(By.cssSelector("div[id='pt1:dataTable::db']"));
        new Utils().scrollToElement(driver, malamTable);
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(3));
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//input[contains(@id, 'dataTable') and @value='" + oldProjectNumber + "']")));

    }

    private void updateProjectNumber(String oldProjectNumber, String newProjectNumber) {
        List<WebElement> elements = driver.findElements(By.xpath("//input[contains(@id, 'dataTable') and @value='" + oldProjectNumber + "']"));
        System.out.println(elements.size());
        for (WebElement element : elements) {
            new Utils().scrollToElement(driver, element);
            element.clear();
            element.sendKeys(newProjectNumber);
        }
        MalamAssistantUi.getInstance().appendLog("Located " + elements.size() + " instances to replace.");
    }

    private void clickSave() {
        WebElement button = driver.findElement(By.xpath("//td/button[@id='pt1:saveButton']"));
        button.click();
        MalamAssistantUi.getInstance().appendLog("Successfully replaced all instances and saved the changes");
    }
}
