package org.group77.mejl.model;

public class ESP {
    private final String identifier;
    private final String host;
    private final int port;
    private final String protocol;
    private final String user;
    private final String password;

    public ESP(String identifier, String host, int port, String protocol, String user, String password) {
        this.identifier = identifier;
        this.host = host;
        this.port = port;
        this.protocol = protocol;
        this.user = user;
        this.password = password;
    }

    public String getIdentifier() {
        return identifier;
    }

    public String getHost() {
        return host;
    }

    public int getPort() {
        return port;
    }

    public String getProtocol() {
        return protocol;
    }

    public String getUser() {
        return user;
    }

    public String getPassword() {
        return password;
    }
}
