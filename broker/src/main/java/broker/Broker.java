package broker;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;

/**
 * Created by luisburgos on 21/09/15.
 */
public class Broker {

    private static int portNumber = 5555;
    private HashMap<String,Service> services;
    private ServerSocket serverSocket;

    public Broker(){
        services = new HashMap<>();
        //Dummy services
        this.addService("Consulta", new Service("localhost",3333, "Consulta"));
        this.addService("Alta", new Service("localhost",3333, "Alta"));
        this.addService("Baja", new Service("localhost",3333, "Baja"));
        this.addService("Cambio", new Service("localhost", 3333, "Cambio"));
    }

    public void addService(String name, Service service){
        services.put(name, service);
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
            serverSocket = new ServerSocket(portNumber);
            while (listening) {
                new Thread(new BrokerThread(this, serverSocket.accept())).start();
            }
        } catch (IOException e) {
            System.err.println("Could not listen on port " + portNumber);
            System.exit(-1);
        }
    }

    public static void main(String[] args) throws IOException {

        Broker broker = new Broker();
        broker.acceptConnections();
    }

}