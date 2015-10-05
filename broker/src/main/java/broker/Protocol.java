package broker;

import com.google.gson.Gson;

import java.net.*;
import java.io.*;

public class Protocol {
    private static final int WAITING = 0;
    private static final int ATENDING = 1;

    private int state = WAITING;

    public String processInput(String theInput) {

        Request request = null;
        String theOutput = null;

        if(theInput != null){
            request = new Gson().fromJson(theInput, Request.class);
            //System.out.println(request.s());
            if(request.getType() == 1){
                return theOutput = request.getServiceName();
            }
        }


        if(state == WAITING) {
            theOutput = "Connected to Broker";
            state = ATENDING;
        } else if(state == ATENDING){
            if (!theInput.equals("Close.")) {
                //theOutput = request.getServiceName();
            } else {
                theOutput = "Close.";
                state = WAITING;
            }
        }

        return theOutput;
    }
}