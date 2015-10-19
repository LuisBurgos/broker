package com.broker.api;

import com.broker.api.entities.BrokerResponse;
import com.broker.api.exceptions.*;
import com.google.gson.Gson;
import com.broker.api.entities.TypesBrokerRequest;
import com.broker.api.entities.BrokerRequest;
import com.broker.api.entities.TypesBrokerResponse;
import com.google.gson.JsonObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * Created by luisburgos on 15/10/15.
 */
public class Connection {

    private String ip;
    private int port;

    private Socket socket;
    private PrintWriter socketOutput;
    private BufferedReader socketInput;

    public Connection(String ip, int port) {
        this.ip = ip;
        this.port = port;
    }

    public void open() throws BrokerConnectionErrorException, IOException {
        connectToBroker();
        initializeBuffers();
    }

    public void close() throws BrokerConnectionErrorException, IOException {
        socket.close();
    }

    public void registerService(int serverPort, String serviceName) throws BrokerConnectionErrorException,
                                                                           ServiceAlreadyDefinedException,
                                                                           IOException {
        String forwardrequest = packData(
                TypesBrokerRequest.REGISTER_SERVICE,
                serviceName,
                String.valueOf(serverPort)
        );
        socketOutput.println(forwardrequest);

        String responseFromBroker;
        while ((responseFromBroker = socketInput.readLine()) != null) {
            BrokerResponse response = unpackData(responseFromBroker);

            if(response.getType() == TypesBrokerResponse.SERVICE_ALREADY_DEFINED){
                throw new ServiceAlreadyDefinedException();
            }

        }
    }

    public void changeServiceState(String serviceName, boolean status) throws BrokerConnectionErrorException,
                                                                              ServiceNotFoundException,
                                                                              IOException {
        String forwardrequest = packData(
                TypesBrokerRequest.CHANGE_SERVICE_STATUS,
                serviceName,
                String.valueOf(status)
        );
        socketOutput.println(forwardrequest);


        String responseFromBroker;
        while ((responseFromBroker = socketInput.readLine()) != null) {
            BrokerResponse response = unpackData(responseFromBroker);

            if(response.getType() == TypesBrokerResponse.SERVICE_NOT_FOUND){
                throw new ServiceNotFoundException();
            }

        }
    }

    public boolean findService(String serviceName) throws BrokerConnectionErrorException,
                                                       IOException {

        String forwardrequest = packData(
                TypesBrokerRequest.FIND_SERVICE,
                serviceName,
                null
        );
        socketOutput.println(forwardrequest);

        String responseFromBroker;
        boolean serviceFound = false;
        while ((responseFromBroker = socketInput.readLine()) != null) {
            BrokerResponse response = unpackData(responseFromBroker);

            if(response.getType() == TypesBrokerResponse.SERVICE_FOUND){
                serviceFound = true;
                break;
            }

        }
        return serviceFound;
    }

    public void executeService(String serviceName, String jsonData) throws ServiceNotFoundException,
                                                                           InvalidDataFormatException,
                                                                           BrokerConnectionErrorException,
                                                                           ServiceNotAvailableException,
                                                                           IOException {
        String forwardrequest = packData(
                TypesBrokerRequest.EXECUTE_SERVICE,
                serviceName,
                jsonData
        );
        socketOutput.println(forwardrequest);

        String responseFromBroker;
        while ((responseFromBroker = socketInput.readLine()) != null) {
            BrokerResponse response = unpackData(responseFromBroker);
            if(response.getType() == TypesBrokerResponse.SERVICE_NOT_AVAILABLE){
                throw new ServiceNotAvailableException();
            }

            //Added to new version
            if(response.getType() == TypesBrokerResponse.SERVICE_NOT_FOUND){
                throw new ServiceNotFoundException();
            }

        }
    }

    public void forwardRequest(String request) throws BrokerException, IOException {
        /*open();

        int requestType = request.getType();

        String responseFromBroker;

        while ((responseFromBroker = socketInput.readLine()) != null) {

            System.out.println("Broker brokerResponse: " + responseFromBroker);

            BrokerResponse brokerResponse = unpackData(responseFromBroker);
            int responseType = brokerResponse.getType();

            if (responseType == TypesBrokerResponse.DISCONNECTED){
                break;
            }

            if(responseType == TypesBrokerResponse.SERVICE_NOT_FOUND){
                throw new ServiceNotFoundException();
            }

            if(responseType == TypesBrokerResponse.SERVICE_FOUND){
                Gson gson = new Gson();
                BrokerRequest brokerRequestHolder = gson.fromJson(request, BrokerRequest.class);
                brokerRequestHolder.setType(TypesBrokerRequest.EXECUTE_SERVICE);
                String newRequestEntity = gson.toJson(brokerRequestHolder);
                socketOutput.println(newRequestEntity);
            }

            if (responseType == TypesBrokerResponse.CONNECTED) {
                socketOutput.println(request);
            }
        }
        close();*/
    }

    private BrokerResponse unpackData(String responseFromBroker){
        BrokerResponse brokerResponse;
        brokerResponse = new Gson().fromJson(responseFromBroker, BrokerResponse.class);
        return brokerResponse;
    }

    private String packData(int type, String serviceName, String data){
        String entity;
        JsonObject json = new JsonObject();
        json.addProperty("type", type);
        json.addProperty("serviceName", serviceName);
        json.addProperty("data", data);
        entity = json.toString();
        return entity;
    }

    private void connectToBroker() throws IOException {
        socket = new Socket(ip, port);
    }

    private void initializeBuffers() throws IOException {
        socketOutput = new PrintWriter(socket.getOutputStream(), true);
        socketInput = new BufferedReader(
                new InputStreamReader(socket.getInputStream())
        );
    }


}
