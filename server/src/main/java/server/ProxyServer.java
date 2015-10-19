package server;

import com.broker.api.BrokerManager;
import com.broker.api.Connection;
import com.broker.api.exceptions.BrokerConnectionErrorException;
import com.broker.api.exceptions.ServiceAlreadyDefinedException;
import com.broker.api.exceptions.ServiceNotFoundException;
import exceptions.ServerErrorException;

import java.io.IOException;

import server.model.Votations;
import server.model.entities.Response;
import server.utils.ServerResponses;

/**
 * Created by luisburgos on 2/10/15.
 */
public class ProxyServer {

    private Connection connection;

    public void callService(String serviceName, Object... params){
        try {
            Votations.getInstance().callFunctionByName(Votations.class, int.class, serviceName, params );
        } catch (ServerErrorException ex) {
            sendResponse(ServerResponses.FAILURE);
        }
        
        sendResponse(ServerResponses.SUCCESS);
        
    }

    public void sendResponse(int responseType){
        Response response = new Response();
        response.setType(responseType);
        //brokerOutput.println(new Gson().toJson(response));
    }

    public void registerServiceToBroker (){
        try {
            connection = BrokerManager.getManager().getDefaultConnection();
            connection.open();
            connection.registerService(Server.PORT_NUMBER_SERVER, "addVoteToCandidateById");
            connection.close();
        } catch (BrokerConnectionErrorException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ServiceAlreadyDefinedException e) {
            e.printStackTrace();
        }
    }

    public void changeServiceState(){
        try {
            connection = BrokerManager.getManager().getDefaultConnection();
            connection.open();
            connection.changeServiceState("addVoteToCandidateById", true);
            connection.close();
        } catch (BrokerConnectionErrorException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ServiceNotFoundException e) {
            e.printStackTrace();
        }
    }

}



