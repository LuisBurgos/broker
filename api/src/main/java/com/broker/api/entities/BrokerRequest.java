package com.broker.api.entities;

/**
 * Created by luisburgos on 2/10/15.
 */
public class BrokerRequest {

    private int type;
    private String serviceName;
    private String data;

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }
}
