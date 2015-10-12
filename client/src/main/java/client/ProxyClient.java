package client;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import org.omg.CORBA.Request;

import javax.swing.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * Created by luisburgos on 21/09/15.
 */
public class ProxyClient {

    private static final int BROKER_PORT_NUMBER = 5555;
    private static final String BASE_HOSTNAME = "localhost";

    private Socket clientSocket;
    private PrintWriter clientOutput;
    private BufferedReader clientInput;

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

    public void sendRequest(int type, String serviceName, String data){
        build();
        String request;
        request = packData(type, serviceName, data);
        sendRequest(request);
    }

    public void sendRequest(String forwardRequest){
        try {
            startRequestProcessing(forwardRequest);
        } catch (IOException e) {
            System.err.println(e.getMessage());
        } catch (ServiceNotFoundException e) {
            final JPanel panel = new JPanel();
            JOptionPane.showMessageDialog(panel, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
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

    private String packData(int type, String serviceName, String candidateId){
        String entity;
        JsonObject json = new JsonObject();
        json.addProperty("type", type);
        json.addProperty("serviceName", serviceName);
        json.addProperty("candidateId", candidateId);
        entity = json.toString();
        return entity;
    }

    private Response unpackData(String responseFromBroker){
        Response response;
        response = new Gson().fromJson(responseFromBroker, Response.class);
        return response;
    }

    private void startRequestProcessing(String request) throws IOException, ServiceNotFoundException {

        String responseFromBroker;

        while ((responseFromBroker = clientInput.readLine()) != null) {

            System.out.println("Broker response: " + responseFromBroker);

            Response response = unpackData(responseFromBroker);
            int responseType = response.getType();

            if (responseType == ResponseTypes.DISCONNECTED){
                break;
            }

            if(responseType == ResponseTypes.SERVICE_NOT_FOUND){
                throw new ServiceNotFoundException();
            }

            if(responseType == ResponseTypes.SERVICE_FOUND){
                String executeRequest;
                executeRequest = packData(BrokerActions.EXECUTE_SERVICE,
                        "addVoteToCandidateById", "1");
                clientOutput.println(executeRequest);
            }

            if (responseType == ResponseTypes.CONNECTED) {
                clientOutput.println(request);
            }
        }
        clientSocket.close();
    }

}
