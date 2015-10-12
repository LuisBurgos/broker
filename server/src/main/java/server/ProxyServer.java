package server;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import server.model.entities.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * Created by luisburgos on 2/10/15.
 */
public class ProxyServer {

    private static final int BROKER_PORT_NUMBER = 5555;
    private static final String BASE_HOSTNAME = "localhost";
    private String entity;

    private Socket brokerSocket;
    private PrintWriter brokerOutput;
    private BufferedReader brokerInput;

    public void build(){
        try {
            connectToBroker();
            initializeBuffers();
        } catch (IOException e) {
            System.err.println("Couldn't get I/O for the connection to " + BASE_HOSTNAME);
            e.printStackTrace();
            System.exit(1);
        }
    }

    public void callService(String serviceName){
           //Magia aqui porfavor...
    }

    public void sendResponse(){

    }

    public void packData(){

    }

    public void unpackData(){

    }

    public void registerServiceToBroker (){
        Service service = new Service("localhost",
                                      Server.PORT_NUMBER_SERVER,
                                      "addVoteToCandidateById"
        );
        String entity;
        JsonObject json = new JsonObject();
        json.addProperty("type", BrokerActions.REGISTER_SERVICE);
        json.addProperty("serviceName", "addVoteToCandidateById");
        json.addProperty("candidateId", new Gson().toJson(service));
        entity = json.toString();
        brokerOutput.println(entity);
    }

    private void connectToBroker() throws IOException {
        brokerSocket = new Socket(BASE_HOSTNAME, BROKER_PORT_NUMBER);
    }

    private void initializeBuffers() throws IOException {
        brokerOutput = new PrintWriter(brokerSocket.getOutputStream(), true);
        brokerInput = new BufferedReader(
                new InputStreamReader(brokerSocket.getInputStream())
        );
    }

}



