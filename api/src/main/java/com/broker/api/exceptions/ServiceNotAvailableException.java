package com.broker.api.exceptions;

/**
 * Created by luisburgos on 16/10/15.
 */
public class ServiceNotAvailableException extends ServiceException {

    private final String errorMessage = "Service requested is unavailable";

    @Override
    public String getMessage() {
        return errorMessage;
    }


}
