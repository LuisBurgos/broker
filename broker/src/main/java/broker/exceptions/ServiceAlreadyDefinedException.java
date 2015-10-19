package broker.exceptions;

/**
 * Created by luisburgos on 16/10/15.
 */
public class ServiceAlreadyDefinedException extends ServiceException {

    private final String errorMessage = "Service is already registered into broker";

    @Override
    public String getMessage() {
        return errorMessage;
    }

}
