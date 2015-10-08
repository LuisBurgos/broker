package client;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.lang.ref.SoftReference;
import java.net.Socket;

/**
 * Created by luisburgos on 21/09/15.
 */
public class ProxyClient {

    private static final int BROKER_PORT_NUMBER = 5555;
    private static final String BASE_HOSTNAME = "localhost";
    private String entity;

    Socket clientSocket;
    PrintWriter clientOutput;
    BufferedReader clientInput;

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
        clientSocket = new Socket(BASE_HOSTNAME, BROKER_PORT_NUMBER);
    }

    private void initializeBuffers() throws IOException {
        clientOutput = new PrintWriter(clientSocket.getOutputStream(), true);
        clientInput = new BufferedReader(
                new InputStreamReader(clientSocket.getInputStream())
        );
    }

    private void startProcessing() throws IOException {

        BufferedReader userInput = new BufferedReader(
                new InputStreamReader(System.in)
        );

        String brokerResponse, fromUser;

        while ((brokerResponse = clientInput.readLine()) != null) {

            System.out.println("Broker: " + brokerResponse);
            if (brokerResponse.equals("Close.")){
                break;
            }

            fromUser = userInput.readLine();
            if (fromUser != null) {
                if(fromUser.equals("send")){
                    clientOutput.println(entity);
                    System.out.println("SEND: " + entity);
                }
                System.out.println("ProxyClient: " + fromUser);
                clientOutput.println(fromUser);
            }
        }
    }

    private void packData(String serviceName, String candidateId){
        JsonObject json = new JsonObject();
        json.addProperty("type", 1);
        json.addProperty("serviceName", serviceName);
        json.addProperty("candidateId", candidateId);
        entity = json.toString();
        try {
            forwardRequest(entity);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ServiceNotFoundException e) {
            //e.printStackTrace();
            System.out.println(e.getMessage());
        }
    }

    private void forwardRequest(String request) throws IOException, ServiceNotFoundException {
        String brokerResponse;

        System.out.println(request);

        while ((brokerResponse = clientInput.readLine()) != null) {

            System.out.println("Broker: " + brokerResponse);
            if (brokerResponse.equals("Close.")){
                break;
            }

            if(brokerResponse.equals("Service not found")){
                throw new ServiceNotFoundException();
            }

            if(brokerResponse.equals("Service FOUND")){
                break;
            }

            if (request != null) {
                clientOutput.println(request);
            }
        }
        clientSocket.close();
    }

    private Response unpackData(String responseFromBroker){
        Response response;
        response = new Gson().fromJson(responseFromBroker, Response.class);
        return response;
    }

    public void sendRequest(String request, String candidateId){
        build();
        packData(request, candidateId);
    }
}
