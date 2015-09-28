package broker;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;

/**
 * Created by luisburgos on 21/09/15.
 */
public class BrokerThread implements Runnable {

    private Socket socket = null;
    private static int serverPortNumber = 2222;
    private static String serverHostName = "localhost";
    private static int totalThreads = 0;
    
    private HashMap<String,Service> services;
    
    public BrokerThread(Socket socket){
        this.socket = socket;
        System.out.println("thread #"+ ++totalThreads + " on Broker");
        services = new HashMap();

        this.addService("Consulta", new Service(serverHostName,serverPortNumber, "Consulta"));
        this.addService("Alta", new Service(serverHostName,serverPortNumber, "Alta"));
        this.addService("Baja", new Service(serverHostName,serverPortNumber, "Baja"));
        this.addService("Cambio", new Service(serverHostName,serverPortNumber, "Cambio"));

    }

    public void connect(ServerSocket client){
        //Genera un socket
    }
    
    public void addService(String name, Service service){
        services.put(name, service);
    }
    
    public void run(){
        try (

                PrintWriter clientOut =
                        new PrintWriter(socket.getOutputStream(), true);
                BufferedReader clientIn =
                        new BufferedReader(
                                new InputStreamReader(socket.getInputStream())
                        );
        ) {

            String inputLine, outputLine;
            Protocol protocol = new Protocol();
            outputLine = protocol.processInput(null);
            clientOut.println(outputLine);

            final int currentThread = totalThreads;

            while((inputLine = clientIn.readLine()) != null ){
                
                outputLine = protocol.processInput(inputLine);
                System.out.println("Current thread #" + currentThread +" requests: " + outputLine);

                //clientOut.println(outputLine);
                if(outputLine.equals("Bye.")){
                    break;
                }

                if(services.containsKey(outputLine)){
                    clientOut.println("YES");
                }
                else{
                    clientOut.println("NO");
                }

            }
            socket.close();

        }catch (IOException e) {
            e.printStackTrace();
        }
    }

}
