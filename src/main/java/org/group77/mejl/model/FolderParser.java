package org.group77.mejl.model;

import com.sun.mail.imap.IMAPFolder;

import javax.mail.Folder;
import javax.mail.MessagingException;
import javax.mail.Store;

public class FolderParser {

    protected TreeNode<ImapsFolder> parseFolders(ImapsFolder[] folders, Store store) throws MessagingException {
        ImapsFolder folder = createFolder(store, "gmail");
        TreeNode<ImapsFolder> root = new TreeNode<>(folder);
        TreeNode<ImapsFolder> inbox = new TreeNode<>(folders[0], root);
        TreeNode<ImapsFolder> gmail = new TreeNode<>(folders[1], root);
        for (int i = 2; i < folders.length; i++) {
            TreeNode<ImapsFolder> node = new TreeNode<>(folders[i], gmail);
            gmail.add(node);
        }
        root.getChildren().add(inbox);
        root.getChildren().add(gmail);
        return root;
    }

    private ImapsFolder createFolder(Store store, String identifier) throws MessagingException {
        Folder folder = store.getFolder(identifier);
        if (!folder.exists()){
            folder.create(Folder.HOLDS_FOLDERS);
        }
        return  new ImapsFolder((IMAPFolder) folder);
    }

}
