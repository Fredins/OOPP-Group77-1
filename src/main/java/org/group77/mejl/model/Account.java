package org.group77.mejl.model;


import java.io.Serializable;
import java.util.Objects;

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

    /**
     * @author Martin Fredin, revised by Hampus Jernkrook
     * @param obj -  the object to compare against
     * @return true iff this instance and the input object has the
     *              same email address, password and server provider.
     */
    @Override
    public boolean equals(Object obj) {
        if(!(obj instanceof Account)){
            return false;
        }
        if(!(Objects.equals(this.getEmailAddress(), ((Account) obj).getEmailAddress()))){
            return false;
        }
        if(!(Objects.equals(this.getPassword(), ((Account) obj).getPassword()))){
            return false;
        }
        if(!(Objects.equals(this.getServerProvider(), ((Account) obj).getServerProvider()))){
            return false;
        }
        return true;
    }

}
