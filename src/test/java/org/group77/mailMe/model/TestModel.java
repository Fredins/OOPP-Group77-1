package org.group77.mailMe.model;

import org.group77.mailMe.model.data.Account;
import org.group77.mailMe.model.data.Email;
import org.group77.mailMe.model.data.Folder;
import org.group77.mailMe.model.exceptions.*;
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

        // ============ Setup account and folders to be used in tests ============

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

        // Accounts
        accounts = new ArrayList<>();
        accounts.add(AccountFactory.createAccount("hej@gmail.com","hej123".toCharArray()));
        accounts.add(AccountFactory.createAccount("hola@gmail.com","hola123".toCharArray()));

    }

    // ========= setActiveAccount =========

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

    // ========= addAccount =========

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

    // ========= createAccount =========

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

    // ========= updateFolder =========
    @Test
    public void testUpdateInboxWithOneEmail()  {
        Model model = new Model(accounts);
        model.getFolders().replaceAll(folders);

        // Add newEmail to model's inbox
        Email newEmail = new Email("adam@gmail.com",new String[]{"hej@gmail.com"},"hej","jag gör inte heller nåt");
        List<Email> newEmails = new ArrayList<>();
        newEmails.add(newEmail);
        model.updateFolder(model.getFolders().get().get(0),newEmails);

        // Assert that inbox is updated with newEmail
        Assertions.assertTrue(model.getFolders().get().get(0).emails().contains(newEmail));
    }

    @Test
    public void testUpdateInboxWithSeveralEmail() {

        Model model = new Model(accounts);
        model.getFolders().replaceAll(folders);

        // Add newEmail to model's inbox
        Email newEmail = new Email("adam@gmail.com",new String[]{"hej@gmail.com"},"hej","jag gör inte heller nåt");
        Email newEmail2 = new Email("adam@gmail.com",new String[]{"hej@gmail.com"},"hej igen","vad gör du nu");
        List<Email> newEmails = new ArrayList<>();
        newEmails.add(newEmail);
        newEmails.add(newEmail2);
        model.updateFolder(model.getFolders().get().get(0),newEmails);

        // Assert that inbox is updated with newEmail
        Assertions.assertTrue(model.getFolders().get().get(0).emails().contains(newEmail) &&
                                model.getFolders().get().get(0).emails().contains(newEmail2));

   }


    // ========= createFolders =========

    @Test
    public void testCreateFolders() {
        Model model = new Model(accounts);

        List<Folder> folders = List.of(
                new Folder("Inbox", new ArrayList<>()),
                new Folder("Archive", new ArrayList<>()),
                new Folder("Sent", new ArrayList<>()),
                new Folder("Drafts", new ArrayList<>()),
                new Folder("Trash", new ArrayList<>()));

        Assertions.assertEquals(folders,model.createFolders());
    }

    // ========= getters and setters =========

    @Test
    public void testGetAccounts() {
        Model model = new Model(accounts);

        Assertions.assertEquals(accounts,model.getAccounts().get());
    }

    @Test
    public void testGetActiveAccount() throws ActiveAccountNotInAccounts {
        Model model = new Model(accounts);
        model.setActiveAccount(model.getAccounts().get().get(0));

        Assertions.assertEquals(model.getAccounts().get().get(0),model.getActiveAccount().get());
    }

    @Test
    public void testGetActiveEmails() {
        Model model = new Model(accounts);
        model.getFolders().replaceAll(folders);
        model.setActiveEmails(model.getFolders().get().get(0).emails());

        Assertions.assertEquals(model.getFolders().get().get(0).emails(),model.getActiveEmails().get());
    }

    @Test
    public void getActiveFolder() {
        Model model = new Model(accounts);
        model.getFolders().replaceAll(folders);

        model.setActiveFolder(model.getFolders().get().get(0));

        Assertions.assertEquals(model.getFolders().get().get(0),model.getActiveFolder().get());
    }

    @Test
    public void getActiveEmail() {
        Model model = new Model(accounts);
        model.getFolders().replaceAll(folders);

        model.setActiveEmail(folders.get(0).emails().get(0));

        Assertions.assertEquals(folders.get(0).emails().get(0),model.getActiveEmail().get());
    }


}

