package broker;

import com.google.gson.Gson;

import java.net.*;
import java.io.*;

public class Protocol {
    private static final int WAITING = 0;
    private static final int ATENDING = 1;

    private int state = WAITING;

    public String processInput(String theInput) {

        Request request;
        String theOutput = null;

        if(theInput == null){
            Response response = new Response();
            response.setType(ResponseTypes.CONNECTED);
            theOutput = new Gson().toJson(response);
        }

        if(theInput != null){
            if(theInput.startsWith("{") && theInput.endsWith("}")){
                theOutput = theInput;
            }else {
                theOutput = "error";
            }
        }
        return theOutput;
    }
}