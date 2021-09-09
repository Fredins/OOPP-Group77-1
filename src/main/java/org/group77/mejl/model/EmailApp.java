package org.group77.mejl.model;

public class EmailApp {

    Connector connector = new Connector();



    public boolean testConnection(String identifier, String host, Integer port, String protocol, String user, String password ){
        return connector.testConnection(identifier, host, port, protocol, user, password);
    }




}
