package server;

import com.google.gson.Gson;
import server.model.entities.Request;
import server.model.entities.Response;
import server.utils.BrokerActions;
import server.utils.Protocol;
import server.utils.ResponseTypes;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * Created by luisburgos on 21/09/15.
 */
public class ServerThread implements Runnable {

    private Socket mSocketServer;
    private ProxyServer mProxyServer;
    private static int totalThreads = 0;

    private PrintWriter socketOutput;
    private BufferedReader socketInput;

    public ServerThread(Socket socket, ProxyServer proxyServer) {
        this.mSocketServer = socket;
        this.mProxyServer = proxyServer;
        System.out.println("thread #"+ (++totalThreads) + " on Server");
    }

    public void run() {
        try {
            initializeBuffers();
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
        System.out.println(processedInputLine);
        socketOutput.println(processedInputLine);

        while ((inputLine = socketInput.readLine()) != null) {

            processedInputLine = protocol.processInput(inputLine);
            System.out.println("Current thread #" + currentThread + " requests: " + processedInputLine);

            Request request = new Gson().fromJson(processedInputLine, Request.class);

            if(request.getType() == BrokerActions.EXECUTE_SERVICE){
                Response response = new Response();
                response.setType(ResponseTypes.REQUEST_RECEIVED);
                response.setMessage("Request received");
                String responseString = new Gson().toJson(response);
                System.out.println(responseString);
                socketOutput.println(responseString);
                mProxyServer.callService(
                        request.getServiceName(),
                        Integer.parseInt(request.getData())
                );
                break;
            }

        }

        mSocketServer.close();
        System.out.println("Disconnect current thread #" + currentThread);
    }

    private void initializeBuffers() throws IOException {
        socketOutput = new PrintWriter(mSocketServer.getOutputStream(), true);
        socketInput = new BufferedReader(
                new InputStreamReader(mSocketServer.getInputStream())
        );
    }

}
