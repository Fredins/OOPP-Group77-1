package org.group77.mejl.model;

import javax.mail.*;
import java.io.*;
import java.util.*;
import java.util.stream.Stream;
import net.harawata.appdirs.AppDirs;
import net.harawata.appdirs.AppDirsFactory;

// THIS CLASS WILL BE SPLIT INTO MULTIPLE SINGLE RESPONSIBILITY CLASSES ONCE
// SOME THINGS ARE FIGURED OUT
public class Model {
    // remove global ugly variable when implementing fetching props from filesystem
    // probably from ~/.config/<app_name>/connected_emails.d/something
    private static Properties props;

    /**
     * Stores esp information serialized in data directory
     * @param esp an object with required data for connecting to remote ESP
     */
    public void writeESP(ESP esp){
        try{
            // TODO implement getDataDir() method
            String path = "C:\\Users\\Martin\\AppData\\Local\\grupp77\\mejl\\esp.d\\" + esp.getIdentifier();
            createFile(path);
            writeTo(esp, path);
        }catch (IOException e){
            e.printStackTrace();
        }

    }

    /**
     *
     * @param path to the file which contains ESP data
     * @return a new deserialized ESP object
     */
    public ESP readESP(String path){
        try{
           ESP esp = readFrom(path);
           return esp;
        }catch (IOException | ClassNotFoundException e){
           e.printStackTrace();
        }
        return null;
    }

    private void createFile(String path) throws IOException {
       File file = new File(path);
       if(file.exists()){
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

    private <T> T readFrom(String path) throws IOException, ClassNotFoundException {
        FileInputStream file = new FileInputStream(path);
        ObjectInputStream in = new ObjectInputStream(file);
        T o =  (T) in.readObject();
        in.close();
        file.close();
        return o;
    }






    // IN DEVELOPMENT
    public void storeProps(Properties props) {
        // TODO store in filesystem under ~/.config/<app_name>/connected_emails.d/something
        // TODO figure out windows/mac equivalent
        Model.props = props;
    }

    public IdentifierAndFolder[] getIdentifierAndFolder() throws MessagingException {
        return prependIdentifier(
                toIdentifierAndFolder(
                        getFolders(
                                connectStore(getProps()))
                ),
                getProps());

    }


    // IN DEVELOPMENT
    private Properties getProps() {
        // TODO fetch from filesystem
        // TODO need information on which email is "active", either use dependency injection or maybe symlink
        if (Model.props == null) {
            Properties props = new Properties();
            props.setProperty("identifier", "gmail");
            props.setProperty("protocol", "imaps");
            props.setProperty("host", "imap.gmail.com");
            props.setProperty("port", "993");
            props.setProperty("user", "77grupp@gmail.com");
            props.setProperty("password", "grupp77group");
            return props;
        }
        return Model.props;
    }


    // IN DEVELOPMENT
    private Store connectStore(Properties props) throws MessagingException {
        Session session = Session.getDefaultInstance(props, null);
        Store store = session.getStore(props.getProperty("protocol"));
        // for gmail you currently need to enable the option "less secure apps" TODO fix OAuth 2.0
        store.connect(
                props.getProperty("host"),
                Integer.parseInt(props.getProperty("port")),
                props.getProperty("user"),
                props.getProperty("password")
        );
        return store;
    }

    // IN DEVELOPMENT
    public Message[] getMessages(Folder folder) throws MessagingException {
        folder.open(Folder.READ_ONLY);
        Message[] messages = folder.getMessages();
        return messages;
    }

    // IN DEVELOPMENT
    private Folder[] getFolders(Store store) throws MessagingException {
        return store.getDefaultFolder().list("*");
    }

    private IdentifierAndFolder[] toIdentifierAndFolder(Folder[] folders){
        return Stream.of(Arrays.stream(folders)
                .map(IdentifierAndFolder::new)
                .toArray(IdentifierAndFolder[]::new)
        ).toArray(IdentifierAndFolder[]::new);
    }

    private IdentifierAndFolder[] prependIdentifier(IdentifierAndFolder[] identifierAndFolders, Properties props) {
        return Stream.concat(
                Stream.of(new IdentifierAndFolder[]{new IdentifierAndFolder(props.getProperty("identifier"))}),
                Stream.of(identifierAndFolders))
                .toArray(IdentifierAndFolder[]::new);
    }
}
// TEMPORARY

