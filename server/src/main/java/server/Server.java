package server;

import java.io.IOException;
import java.net.ServerSocket;

/**
 * Created by luisburgos on 21/09/15.
 */
public class Server {

    public static final int PORT_NUMBER_SERVER = 2222;

    private ProxyServer proxyServer = new ProxyServer();
    private ServerSocket socketServer;

    public void start(){

    }

    public void initializeSocket() throws IOException {
        socketServer = new ServerSocket(PORT_NUMBER_SERVER);
    }

    public void startAcceptingPetitions(){
        boolean listening = true;

        try {
            while (listening) {
                new Thread(new ServerThread(socketServer.accept())).start();;
            }
        } catch (IOException e) {
            System.err.println("Could not listen on port " + PORT_NUMBER_SERVER);
            System.exit(-1);
        }
    }

    public void runService(){

    }

    /**
     * Use BROKER API
     */
    public void registerServiceIntoBroker(){
        proxyServer.build();
        proxyServer.registerServiceToBroker();
    }

    public static void main(String[] args) {
        Server server = new Server();
        try {
            server.initializeSocket();
            server.registerServiceIntoBroker();
            server.startAcceptingPetitions();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


}
