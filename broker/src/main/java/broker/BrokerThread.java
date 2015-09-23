package broker;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by luisburgos on 21/09/15.
 */
public class BrokerThread extends Thread{

    private Socket socket = null;
    private static int serverPortNumber = 2222;
    private static String serverHostName = "localhost";


    public BrokerThread(Socket socket){
        super("BrokerThread");
        this.socket = socket;
        System.out.println("new thread on Broker");
    }

    public void run(){
        try (
                Socket serverSocket = new Socket(serverHostName, serverPortNumber);
                PrintWriter serverOut = new PrintWriter(serverSocket.getOutputStream(), true);
                BufferedReader serverIn = new BufferedReader(
                        new InputStreamReader(serverSocket.getInputStream()));

                PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
                BufferedReader in = new BufferedReader(
                        new InputStreamReader(
                                socket.getInputStream()));
        ) {
            BufferedReader stdIn =
                    new BufferedReader(new InputStreamReader(System.in));
            String fromServer;
            String fromClient;

            String inputLine, outputLine;
            Protocol kkp = new Protocol();
            outputLine = kkp.processInput(null);
            out.println(outputLine);

            while((fromServer = serverIn.readLine()) != null || (fromClient = in.readLine()) != null ){

                if(fromServer != null){
                    outputLine = kkp.processInput(fromServer);
                    System.out.println("fromServer: " + outputLine);
                    out.println(outputLine);
                    if (outputLine.equals("Bye"))
                        break;
                }

                fromClient = stdIn.readLine();
                if(fromClient != null){
                    outputLine = kkp.processInput(fromClient);
                    System.out.println("fromClient: " + outputLine);
                    serverOut.println(outputLine);
                }

            }


            /*while ((inputLine = in.readLine()) != null) {
                outputLine = kkp.processInput(inputLine);
                out.println(outputLine);
                if (outputLine.equals("Bye"))
                    break;
            }*/

            socket.close();

        }catch (IOException e) {
            e.printStackTrace();
        }
    }

}
