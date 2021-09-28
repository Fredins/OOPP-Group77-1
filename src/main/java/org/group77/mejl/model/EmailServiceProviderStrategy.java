package org.group77.mejl.model;
import java.util.*;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;


public abstract class EmailServiceProviderStrategy {

    String hostIn;
    String hostOut;
    String protocolIn;
    String protocolOut;
    int portIn;
    int portOut;

    public EmailServiceProviderStrategy(String hostIn ,String hostOut, String protocolIn, String protocolOut, int portIn, int portOut){
        this.hostIn = hostIn;
        this.hostOut = hostOut;
        this.protocolIn = protocolIn;
        this.protocolOut = protocolOut;
        this.portIn = portIn;
        this.portOut = portOut;

    }

    /**
     * @author Martin
     * @param account is a account
     * @return List<Folder> is a list of folders
     */
    public List<Folder> refreshFromServer(Account account) throws MessagingException{
        return parse(connectStore(account));
    }

    /**
     * @author Martin
     * @param account is a account
     * @return boolean if the connection was successfull
     */
    public boolean testConnection(Account account) throws MessagingException {
        connectStore(account);
        return true;
    }

    /**
     * @author Martin
     * @param account is a account
     * @return Store is a list of folders
     */
    private Store connectStore(Account account) throws MessagingException {
        Session session = Session.getDefaultInstance((new Properties()), null);
        Store store = session.getStore(protocolIn);

        String host = hostIn;
        int port = portIn;
        String address = account.getEmailAddress();
        String password = account.getPassword();

        store.connect(
                hostIn,
                portIn,
                account.getEmailAddress(),
                account.getPassword()
        );
        return store;
    }

    /**@author Alexey Ryabov
     * @param from - active account
     * @param recipient - to account.
     * @param subject - subject.
     * @param content - content.
     * @return
     */
    protected abstract boolean sendEmail(Account from, List<String> recipient, String subject, String content) throws Exception;

    protected abstract List<Folder> parse(Store store) throws MessagingException;

}
