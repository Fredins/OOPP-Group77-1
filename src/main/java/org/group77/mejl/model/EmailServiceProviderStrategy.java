package org.group77.mejl.model;
import java.util.*;
//import javax.*;


public abstract class EmailServiceProviderStrategy {

    String hostIn;
    String hostOut;
    String protocolIn;
    String protocolOut;
    String portIn;
    String portOut;

    public boolean testConnection(Account account) {
        return false;
    }

    public List<Folder> refreshFromServer(Account account) {
        return null;
    }

    public boolean sendEmail(Account from, List<String> recepients, String subject, String content) {
        return false;
    }

    protected abstract List<Folder> parse(javax.mail.Folder[] folders);

}
