package com.broker.api.entities;

/**
 * Created by luisburgos on 15/10/15.
 */
public class Service {

    private String ip;
    private int port;
    private String service;
    private boolean status;

    public Service(String ip, int port, String service, boolean status){
        this.ip = ip;
        this.port = port;
        this.service = service;
        this.status = status;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getService() {
        return service;
    }

    public void setService(String service) {
        this.service = service;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }
}

