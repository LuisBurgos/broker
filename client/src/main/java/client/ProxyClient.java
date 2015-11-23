package client;

import com.broker.api.BrokerManager;
import com.broker.api.Connection;
import com.broker.api.exceptions.BrokerConnectionErrorException;
import com.broker.api.exceptions.InvalidDataFormatException;
import com.broker.api.exceptions.ServiceNotAvailableException;
import com.broker.api.exceptions.ServiceNotFoundException;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import javax.swing.*;
import javax.xml.ws.Response;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * Created by luisburgos on 21/09/15.
 */
public class ProxyClient {

    private Connection connection;

    public void sendRequest(String serviceName, String data){
        try {
            connection = BrokerManager.getManager().getDefaultConnection();
            connection.open();
            if(connection.findService(serviceName)){
                connection.executeService(serviceName, data);
            }else {
                throw new ServiceNotFoundException();
            }
            connection.close();
        } catch (IOException e) {
            final JPanel panel = new JPanel();
            JOptionPane.showMessageDialog(panel,
                    e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        } catch (BrokerConnectionErrorException e) {
            System.err.println(e.getMessage());
        } catch (InvalidDataFormatException e) {
            System.err.println(e.getMessage());
        } catch (ServiceNotFoundException e) {
            final JPanel panel = new JPanel();
            JOptionPane.showMessageDialog(panel,
                    e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        } catch (ServiceNotAvailableException e) {
            System.err.println(e.getMessage());
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
