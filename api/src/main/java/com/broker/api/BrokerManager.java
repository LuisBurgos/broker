package com.broker.api;

import com.broker.api.entities.BrokerInformation;
import com.broker.api.exceptions.BrokerConnectionErrorException;
import com.broker.api.exceptions.BrokerException;
import com.broker.api.internal.BrokersLoader;

import java.util.HashMap;

/**
 * Created by luisburgos on 15/10/15.
 */
public class BrokerManager {

    private static final String DEFAULT_BROKER_NAME = "1";

    private static BrokerManager manager;
    private static HashMap<String, BrokerInformation> knownBrokers;

    public synchronized static BrokerManager getManager(){
        if(manager == null){
            manager = new BrokerManager();
        }
        return manager;
    }

    public Connection getDefaultConnection() throws BrokerConnectionErrorException {
        return getConnection(DEFAULT_BROKER_NAME);
    }

    public Connection getConnection(String brokerName) throws BrokerConnectionErrorException {

        BrokerInformation brokerToConnect = knownBrokers.get(brokerName);

        Connection newConnection;
        newConnection = new Connection(
                brokerToConnect.getIp(),
                brokerToConnect.getPort()
        );

        return newConnection;
    }

    private BrokerManager(){
        initBrokerConfiguration();
    }

    private void initBrokerConfiguration() {
        knownBrokers = BrokersLoader.loadBrokersInformation();
    }

}
