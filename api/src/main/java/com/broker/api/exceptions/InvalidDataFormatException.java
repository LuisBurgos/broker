package com.broker.api.exceptions;

/**
 * Created by luisburgos on 16/10/15.
 */
public class InvalidDataFormatException extends BrokerException {

    private final String errorMessage = "Data format invalid for request";

    @Override
    public String getMessage() {
        return errorMessage;
    }

}
