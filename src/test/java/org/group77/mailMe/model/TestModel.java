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
    public void testSetExistingAccountAsActiveAccount() throws ActiveAccountNotInAccounts {
        Model model = new Model(accounts);
        Account newActiveAccount = accounts.get(0);

        // try to set existing account as active account
        model.setActiveAccount(newActiveAccount);

        // check that newActiveAccount is equal to activeAccount in model
        Assertions.assertEquals(newActiveAccount,model.getActiveAccount().get());
    }

    @Test
    public void testSetNonExistingAccountAsActiveAccount() throws ActiveAccountNotInAccounts, EmailDomainNotSupportedException {
        Model model = new Model(accounts);
        // set an active Account
        model.setActiveAccount(accounts.get(0));

        // create new account that is not in model's accounts
        Account newActiveAccountFake = AccountFactory.createAccount("fake@gmail.com","fake123".toCharArray());

        // try to set new account that is not in model's accounts as activeAccount, should throw ActiveAccountNotInAccounts exception
        Assertions.assertThrows(ActiveAccountNotInAccounts.class, () -> model.setActiveAccount(newActiveAccountFake));

    }
    

    @Test
    public void testUpdateInbox() {
        /*model.getFolders().replaceAll(folders);

        List<Email> newEmails = new ArrayList<>();
        newEmails.add(new Email("adam@gmail.com",new String[]{"hej@gmail.com"},"hej","jag gör inte heller nåt"));*/
    }

}
