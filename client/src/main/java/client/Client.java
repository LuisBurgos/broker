package client;

import java.util.Scanner;

/**
 * Created by luisburgos on 2/10/15.
 */
public class Client {

    private ProxyClient proxyClient = new ProxyClient();

    public void addVoteToCandidateById(int id) {
        proxyClient.sendRequest("addVoteToCandidateById", String.valueOf(id));
    }

    public void addNewCandidate(String candidateName) {
        proxyClient.sendRequest("newCandidate", candidateName);
    }

    public static void main(String[] args) {

        Client client = new Client();

        String n;

        Scanner input = new Scanner(System.in);
        System.out.println("Input an service");

        while ((n = input.nextLine()) != null) {
            if(n.equals("vote")){
                client.addVoteToCandidateById(1);
            }

            if(n.equals("new")){
                client.addNewCandidate("Dave Grohl");
            }

            if(n.equals("close")){
                break;
            }
        }

    }

}
