package org.group77.mejl.model;

import com.sun.mail.imap.IMAPFolder;

import javax.mail.Folder;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Store;
import java.util.Arrays;
import java.util.Properties;

public class Connector {
    private final FolderParser folderParser = new FolderParser();

    public TreeNode<ImapsFolder> getFolderTree(AccountInformation info) throws MessagingException {
        Store store = connectStore(info);
        return folderParser.parseFolders(getFolders(store), store);
    }

    private ImapsFolder[] getFolders(Store store) throws MessagingException {
        return Arrays.stream(store.getDefaultFolder().list("*"))
                .map(f -> new ImapsFolder((IMAPFolder) f))
                .toArray(ImapsFolder[]::new);
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
