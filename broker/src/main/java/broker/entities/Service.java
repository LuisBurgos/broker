/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package broker.entities;

/**
 *
 * @author JoseJulio
 */
public class Service {
    
    private String ip;
    private int port;
    private String service;
    private boolean active;
    
    public Service(String ip, int port, String service){
        this.ip = ip;
        this.port = port;
        this.service = service;
        this.active = true;
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

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
}
