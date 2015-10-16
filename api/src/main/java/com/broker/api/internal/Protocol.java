package com.broker.api.internal;

import com.broker.api.entities.BrokerResponse;
import com.google.gson.Gson;
import com.broker.api.entities.TypesBrokerResponse;

public class Protocol {
    private static final int WAITING = 0;
    private static final int ATENDING = 1;

    private int state = WAITING;

    public String processInput(String theInput) {

        String theOutput = null;

        if(theInput == null){
            BrokerResponse brokerResponse = new BrokerResponse();
            brokerResponse.setType(TypesBrokerResponse.CONNECTED);
            theOutput = new Gson().toJson(brokerResponse);
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