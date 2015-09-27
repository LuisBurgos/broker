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
                Socket serverSocket = new Socket(serverHostName, serverPortNumber);
                PrintWriter serverOut = new PrintWriter(serverSocket.getOutputStream(), true);
                BufferedReader serverIn = new BufferedReader(
                        new InputStreamReader(serverSocket.getInputStream()));

                PrintWriter clientOut = new PrintWriter(socket.getOutputStream(), true);
                BufferedReader clientIn = new BufferedReader(
                        new InputStreamReader(
                                socket.getInputStream()));
        ) {
            //BufferedReader stdIn =
              //      new BufferedReader(new InputStreamReader(System.in));
            String fromServer;
            String fromClient;

            while((fromServer = serverIn.readLine()) != null || (fromClient = clientIn.readLine()) != null ){

                fromServer = serverIn.readLine();
                if(fromServer != null){
                    //outputLine = kkp.processInput(fromServer);
                    //System.out.println("fromServer: " + outputLine);
                    System.out.println(fromServer);
                    clientOut.println(fromServer);

                    //if (outputLine.equals("Bye"))
                    //    break;
                }

                fromClient = clientIn.readLine();
                if(fromClient != null){
                    //outputLine = kkp.processInput(fromClient);
                    //System.out.println("fromClient: " + outputLine);
                    System.out.println(fromClient);
                    serverOut.println(fromClient);
                }

            }
            socket.close();

        }catch (IOException e) {
            e.printStackTrace();
        }
    }

}
