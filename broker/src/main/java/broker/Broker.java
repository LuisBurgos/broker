package broker;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by luisburgos on 21/09/15.
 */
public class Broker {

    private static int portNumber = 5555;

    public static void main(String[] args) throws IOException {

        boolean listening = true;

        try (ServerSocket serverSocket = new ServerSocket(portNumber)) {
            while (listening) {
                new Thread(new BrokerThread(serverSocket.accept())).start();;
            }
        } catch (IOException e) {
            System.err.println("Could not listen on port " + portNumber);
            System.exit(-1);
        }

    }

}