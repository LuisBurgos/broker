/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package broker;

/**
 *
 * @author JoseJulio
 */
public class Service {
    
    private String ip;
    private long port;
    private String service;
    
    public Service(String ip, int port, String services){
        this.ip = ip;
        this.port = port;
        this.service = services;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public long getPort() {
        return port;
    }

    public void setPort(long port) {
        this.port = port;
    }

    public String getService() {
        return service;
    }

    public void setService(String service) {
        this.service = service;
    }
    
    
    
}
