package org.group77.mailMe.model;

import org.group77.mailMe.model.data.Account;
import org.group77.mailMe.model.data.Email;
import org.group77.mailMe.model.data.Folder;
import org.group77.mailMe.model.exceptions.*;
import org.junit.jupiter.api.Assertions;

import org.junit.jupiter.api.Test;
import org.group77.mailMe.model.exceptions.EmailDomainNotSupportedException;

import org.junit.jupiter.api.*;


import java.time.LocalDateTime;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Elin Hagman
 */

public class TestModel {
    private static List<Account> accounts;
    private static List<Folder> folders;
    private static Model model;

    @BeforeEach
    public void beforeEach() throws EmailDomainNotSupportedException {
        // ===== Initiate folders ======

        // Emails to be added to inbox folder
        List<Email> inboxEmails = new ArrayList<>();
        inboxEmails.add(new Email("adam@gmail.com",new String[]{"hej@gmail.com"},"hej","vad gör du", null, null));
        inboxEmails.add(new Email("maria@gmail.com",new String[]{"hej@gmail.com"},"important!","hej", null, null));

        // Mails to be added to sent folder
        List<Email> sentEmails = new ArrayList<>();
        sentEmails.add(new Email("hej@gmail.com", new String[]{"adam@gmail.com"},"hej","inget, vad gör du?", null, null));
        sentEmails.add(new Email("hej@gmail.com",new String[]{"maria@gmail.com"},"important!","hej", null, null));

        // Emails to be added to dateFolder, a folder containing emails with dates
        List<Email> dateEmails = new ArrayList<>();
        String[] to1 = new String[]{"TEST_nr_1@gmail.com", "lol@gmail.com"};
        String[] to2 = new String[]{"TEST_nr_2@outlook.com", "lol@hotmail.com"};
        String[] to3 = new String[]{"TEST_nr_3@live.com", "haha@gmail.com"};


        dateEmails.add(new Email("mailme@gmail.com", to1, "First", "contains SAUSAGE",
                        LocalDateTime.of(2021, Month.OCTOBER, 11, 15, 30, 45)));

        dateEmails.add(new Email("memail@live.com", to2, "Second", "contains sausage",
                        LocalDateTime.of(2021, Month.MAY, 11, 15, 30, 45)));

        dateEmails.add(new Email("lol@hotmail.com", to3, "third", "contains no meat at all",
                        LocalDateTime.of(2010, Month.JANUARY, 11, 15, 30, 45)));


        Folder inboxFolder = new Folder("Inbox",inboxEmails);
        Folder sentFolder = new Folder("Sent",sentEmails);
        Folder dateFolder = new Folder("DateFolder", dateEmails);

        // Folders belonging to active account
        folders = new ArrayList<>();
        folders.add(inboxFolder);
        folders.add(sentFolder);
        folders.add(dateFolder);

        // ===== Initiate accounts =====

        // Accounts
        accounts = new ArrayList<>();
        accounts.add(AccountFactory.createAccount("hej@gmail.com","hej123".toCharArray()));
        accounts.add(AccountFactory.createAccount("hola@gmail.com","hola123".toCharArray()));

        // ==== Initiate model =====
        model = new Model(accounts);
        model.getFolders().replaceAll(folders);

    }

    @AfterEach
    public void afterEach() {
        if (folders != null) {
            folders.clear();
        }
        if (accounts != null) {
            accounts.clear();
        }
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
    public void testGetActiveFolder() {
        Model model = new Model(accounts);
        model.getFolders().replaceAll(folders);

        model.setActiveFolder(model.getFolders().get().get(0));

        Assertions.assertEquals(model.getFolders().get().get(0),model.getActiveFolder().get());
    }

    @Test
    public void testGetActiveEmail() {
        Model model = new Model(accounts);
        model.getFolders().replaceAll(folders);

        model.setActiveEmail(folders.get(0).emails().get(0));

        Assertions.assertEquals(folders.get(0).emails().get(0),model.getActiveEmail().get());
    }

    @Test
    public void testGetAutoSuggestions() {
        Model model = new Model(accounts);

        List<String> autoSuggestions = List.of("adam@gmail.com");
        model.setAutoSuggestions(autoSuggestions);

        Assertions.assertEquals(autoSuggestions,model.getAutoSuggestions().get());

    }

    // ========= setActiveAccount =========

    @Test
    public void testSetExistingAccountAsActiveAccount() throws ActiveAccountNotInAccounts {

        Account newActiveAccount = accounts.get(0);

        // try to set existing account as active account
        model.setActiveAccount(newActiveAccount);

        // check that newActiveAccount is equal to activeAccount in model
        Assertions.assertEquals(newActiveAccount,model.getActiveAccount().get());
    }

    @Test
    public void testSetNonExistingAccountAsActiveAccount() throws EmailDomainNotSupportedException {
        // create new account that is not in model's accounts
        Account newActiveAccountFake = AccountFactory.createAccount("fake@gmail.com","fake123".toCharArray());

        // try to set new account that is not in model's accounts as activeAccount, should throw ActiveAccountNotInAccounts exception
        Assertions.assertThrows(ActiveAccountNotInAccounts.class, () -> model.setActiveAccount(newActiveAccountFake));

    }

    // ========= addAccount =========

    @Test
    public void testAddNotAlreadyAddedAccount() throws EmailDomainNotSupportedException, AccountAlreadyExistsException {
        // create new account that is not in model and add it to model
        Account newAccount = AccountFactory.createAccount("newEmail@gmail.com","test123".toCharArray());
        model.addAccount(newAccount);

        // assert that newAccount was added to model's accounts
        Assertions.assertTrue(model.getAccounts().get().contains(newAccount));

    }

    @Test
    public void testAddAlreadyAddedAccount()  {
        // try to add already existing account to model
        Account existingAccount = model.getAccounts().get().get(0);

        // assert that existingAccount cannot be added to model
        Assertions.assertThrows(AccountAlreadyExistsException.class, () -> model.addAccount(existingAccount));
    }

    // ========= createAccount =========

    @Test
    public void testCreateAccountEmailAddress() throws EmailDomainNotSupportedException {
        Account account = model.createAccount("hej@gmail.com","hej123".toCharArray());

        Assertions.assertEquals("hej@gmail.com",account.emailAddress());
    }

    @Test
    public void testCreateAccountPassword() throws EmailDomainNotSupportedException {
        Account account = model.createAccount("hej@gmail.com","hej123".toCharArray());

        Assertions.assertEquals("hej123",new String(account.password()));
    }

    @Test
    public void testCreateNotValidAccount() {

        Assertions.assertThrows(EmailDomainNotSupportedException.class, () -> model.createAccount(
                                                                            "notValid@yahoo.com",
                                                                                        "fail123".toCharArray()));

    }

    // ========= updateFolder =========

    @Test
    public void testUpdateInboxWithOneEmail()  {

        // Add newEmail to model's inbox
        Email newEmail = new Email("adam@gmail.com",new String[]{"hej@gmail.com"},"hej","jag gör inte heller nåt", null, null);
        List<Email> newEmails = new ArrayList<>();
        newEmails.add(newEmail);
        model.updateFolder(model.getFolders().get().get(0),newEmails);

        // Assert that inbox is updated with newEmail
        Assertions.assertTrue(model.getFolders().get().get(0).emails().contains(newEmail));
    }

    @Test
    public void testUpdateInboxWithSeveralEmail() {


        // Add newEmail to model's inbox
        Email newEmail = new Email("adam@gmail.com",new String[]{"hej@gmail.com"},"hej","jag gör inte heller nåt", null, null);
        Email newEmail2 = new Email("adam@gmail.com",new String[]{"hej@gmail.com"},"hej igen","vad gör du nu", null, null);
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

        List<Folder> folders = List.of(
                new Folder("Inbox", new ArrayList<>()),
                new Folder("Archive", new ArrayList<>()),
                new Folder("Sent", new ArrayList<>()),
                new Folder("Drafts", new ArrayList<>()),
                new Folder("Trash", new ArrayList<>()));

        Assertions.assertEquals(folders,model.createFolders());
    }

    // ========= filterOnTo =========

    @Test
    public void testFilterOnTo() {

        // set email's in sent folder as model's activeEmails
        List<Email> sentEmails = model.getFolders().get().get(1).emails();
        model.setActiveEmails(sentEmails);

        model.filterOnTo("adam@gmail.com");

        // only adam@gmail.com's mail should be left in activeEmails, not maria@gmail.com
        List<Email> expectedEmails = List.of(folders.get(1).emails().get(0)); // adam@gmail.com added to expectedEmails

        Assertions.assertEquals(expectedEmails,model.getActiveEmails().get());

    }

    // ========= filterOnFrom =========

    @Test
    public void testFilterOnFrom() {

        // set email's in sent folder as model's activeEmails
        List<Email> inboxEmails = model.getFolders().get().get(0).emails();
        model.setActiveEmails(inboxEmails);

        model.filterOnFrom("maria@gmail.com");

        // only emails from maria@gmail.com should be left in activeEmails
        List<Email> expectedEmails = List.of(folders.get(0).emails().get(1)); // maria@gmail.com added to expectedEmails

        Assertions.assertEquals(expectedEmails,model.getActiveEmails().get());
    }

    // ========= filterOnMaxDate =========
    @Test
    public void testFilterOnMaxDate() {

        // set dateFolder's emails as active
        List<Email> dateEmails = model.getFolders().get().get(2).emails();
        model.setActiveEmails(dateEmails);

        model.filterOnMaxDate(LocalDateTime.of(2020, Month.DECEMBER,31,12,30));

        // only email that should be left in activeEmails is the third one from 2010
        List<Email> expectedEmails = List.of(model.getFolders().get().get(2).emails().get(2));

        Assertions.assertEquals(expectedEmails,model.getActiveEmails().get());

    }

    // ========= filterOnMinDate =========

    @Test
    public void testOnMinDate() {

        // set dateFolder's emails as active
        List<Email> dateEmails = model.getFolders().get().get(2).emails();
        model.setActiveEmails(dateEmails);

        model.filterOnMinDate(LocalDateTime.of(2020, Month.DECEMBER,31,12,30));

        // only email that should be left in activeEmails is the two first ones from 2021
        List<Email> expectedEmails = List.of(model.getFolders().get().get(2).emails().get(0),
                                            model.getFolders().get().get(2).emails().get(1));

        Assertions.assertEquals(expectedEmails,model.getActiveEmails().get());

    }

    // ========= sortByNewToOld =========
    @Test
    public void testSortByNewToOld() {

        // unsortedEmails are not sorted by date
        List<Email> dateEmails = model.getFolders().get().get(2).emails();
        List<Email> unsortedEmails = List.of(dateEmails.get(0),dateEmails.get(2),dateEmails.get(1));

        model.setActiveEmails(unsortedEmails);
        model.sortByNewToOld();

        List<Email> expectedEmails = List.of(dateEmails.get(0),dateEmails.get(1),dateEmails.get(2));

        Assertions.assertEquals(expectedEmails,model.getActiveEmails().get());


    }

    // ========= sortByOldToNew =========

    @Test
    public void testSortByOldToNew() {

        // set dateEmails as active emails
        List<Email> dateEmails = model.getFolders().get().get(2).emails();
        model.setActiveEmails(dateEmails);

        model.sortByOldToNew();

        // dateEmails from oldest to newest
        List<Email> expectedEmails = List.of(dateEmails.get(2),dateEmails.get(1),dateEmails.get(0));

        Assertions.assertEquals(expectedEmails,model.getActiveEmails().get());
    }

    // ========= clearFilter =========

    @Test
    public void testClearFilter() {

        // set dateFolder and its emails as activeFolder and activeEmails
        model.setActiveFolder(model.getFolders().get().get(2));
        model.setActiveEmails(model.getActiveFolder().get().emails());

        // save original order of emails
        List<Email> originalEmails = new ArrayList<>(model.getActiveFolder().get().emails());

        // change their original order (new to old) to old to new
        model.sortByOldToNew();

        model.clearFilter();

        // assert that emails are back to original order
        Assertions.assertEquals(originalEmails,model.getActiveEmails().get());

    }

    // ========= search =========

    @Test
    public void testSearchFromField() {
        // set inboxFolder and its emails as activeFolder and activeEmails
        Folder inboxFolder = model.getFolders().get().get(0);
        model.setActiveFolder(inboxFolder);
        model.setActiveEmails(inboxFolder.emails());

        model.search("adam@gmail.com");

        // only email from adam@gmail.com should be in activeEmails
        List<Email> expectedEmails = List.of(inboxFolder.emails().get(0));

        Assertions.assertEquals(expectedEmails,model.getActiveEmails().get());
    }


    @Test
    public void testSearchToField() {
        // set sentFolder and its emails as activeFolder and activeEmails
        Folder sentFolder = model.getFolders().get().get(1);
        model.setActiveFolder(sentFolder);
        model.setActiveEmails(sentFolder.emails());

        model.search("adam@gmail.com");

        // only email containing fields with adam@gmail.com should be in activeEmails
        List<Email> expectedEmails = List.of(sentFolder.emails().get(0));

        Assertions.assertEquals(expectedEmails,model.getActiveEmails().get());
    }

    @Test
    public void testSearchContentField() {
        // set inboxFolder and its emails as activeFolder and activeEmails
        Folder inboxFolder = model.getFolders().get().get(0);
        model.setActiveFolder(inboxFolder);
        model.setActiveEmails(inboxFolder.emails());

        model.search("Vad gör");

        // only email containing fields with "Vad gör" should be in activeEmails
        List<Email> expectedEmails = List.of(inboxFolder.emails().get(0));

        Assertions.assertEquals(expectedEmails,model.getActiveEmails().get());
    }

    @Test
    public void testSearchSeveralFields() {
        // set inboxFolder and its emails as activeFolder and activeEmails
        Folder inboxFolder = model.getFolders().get().get(0);
        model.setActiveFolder(inboxFolder);
        model.setActiveEmails(inboxFolder.emails());

        model.search("hej");

        // both emails contain "hej", first one in subject field and second one in content field
        List<Email> expectedEmails = List.of(inboxFolder.emails().get(0),inboxFolder.emails().get(1));

        Assertions.assertEquals(expectedEmails,model.getActiveEmails().get());

    }

    @Test
    public void testSearchThatResultsInNoEmails() {

        // set inboxFolder and its emails as activeFolder and activeEmails
        Folder inboxFolder = model.getFolders().get().get(0);
        model.setActiveFolder(inboxFolder);
        model.setActiveEmails(inboxFolder.emails());

        model.search("lwekäwerjäwörk");

        Assertions.assertTrue(model.getActiveEmails().get().isEmpty());
    }

}

