package broker;

import broker.entities.Request;
import broker.entities.Response;
import broker.entities.Service;
import broker.exceptions.ServiceNotFoundException;
import broker.utils.BrokerActions;
import broker.utils.ResponseTypes;
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

            Request request = new Gson().fromJson(processedInputLine, Request.class);

            if(request.getType() == BrokerActions.FIND_SERVICE){
                try {
                    if(mBroker.findService(request.getServiceName()) != null){
                        Response response = new Response();
                        response.setType(ResponseTypes.SERVICE_FOUND);
                        clientOut.println(new Gson().toJson(response));
                    }
                } catch (ServiceNotFoundException e) {
                    Response response = new Response();
                    response.setType(ResponseTypes.SERVICE_NOT_FOUND);
                    clientOut.println(new Gson().toJson(response));
                    break;
                }
            }

            if(request.getType() == BrokerActions.EXECUTE_SERVICE){
                Service serviceToExecute = null;
                try {
                    serviceToExecute = mBroker.findService(request.getServiceName());
                } catch (ServiceNotFoundException e) {
                    e.printStackTrace();
                }
                startServiceExecution(serviceToExecute);
                break;
            }

            if(request.getType() == BrokerActions.REGISTER_SERVICE){
                Service service = new Gson().fromJson(request.getCandidateId(), Service.class);
                mBroker.registerService(service);
                break;
            }

            if(processedInputLine.equals("Close.")){
                break;
            }

        }

        mSocketBroker.close();
        System.out.println("Disconnect current thread #" + currentThread);
    }

    private void startServiceExecution(Service serviceToExecute){

        String hostname = serviceToExecute.getIp();
        int port         = (int)serviceToExecute.getPort();

        try {
            connectToProxyServer(hostname, port);
            initializeBuffersToServer();
            sendRequestExecution(serviceToExecute.getService());
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void sendRequestExecution(String serviceName) throws IOException {
        String responseFromProxyServer;
        
        while ((responseFromProxyServer = servertIn.readLine()) != null) {

            System.out.println("ProxyServer response: " + responseFromProxyServer);
            serverOut.println(serviceName);

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
