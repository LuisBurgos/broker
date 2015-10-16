package client;

import com.broker.api.entities.TypesBrokerRequest;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;

/**
 * Created by luisburgos on 11/10/15.
 */
public class VotesClient {

    private final String SERVICE_ADD_VOTE = "addVoteToCandidateById";

    private ProxyClient proxyClient;
    private JFrame      mainFrame;
    private JPanel      mainPanel;

    public VotesClient(){
        prepareGUI();
        proxyClient  = new ProxyClient();
    }

    public void addVoteToCandidateById(int id) {
        String candidateId = String.valueOf(id);
        proxyClient.sendRequest(TypesBrokerRequest.FIND_SERVICE, SERVICE_ADD_VOTE, candidateId);
    }

    private void prepareGUI(){
        mainFrame = new JFrame("Vote Panel");
        mainFrame.setSize(250, 100);
        mainFrame.setLocationRelativeTo(null);
        mainFrame.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent windowEvent) {
                System.exit(0);
            }
        });

        mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));

        loadDatGUI();

        mainFrame.add(mainPanel);
        mainFrame.setVisible(true);
    }

    private void loadDatGUI() {
        ArrayList<String> candidates = new ArrayList();
        candidates.add("Candidato 1");
        candidates.add("Candidato 2");
        candidates.add("Candidato 3");

        Box box = Box.createVerticalBox();

        for(int i = 0; i < candidates.size(); i++){
            final int candidateIndex = i;
            JPanel panelHolder = new JPanel();
            JButton buttonHolder = new JButton("Vote for " + candidates.get(i));
            buttonHolder.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    addVoteToCandidateById(candidateIndex + 1);
                }
            });
            box.add(buttonHolder);
            panelHolder.add(box);
            panelHolder.setLayout(new BoxLayout(panelHolder, BoxLayout.X_AXIS));
            mainPanel.add(panelHolder);
        }

    }

}
