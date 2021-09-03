package org.group77.mejl.model;

import javax.mail.*;
import java.util.*;

public class Model {

    // IN DEVELOPMENT
    private Store connectToStore() throws MessagingException {
        Properties props = new Properties();
        // protocol IMAP with SLL
        props.setProperty("mail.store.protocol", "imaps");
        Session session = Session.getDefaultInstance(props, null);
        Store store = session.getStore("imaps");
        // for gmail you currently need to enable the option "less secure apps" TODO fix OAuth 2.0
        store.connect("imap.gmail.com",993, "77grupp@gmail.com", "grupp77group");
        return store;
    }


    // IN DEVELOPMENT
    public Message[] getMessages(String folder) throws MessagingException {
            Store store = connectToStore();
            Folder inbox = store.getFolder(folder);
            inbox.open(Folder.READ_ONLY);
            Message[] messages = inbox.getMessages();
            return messages;
    }

    // IN DEVELOPMENT
    public Folder[] getfolders() throws MessagingException {
        Store store = connectToStore();
        return store.getDefaultFolder().list("*");
    }
}
