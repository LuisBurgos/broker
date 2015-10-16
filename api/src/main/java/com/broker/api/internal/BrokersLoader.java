package com.broker.api.internal;

import com.google.gson.Gson;
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

    private static String FILE_PATH = "brokersInformation.json";

    public static HashMap<String, BrokerInformation> loadBrokersInformation() {

        Gson gson = new Gson();
        BrokerInformation[] result;
        HashMap<String, BrokerInformation> knownBrokers;

        try {
            BufferedReader json  = new BufferedReader(new FileReader(FILE_PATH));
            result = gson.fromJson(json, BrokerInformation[].class);
            knownBrokers = new HashMap<String, BrokerInformation>();

            for (BrokerInformation currentBroker : Arrays.asList(result)) {
                knownBrokers.put(currentBroker.getName(), currentBroker);
            }

        } catch (FileNotFoundException e) {
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
