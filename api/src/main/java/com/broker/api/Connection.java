package com.broker.api;

import com.broker.api.entities.BrokerResponse;
import com.broker.api.exceptions.*;
import com.google.gson.Gson;
import com.broker.api.entities.TypesBrokerRequest;
import com.broker.api.entities.BrokerRequest;
import com.broker.api.entities.TypesBrokerResponse;

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
        setIp(ip);
        setPort(port);
    }

    public void open() throws BrokerConnectionErrorException, IOException {
        connectToBroker();
        initializeBuffers();
    }

    public void close() throws BrokerConnectionErrorException, IOException {
        socket.close();
    }

    public void registerService(int serverPort, String serviceName) throws BrokerConnectionErrorException,
                                                                           ServiceAlreadyDefinedException{

    }

    public void changeServiceState(String serviceName, boolean status) throws BrokerConnectionErrorException,
                                                                              ServiceNotFoundException {

    }

    public void findService(String serviceName) throws BrokerConnectionErrorException, ServiceNotFoundException {

    }

    public void executeService(String serviceName, String jsonData) throws ServiceNotFoundException,
                                                                           InvalidDataFormatException,
                                                                           BrokerConnectionErrorException,
                                                                           ServiceNotAvailableException {
        //Code to execute.

    }

    public void forwardRequest(String request) throws BrokerException, IOException {
        //Todo: Change to correct exception.
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
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    private BrokerResponse unpackData(String responseFromBroker){
        BrokerResponse brokerResponse;
        brokerResponse = new Gson().fromJson(responseFromBroker, BrokerResponse.class);
        return brokerResponse;
    }

    private void connectToBroker() throws IOException {
        socket = new Socket(getIp(), getPort());
    }

    private void initializeBuffers() throws IOException {
        socketOutput = new PrintWriter(socket.getOutputStream(), true);
        socketInput = new BufferedReader(
                new InputStreamReader(socket.getInputStream())
        );
    }


}
