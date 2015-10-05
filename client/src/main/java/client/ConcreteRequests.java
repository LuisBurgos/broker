package client;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Scanner;

/**
 * Created by luisburgos on 2/10/15.
 */
public class ConcreteRequests {

    private Client api;

    public ConcreteRequests(){
        api = new Client();
    }


    public void addNewCandidate(String candidateName){
        api.addNewCandidate(candidateName);
    }

    public void addVoteToCandidateById(int id){
        api.addVoteToCandidateById(id);
    }

    /*public static void main(String[] args) {

        ConcreteRequests concreteRequests = new ConcreteRequests();

        String n;

        Scanner input = new Scanner(System.in);
        System.out.println("Input an service");

        while ((n = input.nextLine()) != null) {
            if(n.equals("vote")){
                concreteRequests.addVoteToCandidateById(1);
            }

            if(n.equals("new")){
                concreteRequests.addNewCandidate("Dave Grohl");
            }

            if(n.equals("close")){
                break;
            }
        }

    }*/


}
