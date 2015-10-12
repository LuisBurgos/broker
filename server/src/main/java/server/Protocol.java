package server;

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
            if (!theInput.equals("Close.")) {
                theOutput = theInput;
            } else {
                theOutput = "Close.";
                state = WAITING;
            }
        }

        return theOutput;
    }
}