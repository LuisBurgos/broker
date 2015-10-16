package client;


import com.broker.api.BrokerManager;
import com.broker.api.Connection;
import com.broker.api.exceptions.BrokerConnectionErrorException;
import com.broker.api.exceptions.BrokerException;
import com.broker.api.exceptions.ServiceNotFoundException;
import com.google.gson.Gson;
import com.google.gson.JsonObject;


import javax.swing.*;
import javax.xml.ws.Response;
import java.awt.*;
import java.io.IOException;

/**
 * Created by luisburgos on 21/09/15.
 */
public class ProxyClient {

    private Connection connection;

    public void returnFromRequest(){

    }

    public void sendRequest(int type, String serviceName, String data){
        String request;
        request = packData(type, serviceName, data);
        try {
            connection = BrokerManager.getManager().getDefaultConnection();
            connection.open();
            connection.findService(serviceName);
            connection.close();
        } catch (IOException e) {
            System.err.println(e.getMessage());
        } catch (BrokerConnectionErrorException e) {
            System.err.println(e.getMessage());
        } catch (ServiceNotFoundException e) {

            final JPanel panel = new JPanel();
            JOptionPane.showMessageDialog(
                    panel,
                    e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );

        }
    }


    private String packData(int type, String serviceName, String data){

        String entity;
        JsonObject json = new JsonObject();
        json.addProperty("type", type);
        json.addProperty("serviceName", serviceName);
        json.addProperty("data", data);
        entity = json.toString();

        return entity;
    }

    private Response unpackData(String responseFromBroker){
        Response response;
        response = new Gson().fromJson(responseFromBroker, Response.class);
        return response;
    }

}
