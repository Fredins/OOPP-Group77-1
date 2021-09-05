package org.group77.mejl.model;

import com.sun.mail.imap.IMAPFolder;

import javax.mail.*;
import java.io.*;
import java.util.Properties;

// THIS CLASS WILL BE SPLIT INTO MULTIPLE SINGLE RESPONSIBILITY CLASSES ONCE
// SOME THINGS ARE FIGURED OUT
public class Model {
    /**
     * Stores esp information serialized in data directory
     *
     * @param esp an object with required data for connecting to remote ESP
     */
    public void writeESP(ESP esp) {
        try {
            // TODO implement getDataDir() method
            String path = "C:\\Users\\Martin\\AppData\\Local\\grupp77\\mejl\\esp.d\\" + esp.getIdentifier();
            createFile(path);
            writeTo(esp, path);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /**
     * @param path to the file which contains ESP data
     * @return a new deserialized ESP object
     */
    public ESP readESP(String path) {
        try {
            ESP esp = readFrom(path);
            return esp;
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    private void createFile(String path) throws IOException {
        File file = new File(path);
        if (file.exists()) {
            file.delete();
        }
        file.createNewFile();
    }

    private <T> void writeTo(T o, String path) throws IOException {
        FileOutputStream file = new FileOutputStream(path);
        ObjectOutputStream out = new ObjectOutputStream(file);
        out.writeObject(o);
        out.close();
        file.close();
    }

    /**
     * tests connection to store
     * @param esp an object with required data for connecting to remote ESP
     * @return true if connection was established
     */
    public boolean connectESP(ESP esp){
        try{
            connectStore(esp);
        }catch(MessagingException e){
            return false;
        }
        return true;
    }

    private Store connectStore(ESP esp) throws MessagingException {
        Session session = Session.getDefaultInstance((new Properties()), null);
        Store store = session.getStore(esp.getProtocol());
        // for gmail you currently need to enable the option "less secure apps" TODO fix OAuth 2.0
        store.connect(
                esp.getHost(),
                esp.getPort(),
                esp.getUser(),
                esp.getPassword()
        );
        return store;
    }

    private <T> T readFrom(String path) throws IOException, ClassNotFoundException {
        FileInputStream file = new FileInputStream(path);
        ObjectInputStream in = new ObjectInputStream(file);
        T o = (T) in.readObject();
        in.close();
        file.close();
        return o;
    }

    private Folder[] getFolders(Store store) throws MessagingException {
        return store.getDefaultFolder().list("*");
    }

    private Folder mkdir(Store store, String identifier) throws MessagingException {
       Folder folder = store.getFolder(identifier);
       if (!folder.exists()){
           folder.create(Folder.HOLDS_FOLDERS);
       }
       return folder;
    }


    // IN DEVELOPMENT

    // IN DEVELOPMENT
    public Message[] getMessages(Folder folder) throws MessagingException {
        folder.open(Folder.READ_ONLY);
        Message[] messages = folder.getMessages();
        return messages;
    }


}



