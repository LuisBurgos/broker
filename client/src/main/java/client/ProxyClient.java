package client;

import broker.BrokerManager;
import broker.Connection;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import entities.Response;
import exceptions.BrokerException;

import javax.swing.*;
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

    public void returnFromRequest(){

    }

    public void sendRequest(int type, String serviceName, String data){
        String request;
        request = packData(type, serviceName, data);
        try {
            connection = BrokerManager.getManager().getDefaultConnection();
            connection.open();
            connection.sendRequest(request);
            connection.close();
        } catch (BrokerException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
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
