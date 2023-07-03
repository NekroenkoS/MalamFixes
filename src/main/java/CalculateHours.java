import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;
import java.util.Objects;

public class CalculateHours {

    public void calculateHours(String userName, String password) {
        WebDriver driver = new Utils().webDriverInit();
        new ConnectToMalam(userName,password,driver).start();
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id=\"pt1:dataTable::db\"]/table/tbody/tr[1]")));

        List<WebElement> elements = driver.findElements(By.xpath("//*[@id=\"pt1:dataTable::db\"]/table/tbody/tr"));
        int myTotalWorkHours = 0;
        int myTotalWorkMiuntes = 0;
        int totalWorkHoursNeeded = 0;
        int totalWorkMinutesNeeded=0;
        int row=0;
        for (WebElement element : elements) {

           new Utils().scrollToElement(driver, element);

            WebElement yourDailyWorkHours = element.findElement(By.xpath("//*[@id=\"pt1:dataTable:"+row+":hourTotalLabel\"]"));

            String label = yourDailyWorkHours.getText();
            if (!Objects.equals(label, "")) {
                // System.out.println(row + ". This is your work hours: " + label);

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
                    //     System.out.println(row +". This is work hours needed: "+workHoursNeeded);
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
