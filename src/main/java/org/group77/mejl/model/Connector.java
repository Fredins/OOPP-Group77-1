package org.group77.mejl.model;

import javax.mail.Folder;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Store;
import java.util.Properties;

public class Connector {
    private final FolderParser folderParser = new FolderParser();

    public TreeNode<Folder> getFolderTree(AccountInformation info) throws MessagingException {
        Store store = connectStore(info);
        return folderParser.getFolderTree(getFolders(store), store);
    }

    private Folder[] getFolders(Store store) throws MessagingException {
        return store.getDefaultFolder().list("*");
    }

    /**
     * tests connection to store
     * @param info an object with required data for connecting to remote ESP
     * @return true if connection was established
     */
    protected MessagingException testConnection(AccountInformation info){
        try{
            connectStore(info);
        }catch(MessagingException e){
            return e;
        }
        return null;
    }

    private Store connectStore(AccountInformation info) throws MessagingException {
        Session session = Session.getDefaultInstance((new Properties()), null);
        Store store = session.getStore(info.getProtocol());
        // for gmail you currently need to enable the option "less secure apps" TODO fix OAuth 2.0
        store.connect(
                info.getHost(),
                info.getPort(),
                info.getUser(),
                info.getPassword()
        );
        return store;
    }

}
