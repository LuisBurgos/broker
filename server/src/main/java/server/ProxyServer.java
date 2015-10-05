package server;

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

    Socket brokerSocket;
    PrintWriter brokerOutput;
    BufferedReader brokerInput;

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

    private void connectToBroker() throws IOException {
        brokerSocket = new Socket(BASE_HOSTNAME, BROKER_PORT_NUMBER);
    }

    private void initializeBuffers() throws IOException {
        brokerOutput = new PrintWriter(brokerSocket.getOutputStream(), true);
        brokerInput = new BufferedReader(
                new InputStreamReader(brokerSocket.getInputStream())
        );
    }


    public void callService(){
        brokerOutput.println("{\"ip\":localhost\"2222\",\"port\":\"2222\",\"service\":\"addVoteToCandidateById\"}");
    }

    public void sendResponse(){

    }

    public void packData(){

    }

    public void unpackData(){

    }

}



