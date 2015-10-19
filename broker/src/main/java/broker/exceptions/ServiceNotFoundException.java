package broker.exceptions;

/**
 * Created by luisburgos on 27/09/15.
 */
public class ServiceNotFoundException extends ServiceException {

    private final String errorMessage = "Couldn't get service requested. Not found.";

    @Override
    public String getMessage() {
        return errorMessage;
    }

}
