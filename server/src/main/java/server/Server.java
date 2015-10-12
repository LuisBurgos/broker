package server;

import server.events.Event;
import server.events.NewCandidate;
import server.events.Vote;
import server.model.Model;
import server.model.Votations;
import server.views.BarChartView;
import server.views.PieChartView;

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
                new Thread(new ServerThread(socketServer.accept(),proxyServer)).start();;
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

        Event voteEvent         = new Vote("New vote");
        Event newCandidateEvent = new NewCandidate("New candidate");

        //Votations
        Model votations = Votations.getInstance(); //Initialize the model loads the data.

        //Views
        PieChartView pieChartView = new PieChartView();
        BarChartView barChartView = new BarChartView();

        //Register
        votations.register(voteEvent, pieChartView);
        votations.register(voteEvent, barChartView);

        votations.register(newCandidateEvent, pieChartView);
        votations.register(newCandidateEvent, barChartView);

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
