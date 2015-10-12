package broker;

import broker.entities.Service;
import broker.exceptions.ServiceNotFoundException;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.HashMap;

/**
 * Created by luisburgos on 21/09/15.
 */
public class Broker {

    private static final int PORT_NUMBER_BROKER = 5555;
    private HashMap<String,Service> services;
    private ServerSocket serverSocketBroker;

    public Broker(){
        services = new HashMap<String, Service>();
    }

    public void registerService(Service service){
        registerService(service.getService(), service);
    }

    public void registerService(String name, Service service){
        if(!services.containsKey(name)){
            services.put(name, service);
        }
        System.out.println(services);
    }

    public Service findService(String serviceName) throws ServiceNotFoundException {
        System.out.println(services);
        if(services.containsKey(serviceName)){
            return services.get(serviceName);
        }else{
            throw new ServiceNotFoundException();
        }
    }

    public void acceptConnections(){
        boolean listening = true;

        try {
            serverSocketBroker = new ServerSocket(PORT_NUMBER_BROKER);
            while (listening) {
                new Thread(new BrokerThread(this, serverSocketBroker.accept())).start();
            }
        } catch (IOException e) {
            System.err.println("Could not listen on port " + PORT_NUMBER_BROKER);
            System.exit(-1);
        }
    }

    public static void main(String[] args) throws IOException {
        Broker broker = new Broker();
        broker.acceptConnections();
    }

}