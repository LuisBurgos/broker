package broker;

import broker.entities.Service;
import broker.exceptions.ServiceAlreadyDefinedException;
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

    public void registerService(Service service) throws ServiceAlreadyDefinedException {
        registerService(service.getService(), service);
    }

    public void changeServiceState(String serviceName, boolean status) throws ServiceNotFoundException {
        findService(serviceName).setActive(status);
        //Service serviceToChange = findService(serviceName);
        //serviceToChange.setActive(status);
        //services.put(serviceName, serviceToChange);
    }

    public void registerService(String name, Service service) throws ServiceAlreadyDefinedException {
        if(!services.containsKey(name)){
            services.put(name, service);
        }else{
            throw new ServiceAlreadyDefinedException();
        }
    }

    public Service findService(String serviceName) throws ServiceNotFoundException {
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