package org.group77.mejl.model;

import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Store;
import java.io.IOException;

public class EmailApp {

    private final AccountHandler accountHandler = new AccountHandler();
    private final Connector connector = new Connector();


    private Folder[] getFolders(Store store) throws MessagingException {
        return store.getDefaultFolder().list("*");
    }


    private Message[] getMessages(Folder folder) throws MessagingException {
        folder.open(Folder.READ_ONLY);
        Message[] messages = folder.getMessages();
        return messages;
    }

    public String addEmail(String identifier, String host, int port, String protocol, String user, String password){
        AccountInformation info = new AccountInformation(
                identifier,
                host,
                port,
                protocol,
                user,
                password
        );
        MessagingException e = connector.testConnection(info);
        if(e != null){
            // return exception message to client
            return e.toString();
        }
        IOException e1 = accountHandler.writeAccount(info);
        if (e1 != null) {
            return e1.toString();
        }
        return "account successfully added";
    }


}
