package com.broker.api.internal;

import com.google.gson.*;
import com.broker.api.entities.BrokerInformation;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;

/**
 * Created by luisburgos on 16/10/15.
 */
public class BrokersLoader {

    private static String FILE_PATH = "src/main/java/com/broker/api/internal/brokersInformation.json";

    public static HashMap<String, BrokerInformation> loadBrokersInformation() {

        Gson gson = new Gson();
        BrokerInformation[] result;
        HashMap<String, BrokerInformation> knownBrokers;

        try {
            BufferedReader json  = new BufferedReader(new FileReader(FILE_PATH));

            JsonObject brokersObject = new JsonParser().parse(json).getAsJsonObject();
            JsonArray brokers = brokersObject.getAsJsonArray("brokers");

            result = gson.fromJson(brokers, BrokerInformation[].class);
            knownBrokers = new HashMap<String, BrokerInformation>();

            for (BrokerInformation currentBroker : Arrays.asList(result)) {
                knownBrokers.put(currentBroker.getName(), currentBroker);
            }

        } catch (FileNotFoundException e) {
            System.err.println(e.getMessage());
            System.out.println("Loading backup information...");
            knownBrokers = loadBackupInformation();
        }

        return knownBrokers;
    }

    private static HashMap<String, BrokerInformation> loadBackupInformation() {
        HashMap<String, BrokerInformation> backupBrokersInformation;
        backupBrokersInformation = new HashMap<String, BrokerInformation>();
        backupBrokersInformation.put("1", new BrokerInformation("1", "localhost", 5555));
        return backupBrokersInformation;
    }

}
