package server;

import com.google.gson.Gson;
import server.model.entities.Response;
import server.utils.ResponseTypes;

public class Protocol {
    private static final int WAITING = 0;
    private static final int ATENDING = 1;

    private int state = WAITING;

    public String processInput(String theInput) {
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
                Response response = new Response();
                response.setType(ResponseTypes.REQUEST_ERROR);
                theOutput = new Gson().toJson(response);
            }
        }
        return theOutput;
    }
}