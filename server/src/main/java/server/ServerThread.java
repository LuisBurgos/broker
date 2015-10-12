package server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * Created by luisburgos on 21/09/15.
 */
public class ServerThread implements Runnable {

    private Socket socket = null;
    private static int contadorDeThreads = 1;

    private PrintWriter socketOut;
    private BufferedReader socketIn;

    public ServerThread(Socket socket) {
        this.socket = socket;
        System.out.println("thread #"+ contadorDeThreads + " on Server");
        contadorDeThreads++;
    }

    private void connect(){
        try {
            initializeBuffers();
            String inputLine, outputLine;
            //Protocol kkp = new Protocol();
            //outputLine = kkp.processInput(null);
            //socketOut.println(outputLine);

            while ((inputLine = socketIn.readLine()) != null) {
                //outputLine = kkp.processInput(inputLine);
                System.out.println(inputLine);
                socketOut.println(inputLine);
                //if (outputLine.equals("Bye"))
                //  break;
            }

            socket.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void run() {
        connect();
    }

    private void initializeBuffers() throws IOException {
        socketOut = new PrintWriter(socket.getOutputStream(), true);
        socketIn = new BufferedReader(
                new InputStreamReader(socket.getInputStream())
        );
    }

}
