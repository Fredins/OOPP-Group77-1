package org.group77.mejl.model;

import com.sun.mail.imap.IMAPFolder;

import javax.mail.Folder;
import javax.mail.MessagingException;
import javax.mail.Store;

public class FolderParser {

    protected Tree<EmailFolder> parseFolders(EmailFolder[] folders, Store store) throws MessagingException {
        EmailFolder folder = createFolder(store, "gmail");
        Tree<EmailFolder> root = new Tree<>(folder);
        Tree<EmailFolder> inbox = new Tree<>(folders[0], root);
        Tree<EmailFolder> gmail = new Tree<>(folders[1], root);
        for (int i = 2; i < folders.length; i++) {
            Tree<EmailFolder> node = new Tree<>(folders[i], gmail);
            gmail.add(node);
        }
        root.getChildren().add(inbox);
        root.getChildren().add(gmail);
        return root;
    }

    private EmailFolder createFolder(Store store, String identifier) throws MessagingException {
        Folder folder = store.getFolder(identifier);
        if (!folder.exists()){
            folder.create(Folder.HOLDS_FOLDERS);
        }
        return  new EmailFolder((IMAPFolder) folder);
    }

}
