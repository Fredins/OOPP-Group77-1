package org.group77.mejl.model;

import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Store;

public class EmailApp {

    private Folder[] getFolders(Store store) throws MessagingException {
        return store.getDefaultFolder().list("*");
    }


    public Message[] getMessages(Folder folder) throws MessagingException {
        folder.open(Folder.READ_ONLY);
        Message[] messages = folder.getMessages();
        return messages;
    }

}
