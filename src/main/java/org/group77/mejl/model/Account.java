package org.group77.mejl.model;


import java.io.Serializable;

public class Account implements Serializable {
    private final String emailAddress;
    private final String password;
    private final ServerProvider provider;

    public Account(String emailAddress, String password, ServerProvider provider){
        this.emailAddress = emailAddress;
        this.password = password;
        this.provider = provider;
    }
    
    public String getEmailAddress(){ return emailAddress; }
    public String getPassword(){ return password; }
    public ServerProvider getServerProvider() {return provider;}

}
