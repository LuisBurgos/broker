package broker;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by luisburgos on 21/09/15.
 */
public class Broker {


 public static void main(String[] args) throws IOException {
         int portNumber = 8888;
         
       
         ServerSocket serverSocket = new ServerSocket(portNumber);
       
         while(true){
           Socket clientSocket = serverSocket.accept();
           new BrokerThread(clientSocket).start();
        }
    }

}