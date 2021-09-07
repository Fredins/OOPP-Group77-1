package org.group77.mejl.model;

import javax.mail.*;

// THIS CLASS WILL BE SPLIT INTO MULTIPLE SINGLE RESPONSIBILITY CLASSES ONCE
// SOME THINGS ARE FIGURED OUT
public class Model {



    private Folder[] getFolders(Store store) throws MessagingException {
        return store.getDefaultFolder().list("*");
    }

    private Folder createFolder(Store store, String identifier) throws MessagingException {
       Folder folder = store.getFolder(identifier);
       if (!folder.exists()){
           folder.create(Folder.HOLDS_FOLDERS);
       }
       return folder;
    }

    // IN DEVELOPMENT
    public Message[] getMessages(Folder folder) throws MessagingException {
        folder.open(Folder.READ_ONLY);
        Message[] messages = folder.getMessages();
        return messages;
    }


}



