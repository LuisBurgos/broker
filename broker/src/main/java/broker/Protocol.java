package broker;

import broker.entities.Request;
import broker.entities.Response;
import broker.utils.ResponseTypes;
import com.google.gson.Gson;

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
                theOutput = "error";
            }
        }
        return theOutput;
    }
}