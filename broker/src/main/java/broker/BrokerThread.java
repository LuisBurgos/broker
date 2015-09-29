package broker;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;

/**
 * Created by luisburgos on 21/09/15.
 */
public class BrokerThread implements Runnable {

    private Broker broker;
    private Socket socket;
    private static int totalThreads = 0;

    public BrokerThread(Broker broker, Socket socket){
        this.broker = broker;
        this.socket = socket;
        System.out.println("thread #"+ ++totalThreads + " on Broker");
    }

    public void run(){
        connect();
    }

    private void connect(){
        try (
                PrintWriter clientOut =
                        new PrintWriter(socket.getOutputStream(), true);
                BufferedReader clientIn =
                        new BufferedReader(
                                new InputStreamReader(socket.getInputStream())
                        );
        ) {

            String inputLine, outputLine;
            Protocol protocol = new Protocol();
            outputLine = protocol.processInput(null);
            clientOut.println(outputLine);

            final int currentThread = totalThreads;

            while((inputLine = clientIn.readLine()) != null ){

                outputLine = protocol.processInput(inputLine);
                System.out.println("Current thread #" + currentThread +" requests: " + outputLine);

                if(outputLine.equals("Close.")){
                    break;
                }

                try {
                    if(broker.findService(outputLine) != null){
                        clientOut.println("Service FOUND");
                    }
                } catch (ServiceNotFoundException e) {
                    clientOut.println("Service not found");
                }

            }
            socket.close();

        }catch (IOException e) {
            e.printStackTrace();
        }
    }

}
