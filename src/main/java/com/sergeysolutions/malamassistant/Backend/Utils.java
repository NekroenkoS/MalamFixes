package com.sergeysolutions.malamassistant.Backend;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

public class Utils {

    public Utils(){}

    public void scrollToElement(WebDriver driver, WebElement element) {
        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript("arguments[0].scrollIntoView(true);", element);
    }

    public WebDriver webDriverInit(boolean isHeadless) throws IOException {
        String tempDir = System.getProperty("java.io.tmpdir");
        File tempDriver = new File(tempDir, "chromedriver.exe");
        if (!tempDriver.exists()) {
            try (InputStream in = getClass().getResourceAsStream("/chromedriver.exe")) {
                assert in != null;
                Files.copy(in, tempDriver.toPath(), StandardCopyOption.REPLACE_EXISTING);
            }
        }
        System.setProperty("webdriver.chrome.driver", tempDriver.getAbsolutePath());
        ChromeOptions options = new ChromeOptions();
        if(isHeadless){
            options.addArguments("headless");
        }
        return new ChromeDriver(options);
    }
}


