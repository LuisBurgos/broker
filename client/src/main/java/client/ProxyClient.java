package client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

/**
 * Created by luisburgos on 21/09/15.
 */
public class ProxyClient {

    private static int portNumber = 5555;
    private static String hostName = "localhost";

    public static void main(String[] args) throws IOException {

        try (
                Socket clientSocket = new Socket(hostName, portNumber);
                PrintWriter clientOutput = new PrintWriter(clientSocket.getOutputStream(), true);
                BufferedReader clientInput = new BufferedReader(
                        new InputStreamReader(clientSocket.getInputStream())
                );
        ) {
            BufferedReader userInput = new BufferedReader(
                    new InputStreamReader(System.in)
            );

            String brokerResponse, fromUser;

            while ((brokerResponse = clientInput.readLine()) != null) {
                System.out.println("Broker: " + brokerResponse);
                if (brokerResponse.equals("Bye.")){
                    System.out.println("Desconectado...");
                    break;
                }

                fromUser = userInput.readLine();
                if (fromUser != null) {
                    System.out.println("ProxyClient: " + fromUser);
                    clientOutput.println(fromUser);
                }
            }

        } catch (UnknownHostException e) {
            System.err.println("Don't know about host " + hostName);
            System.exit(1);
        } catch (IOException e) {
            System.err.println("Couldn't get I/O for the connection to " + hostName);
            e.printStackTrace();
            System.exit(1);
        }
    }


}
