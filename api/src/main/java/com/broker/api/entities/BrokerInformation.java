package com.broker.api.entities;

/**
 * Created by luisburgos on 15/10/15.
 */
public class BrokerInformation {

    private String name;
    private String ip;
    private int port;

    public BrokerInformation(String name, String ip, int port) {
        this.name = name;
        this.ip = ip;
        this.port = port;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    @Override
    public String toString() {
        return "BrokerInformation{" +
                "name='" + name + '\'' +
                ", ip='" + ip + '\'' +
                ", port=" + port +
                '}';
    }
}
