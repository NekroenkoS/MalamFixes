package com.sergeysolutions.malamassistant.Backend;

import java.io.IOException;

public class Main {

    public static void main(String[] args) throws IOException {
        String userName = "317149953";
        String password = "z@27A7h3*ruD!a";
        String oldProjectNumber = "oldProjectNumber";
        String newProjectNumber = "newProjectNumber";

        //new ChangeProjectNumber(userName,password).start(oldProjectNumber,newProjectNumber);
        new CalculateHours(userName,password).start();
    }
}