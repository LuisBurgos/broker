package client;

/**
 * Created by luisburgos on 27/09/15.
 */
public class ServiceNotFoundException extends Exception {

    private final String errorMessage = "Couldn't get service requested";

    @Override
    public String getMessage() {
        return errorMessage;
    }

}
