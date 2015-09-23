package broker;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by luisburgos on 21/09/15.
 */
public class BrokerThread extends Thread{
    
    private Socket socket;
    
    public BrokerThread(Socket socket){
         super("BrokerThread");
        this.socket = socket;
    }
    
    public void run(){
        try {
            
            Socket serverSocket = new Socket("Server.server.com", 2222);
            PrintWriter serverOut = new PrintWriter(serverSocket.getOutputStream(), true);
            BufferedReader serverIn = new BufferedReader(new InputStreamReader(serverSocket.getInputStream()));

            
            PrintWriter out = new PrintWriter(socket.getOutputStream());
            BufferedReader in = new BufferedReader(
                    new InputStreamReader(
                            socket.getInputStream()));
            
            KnockKnockProtocol kkp = new KnockKnockProtocol();
            
            String fromServer= null, fromClient= null;
            
            while((fromServer = serverIn.readLine()) != null || (fromClient = in.readLine()) != null ){
                
                if(fromServer != null){
                    String outputLine = kkp.processInput(fromServer);
                    out.println(outputLine);
                }
                
                if(fromClient != null){
                    String outputLine = kkp.processInput(fromClient);
                    serverOut.println(outputLine);
                }
                
            }
            
            
            
        } catch (IOException ex) {
            Logger.getLogger(BrokerThread.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
}
