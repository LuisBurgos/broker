package broker;

import java.net.*;
import java.io.*;

public class Protocol {
    private static final int WAITING = 0;
    private static final int ATENDING = 1;

    private int state = WAITING;

    public String processInput(String theInput) {
        String theOutput = null;

        if(state == WAITING) {
            theOutput = "Connected to Broker";
            state = ATENDING;
        } else if(state == ATENDING){
            if (!theInput.equals("Bye.")) {
                theOutput = theInput;
            } else {
                theOutput = "Bye.";
                state = WAITING;
            }
        }

        return theOutput;
    }
}