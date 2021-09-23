package org.group77.mejl.oldModel;

import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class EmailApp {

    private final AccountHandler accountHandler = new AccountHandler();
    private final Connector connector = new Connector();

    public Tree<EmailFolder> getTree(Account info) {
        return connector.getTree(info);
    }
    public List<Tree<EmailFolder>> getTrees() throws IOException {
        return Arrays.stream(accountHandler.getAcccountsWithProtocol("imaps"))
                .map(this::getTree)
                .collect(Collectors.toList());
    }

    // TODO elin och alexey använd denna för att visa en drop down när man ska skicka ett mejl
    public Account[] getSendingAccounts() throws IOException {
        return Arrays.stream(accountHandler.getAcccountsWithProtocol("smtp"))
                .toArray(Account[]::new);
    }


    private Message[] getMessages(Folder folder) throws MessagingException {
        folder.open(Folder.READ_ONLY);
        Message[] messages = folder.getMessages();
        return messages;
    }

    public String addEmail(String identifier, String host, int port, String protocol, String user, String password){
        Account info = new Account(
                identifier,
                host,
                port,
                protocol,
                user,
                password
        );
        MessagingException e = connector.testConnection(info);
        if(e != null){
            // return exception message to client
            return e.toString();
        }
        IOException e1 = accountHandler.writeAccount(info);
        if (e1 != null) {
            return e1.toString();
        }
        return "account successfully added";
    }


}
