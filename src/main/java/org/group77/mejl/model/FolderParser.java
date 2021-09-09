package org.group77.mejl.model;

import javax.mail.Folder;
import javax.mail.MessagingException;
import javax.mail.Store;

public class FolderParser {

    protected TreeNode<Folder> getFolderTree(Folder[] folders, Store store) throws MessagingException {
        Folder folder = createFolder(store, "gmail");
        TreeNode<Folder> root = new TreeNode<>(folder);
        TreeNode<Folder> inbox = new TreeNode<>(folders[0], root);
        TreeNode<Folder> gmail = new TreeNode<>(folders[1], root);
        for (int i = 2; i < folders.length; i++) {
            TreeNode<Folder> node = new TreeNode<>(folders[i], gmail);
            gmail.add(node);
        }
        root.getChildren().add(inbox);
        root.getChildren().add(gmail);
        return root;
    }

    private Folder createFolder(Store store, String identifier) throws MessagingException {
        Folder folder = store.getFolder(identifier);
        if (!folder.exists()){
            folder.create(Folder.HOLDS_FOLDERS);
        }
        return folder;
    }

}
