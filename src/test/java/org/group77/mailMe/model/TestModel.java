package org.group77.mailMe.model;

import org.group77.mailMe.model.data.Account;
import org.group77.mailMe.model.data.Email;
import org.group77.mailMe.model.data.Folder;
import org.group77.mailMe.services.storage.AccountAlreadyExistsException;
import org.junit.Assert;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
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
    public void testSetNonExistingAccountAsActiveAccount() throws EmailDomainNotSupportedException {
        Model model = new Model(accounts);

        // create new account that is not in model's accounts
        Account newActiveAccountFake = AccountFactory.createAccount("fake@gmail.com","fake123".toCharArray());

        // try to set new account that is not in model's accounts as activeAccount, should throw ActiveAccountNotInAccounts exception
        Assertions.assertThrows(ActiveAccountNotInAccounts.class, () -> model.setActiveAccount(newActiveAccountFake));

    }

    @Test
    public void testAddNotAlreadyAddedAccount() throws EmailDomainNotSupportedException, AccountAlreadyExistsException {
        Model model = new Model(accounts);

        // create new account that is not in model and add it to model
        Account newAccount = AccountFactory.createAccount("newEmail@gmail.com","test123".toCharArray());
        model.addAccount(newAccount);

        // assert that newAccount was added to model's accounts
        Assertions.assertTrue(model.getAccounts().get().contains(newAccount));

    }

    @Test
    public void testAddAlreadyAddedAccount()  {
        Model model = new Model(accounts);

        // try to add already existing account to model
        Account existingAccount = model.getAccounts().get().get(0);

        // assert that existingAccount cannot be added to model
        Assertions.assertThrows(AccountAlreadyExistsException.class, () -> model.addAccount(existingAccount));
    }

    @Test
    public void testCreateAccountEmailAddress() throws EmailDomainNotSupportedException {
        Model model = new Model(accounts);
        Account account = model.createAccount("hej@gmail.com","hej123".toCharArray());

        Assertions.assertEquals("hej@gmail.com",account.emailAddress());
    }

    @Test
    public void testCreateAccountPassword() throws EmailDomainNotSupportedException {
        Model model = new Model(accounts);
        Account account = model.createAccount("hej@gmail.com","hej123".toCharArray());

        Assertions.assertEquals("hej123",new String(account.password()));
    }

    @Test
    public void testCreateNotValidAccount() {
        Model model = new Model(accounts);

        Assertions.assertThrows(EmailDomainNotSupportedException.class, () -> model.createAccount(
                                                                            "notValid@yahoo.com",
                                                                                        "fail123".toCharArray()));

    }


    @Test
    public void testUpdateInbox() {
        /*model.getFolders().replaceAll(folders);

        List<Email> newEmails = new ArrayList<>();
        newEmails.add(new Email("adam@gmail.com",new String[]{"hej@gmail.com"},"hej","jag gör inte heller nåt"));*/
    }

}
