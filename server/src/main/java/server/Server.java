package server;

import java.io.IOException;
import java.net.ServerSocket;

/**
 * Created by luisburgos on 21/09/15.
 */
public class Server {

    private static int portNumber = 2222;

    public static void main(String[] args) throws IOException {

        boolean listening = true;

        try (ServerSocket serverSocket = new ServerSocket(portNumber)) {
            while (listening) {
                new ServerThread(serverSocket.accept()).start();
            }
        } catch (IOException e) {
            System.err.println("Could not listen on port " + portNumber);
            System.exit(-1);
        }
    }

}
