package org.group77.mejl.model;

import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Store;
import java.util.Properties;

public class Connector {
    /**
     * tests connection to store
     * @param info an object with required data for connecting to remote ESP
     * @return true if connection was established
     */
    public boolean testConnection(AccountInformation info){
        try{
            connectStore(info);
        }catch(MessagingException e){
            return false;
        }
        return true;
    }

    private Store connectStore(AccountInformation info) throws MessagingException {
        Session session = Session.getDefaultInstance((new Properties()), null);
        Store store = session.getStore(info.getProtocol());
        // for gmail you currently need to enable the option "less secure apps" TODO fix OAuth 2.0
        store.connect(
                info.getHost(),
                info.getPort(),
                info.getUser(),
                info.getPassword()
        );
        return store;
    }
}
