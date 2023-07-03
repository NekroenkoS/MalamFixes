public class Main {

    public static void main(String[] args) throws InterruptedException {
        String userName = "userName"; // שם משתמש למלמ
        String password = "password"; //סיסמא למלמ
        String oldProjectNumber = "oldProjectNumber"; //מספר פרוייקט אותו רוצים להחליף
        String newProjectNumber = "newProjectNumber"; //מספר פרוייקט שרוצים שיישמר

        new ChangeProjectNumber(userName,password,oldProjectNumber,newProjectNumber);
        //CalculateHours.calculateHours(userName, password);
    }
}