package org.group77.mejl.model;

import javax.mail.*;
import java.util.*;
import java.util.stream.Stream;

// THIS CLASS WILL BE SPLIT INTO MULTIPLE SINGLE RESPONSIBILITY CLASSES ONCE
// SOME THINGS ARE FIGURED OUT
public class Model {
    // remove global ugly variable when implementing fetching props from filesystem
    // probably from ~/.config/<app_name>/connected_emails.d/something
    private static Properties props;


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

