package org.group77.mailMe.model;

import org.group77.mailMe.model.data.Account;
import org.group77.mailMe.model.data.Email;
import org.group77.mailMe.model.data.Folder;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Elin Hagman
 */

public class TestModel {
    private static List<Account> accounts;
    private static List<Folder> folders;

    @BeforeAll
    public static void setup() throws EmailDomainNotSupportedException {

        // ===== Initiate folders ======

        // Mails to be added to inbox folder
        List<Email> inboxEmails = new ArrayList<>();
        inboxEmails.add(new Email("adam@gmail.com",new String[]{"hej@gmail.com"},"hej","vad gör du"));
        inboxEmails.add(new Email("maria@gmail.com",new String[]{"hej@gmail.com"},"important!","hej"));

        // Mails to be added to sent folder
        List<Email> sentEmails = new ArrayList<>();
        sentEmails.add(new Email("hej@gmail.com", new String[]{"adam@gmail.com"},"hej","inget, vad gör du?"));
        sentEmails.add(new Email("hej@gmail.com",new String[]{"maria@gmail.com"},"important!","hej"));

        Folder inboxFolder = new Folder("Inbox",inboxEmails);
        Folder sentFolder = new Folder("Sent",sentEmails);

        // Folders belonging to active account
        folders = new ArrayList<>();
        folders.add(inboxFolder);
        folders.add(sentFolder);

        // ===== Initiate accounts =====

        // Models accounts
        accounts = new ArrayList<>();
        accounts.add(AccountFactory.createAccount("hej@gmail.com","hej123".toCharArray()));
        accounts.add(AccountFactory.createAccount("hola@gmail.com","hola123".toCharArray()));

    }

    @Test
    public void testSetExistingAccountAsActiveAccount() {
        Model model = new Model(accounts);
        Account activeAccount = accounts.get(0);

        Assertions.assertDoesNotThrow(model.setActiveAccount(activeAccount));

    }

    @Test
    public void testSetNonExistingAccountAsActiveAccount() {

    }

    @Test
    public void testUpdateInbox() {
        /*model.getFolders().replaceAll(folders);

        List<Email> newEmails = new ArrayList<>();
        newEmails.add(new Email("adam@gmail.com",new String[]{"hej@gmail.com"},"hej","jag gör inte heller nåt"));*/
    }

}
