package broker;

import broker.entities.*;
import broker.exceptions.ServerErrorException;
import broker.exceptions.ServiceAlreadyDefinedException;
import broker.exceptions.ServiceNotAvailableException;
import broker.exceptions.ServiceNotFoundException;
import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * Created by luisburgos on 21/09/15.
 */
public class BrokerThread implements Runnable {

    private Broker mBroker;
    private Socket mSocketBroker;
    private Socket mProxyServerSocket;
    private static int totalThreads = 0;

    private PrintWriter clientOut;
    private BufferedReader clientIn;
    private PrintWriter serverOut;
    private BufferedReader servertIn;

    public BrokerThread(Broker broker, Socket socket){
        this.mBroker = broker;
        this.mSocketBroker = socket;
        System.out.println("thread #" + ++totalThreads + " on Broker");
    }

    public void run(){
        try{
            initializeBuffersToClient();
            connect();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void connect() throws IOException {
        final int currentThread = totalThreads;

        String inputLine, processedInputLine;
        Protocol protocol = new Protocol();
        processedInputLine = protocol.processInput(null);
        clientOut.println(processedInputLine);

        while((inputLine = clientIn.readLine()) != null ){

            processedInputLine = protocol.processInput(inputLine);
            System.out.println("Current thread #" + currentThread + " requests: " + processedInputLine);

            BrokerRequest brokerRequest = new Gson().fromJson(processedInputLine, BrokerRequest.class);

            if(brokerRequest.getType() == TypesBrokerRequest.FIND_SERVICE){
                try {
                    Service serviceRequested;
                    serviceRequested = mBroker.findService(brokerRequest.getServiceName());
                    if(serviceRequested != null){
                        BrokerResponse brokerResponse = new BrokerResponse();
                        brokerResponse.setType(TypesBrokerResponse.SERVICE_FOUND);
                        clientOut.println(new Gson().toJson(brokerResponse));
                    }
                } catch (ServiceNotFoundException e) {
                    BrokerResponse brokerResponse = new BrokerResponse();
                    brokerResponse.setType(TypesBrokerResponse.SERVICE_NOT_FOUND);
                    clientOut.println(new Gson().toJson(brokerResponse));
                    break;
                }

            }

            if(brokerRequest.getType() == TypesBrokerRequest.EXECUTE_SERVICE){
                Service serviceToExecute;
                try {
                    serviceToExecute = mBroker.findService(brokerRequest.getServiceName());
                    if(!serviceToExecute.isActive()){
                        throw new ServiceNotAvailableException();
                    }
                } catch (ServiceNotFoundException e) {
                    BrokerResponse brokerResponse = new BrokerResponse();
                    brokerResponse.setType(TypesBrokerResponse.SERVICE_NOT_FOUND);
                    clientOut.println(new Gson().toJson(brokerResponse));
                    break;
                } catch (ServiceNotAvailableException e) {
                    BrokerResponse brokerResponse = new BrokerResponse();
                    brokerResponse.setType(TypesBrokerResponse.SERVICE_NOT_AVAILABLE);
                    clientOut.println(new Gson().toJson(brokerResponse));
                    break;
                }
                String data = brokerRequest.getData();
                startServiceExecution(serviceToExecute, data);
                break;
            }

            if(brokerRequest.getType() == TypesBrokerRequest.REGISTER_SERVICE){
                Service service = new Service(
                        mSocketBroker.getLocalAddress().getHostName(),
                        Integer.parseInt(brokerRequest.getData()),
                        brokerRequest.getServiceName());
                try {
                    mBroker.registerService(service);
                } catch (ServiceAlreadyDefinedException e) {
                    BrokerResponse brokerResponse = new BrokerResponse();
                    brokerResponse.setType(TypesBrokerResponse.SERVICE_ALREADY_DEFINED);
                    clientOut.println(new Gson().toJson(brokerResponse));
                }
                break;
            }

            if(brokerRequest.getType() == TypesBrokerRequest.CHANGE_SERVICE_STATUS){
                try {
                    mBroker.changeServiceState(
                            brokerRequest.getServiceName(),
                            Boolean.parseBoolean(brokerRequest.getData()));
                } catch (ServiceNotFoundException e) {
                    BrokerResponse brokerResponse = new BrokerResponse();
                    brokerResponse.setType(TypesBrokerResponse.SERVICE_NOT_FOUND);
                    clientOut.println(new Gson().toJson(brokerResponse));
                }
                break;
            }

            if(processedInputLine.equals("Close.")){
                break;
            }

        }

        mSocketBroker.close();
        System.out.println("Disconnect current thread #" + currentThread);
    }

    private void startServiceExecution(Service serviceToExecute, String data){

        String hostname = serviceToExecute.getIp();
        int port = serviceToExecute.getPort();

        try {
            connectToProxyServer(hostname, port);
            initializeBuffersToServer();
            sendRequestExecution(serviceToExecute.getService(), data);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ServerErrorException e) {
            System.err.println(e.getMessage());
        }

    }

    private void sendRequestExecution(String serviceName, String data) throws IOException, ServerErrorException {

        String responseFromProxyServer;
        BrokerRequest initialBrokerRequest;

        while ((responseFromProxyServer = servertIn.readLine()) != null) {

            System.out.println("ProxyServer brokerResponse: " + responseFromProxyServer);

            BrokerResponse brokerResponse =  new Gson().fromJson(responseFromProxyServer, BrokerResponse.class);
            int responseType = brokerResponse.getType();

            if(responseType == TypesBrokerResponse.CONNECTED){
                initialBrokerRequest = new BrokerRequest();
                initialBrokerRequest.setType(TypesBrokerRequest.EXECUTE_SERVICE);
                initialBrokerRequest.setServiceName(serviceName);
                initialBrokerRequest.setData(data);
                serverOut.println(new Gson().toJson(initialBrokerRequest));
            }

            if(responseType == TypesBrokerResponse.REQUEST_RECEIVED){
                System.out.println(brokerResponse.getMessage());
                break;
            }

            if(responseType == TypesBrokerResponse.REQUEST_ERROR){
                throw new ServerErrorException();
            }

        }
        mProxyServerSocket.close();
    }

    private void connectToProxyServer(String hostname, int portNumber) throws IOException {
        mProxyServerSocket = new Socket(hostname, portNumber);
    }

    private void initializeBuffersToClient() throws IOException {
        clientOut = new PrintWriter(mSocketBroker.getOutputStream(), true);
        clientIn = new BufferedReader(
                new InputStreamReader(mSocketBroker.getInputStream())
        );
    }

    private void initializeBuffersToServer() throws IOException {
        serverOut = new PrintWriter(mProxyServerSocket.getOutputStream(), true);
        servertIn = new BufferedReader(
                new InputStreamReader(mProxyServerSocket.getInputStream())
        );
    }

}
