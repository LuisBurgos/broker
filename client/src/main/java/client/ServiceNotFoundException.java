package client;

/**
 * Created by luisburgos on 27/09/15.
 */
public class ServiceNotFoundException extends Exception {
    public ServiceNotFoundException() { super(); }
    public ServiceNotFoundException(String message) { super(message); }
    public ServiceNotFoundException(String message, Throwable cause) { super(message, cause); }
    public ServiceNotFoundException(Throwable cause) { super(cause); }
}
