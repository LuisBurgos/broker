package broker;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by luisburgos on 21/09/15.
 */
public class BrokerThread implements Runnable {

    private Socket socket = null;
    private static int serverPortNumber = 2222;
    private static String serverHostName = "localhost";
    private static int contadorDeThreads = 1;

    public BrokerThread(Socket socket){
        this.socket = socket;
        System.out.println("thread #"+ contadorDeThreads + " on Broker");
        contadorDeThreads++;
    }

    public void connect(ServerSocket client){
        //Genera un socket
    }

    public void run(){
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

            while((inputLine = clientIn.readLine()) != null ){

                outputLine = protocol.processInput(inputLine);
                System.out.println(outputLine);
                clientOut.println(outputLine);
                if(outputLine.equals("Bye.")){
                    break;
                }
            }
            socket.close();

        }catch (IOException e) {
            e.printStackTrace();
        }
    }

}
