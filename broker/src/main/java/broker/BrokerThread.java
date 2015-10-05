package broker;

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
            initializeBuffers();
            connect();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void connect() throws IOException {
        final int currentThread = totalThreads;

        String inputLine, outputLine;
        Protocol protocol = new Protocol();
        outputLine = protocol.processInput(null);
        clientOut.println(outputLine);

        while((inputLine = clientIn.readLine()) != null ){

            outputLine = protocol.processInput(inputLine);
            System.out.println("Current thread #" + currentThread +" requests: " + outputLine);

            if(outputLine.equals("Close.")){
                break;
            }

            try {
                if(mBroker.findService(outputLine) != null){
                    clientOut.println("Service FOUND");
                }
            } catch (ServiceNotFoundException e) {
                clientOut.println("Service not found");
            }
        }

        mSocketBroker.close();
        System.out.println("Disconnect current thread #" + currentThread);
    }

    private void initializeBuffers() throws IOException {
        clientOut = new PrintWriter(mSocketBroker.getOutputStream(), true);
        clientIn = new BufferedReader(
                new InputStreamReader(mSocketBroker.getInputStream())
        );
    }

}
