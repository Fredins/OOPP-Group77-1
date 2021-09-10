package org.group77.mejl.model;

import java.io.Serializable;
import java.util.Objects;

public class Account implements Serializable {
    private final String identifier;
    private final String host;
    private final int port;
    private final String protocol;
    private final String user;
    private final String password;

    public Account(String identifier, String host, int port, String protocol, String user, String password) {
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

    @Override
    public boolean equals(Object obj) {
        if(!(obj instanceof Account)){
           return false;
        }
        if(!(Objects.equals(getIdentifier(), ((Account) obj).getIdentifier()))){
           return false;
        }
        if(!(Objects.equals(getHost(), ((Account) obj).getHost()))){
            return false;
        }
        if(!(getPort() == ((Account) obj).getPort())){
            return false;
        }
        if(!(Objects.equals(getUser(), ((Account) obj).getUser()))){
            return false;
        }
        if(!(Objects.equals(getPassword(), ((Account) obj).getPassword()))){
            return false;
        }
        return true;
    }
}
