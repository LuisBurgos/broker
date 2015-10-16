package com.broker.api.exceptions;

/**
 * Created by luisburgos on 16/10/15.
 */
public class BrokerConnectionErrorException extends BrokerException {

    private final String errorMessage = "Couldn't connect to Broker";

    @Override
    public String getMessage() {
        return errorMessage;
    }


}
