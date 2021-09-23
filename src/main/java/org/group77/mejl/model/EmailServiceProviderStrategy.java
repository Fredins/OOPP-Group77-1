package org.group77.mejl.model;
import java.util.*;
import javax.mail.*;


public abstract class EmailServiceProviderStrategy {

    String hostIn;
    String hostOut;
    String protocolIn;
    String protocolOut;
    int portIn;
    int portOut;


    public List<Folder> refreshFromServer(Account account) throws MessagingException{
        return parse(
            connectStore(account)
            .getDefaultFolder()
            .list("*")
            );
    }
    
    public boolean testConnection(Account info) throws MessagingException{
        try{
            connectStore(info);
        }catch(MessagingException e){
            e.printStackTrace();
            return false;
        }
        return true;
    }

    private Store connectStore(Account account) throws MessagingException {
        Session session = Session.getDefaultInstance((new Properties()), null);
        Store store = session.getStore(protocolIn);
        store.connect(
                hostIn,
                portIn,
                account.getEmailAddress(),
                account.getPassword()
        );
        return store;
    }

    public boolean sendEmail(Account from, List<String> recepients, String subject, String content) {
        return false;
    }

    protected abstract List<Folder> parse(javax.mail.Folder[] folders);

}
