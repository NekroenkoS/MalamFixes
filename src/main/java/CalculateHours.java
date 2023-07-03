import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;
import java.util.Objects;

public class CalculateHours {

    private final WebDriver driver;
    private int myTotalWorkHours;
    private int myTotalWorkMinutes;
    private int totalWorkHoursNeeded;
    private int totalWorkMinutesNeeded;
    private int row;

    public CalculateHours(String userName, String password) {
        driver = new Utils().webDriverInit();
        new ConnectToMalam(driver).start(userName, password);
        this.myTotalWorkHours = 0;
        this.myTotalWorkMinutes = 0;
        this.totalWorkHoursNeeded = 0;
        this.totalWorkMinutesNeeded = 0;
        this.row = 0;

    }

    public void start(){
        waitForMalam();
        List<WebElement> elements = driver.findElements(By.xpath("//*[@id=\"pt1:dataTable::db\"]/table/tbody/tr"));
        handleHours(elements);
        displayMessage();
        calcDiffHours();
    }

    private void waitForMalam(){
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id=\"pt1:dataTable::db\"]/table/tbody/tr[1]")));
    }

    private void handleHours(List<WebElement> elements){
        for (WebElement element : elements) {
            new Utils().scrollToElement(driver, element);

            WebElement yourDailyWorkHours = element.findElement(By.xpath("//*[@id=\"pt1:dataTable:" + row + ":hourTotalLabel\"]"));
            String label = yourDailyWorkHours.getText();

            if (!Objects.equals(label, "")) {
                String[] timeParts = label.split(":");
                int hours = Integer.parseInt(timeParts[0]);
                int minutes = Integer.parseInt(timeParts[1]);

                myTotalWorkHours += hours;
                myTotalWorkMinutes += minutes;

                if (myTotalWorkMinutes >= 60) {
                    myTotalWorkHours += myTotalWorkMinutes / 60;
                    myTotalWorkMinutes %= 60;
                }

                WebElement workHoursNeededElement = element.findElement(By.xpath("//*[@id=\"pt1:dataTable::db\"]/table/tbody/tr[" + (row + 1) + "]/td[15]/nobr/span"));
                String workHoursNeeded = workHoursNeededElement.getAttribute("innerHTML").trim();

                if (!Objects.equals(workHoursNeeded, "")) {
                    String[] totalWorkParts = workHoursNeeded.split(":");
                    int totalWorkHours = Integer.parseInt(totalWorkParts[0]);
                    int totalWorkMinutes = Integer.parseInt(totalWorkParts[1]);

                    totalWorkHoursNeeded += totalWorkHours;
                    totalWorkMinutesNeeded += totalWorkMinutes;

                    if (totalWorkMinutesNeeded >= 60) {
                        totalWorkHoursNeeded += totalWorkMinutesNeeded / 60;
                        totalWorkMinutesNeeded %= 60;
                    }
                }
            }
            row++;
        }
    }

    private void calcDiffHours() {
        int differenceHours = myTotalWorkHours - totalWorkHoursNeeded;
        int differenceMinutes = myTotalWorkMinutes - totalWorkMinutesNeeded;
        String differenceSign = (differenceHours >= 0) ? "+" : "-";
        differenceHours = Math.abs(differenceHours);
        differenceMinutes = Math.abs(differenceMinutes);
        String differenceTime = String.format("%02d:%02d", differenceHours, differenceMinutes);
        if (differenceHours > 0 || differenceMinutes > 0) {
            String differenceMessage = "You have ";
            differenceMessage += (differenceHours > 0) ? differenceHours + " hours " : "";
            differenceMessage += (differenceMinutes > 0) ? differenceMinutes + " minutes " : "";

            if (differenceSign.equals("+")) {
                differenceMessage += "more than the required hours.";
            } else {
                differenceMessage += "less than the required hours.";
            }
            System.out.println(differenceMessage);
        } else {
            System.out.println("You have completed the required hours.");
        }

    }

    private void displayMessage(){
        System.out.println("Total work hours needed until today: " + totalWorkHoursNeeded + " hours and " + totalWorkMinutesNeeded + " minutes.");
        System.out.println("Total hours worked until today: " + myTotalWorkHours + " hours and " + myTotalWorkMinutes + " minutes.");

    }


}
