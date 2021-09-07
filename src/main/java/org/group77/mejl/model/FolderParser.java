package org.group77.mejl.model;

import javax.mail.Folder;
import javax.mail.MessagingException;
import javax.mail.Store;

public class FolderParser {

    private Folder createFolder(Store store, String identifier) throws MessagingException {
        Folder folder = store.getFolder(identifier);
        if (!folder.exists()){
            folder.create(Folder.HOLDS_FOLDERS);
        }
        return folder;
    }

}
