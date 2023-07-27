package com.sergeysolutions.malamassistant.Backend;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import java.io.File;

public class Utils {

    public Utils(){}

    public void scrollToElement(WebDriver driver, WebElement element) {
        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript("arguments[0].scrollIntoView(true);", element);
    }

    public WebDriver webDriverInit(boolean isHeadless) {
        String projectPath = System.getProperty("user.dir");
        String driverPath = projectPath + "\\chromedriver.exe";
        System.setProperty("webdriver.chrome.driver", new File("chromedriver.exe").getAbsolutePath());
        ChromeOptions options = new ChromeOptions();
        if(isHeadless){
        options.addArguments("headless");}
        return new ChromeDriver(options);
    }
}


