package org.group77.mejl.model;

import javax.mail.*;
import java.util.*;

public class Model {

    private Store connectToStore() throws MessagingException {
        Properties props = new Properties();
        // protocol IMAP with SLL
        props.setProperty("mail.store.protocol", "imaps");
        Session session = Session.getDefaultInstance(props, null);
        Store store = session.getStore("imaps");
        // for gmail you currently need to enable the option "less secure apps"
        store.connect("imap.gmail.com",993, "77grupp@gmail.com", "grupp77group");
        return store;
    }

    public Message[] checkMails(String folder) throws MessagingException {
            Store store = connectToStore();
            Folder inbox = store.getFolder(folder);
            inbox.open(Folder.READ_ONLY);
            Message[] messages = inbox.getMessages();
            return messages;
    }
}
