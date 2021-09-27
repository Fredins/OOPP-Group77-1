package org.group77.mejl;
import org.group77.mejl.model.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class TestLocalDiskStorage {

    @Test
    void testRetrieveAllEmailAddresses() throws Exception {
        Storage storage = new LocalDiscStorage();
        String[] addresses = new String[]{
                "TEST_nr_1@gmail.com",
                "TEST_nr_2@gmail.com",
                "TEST_nr_3@gmail.com"
        };
        Account[] accounts = new Account[]{
                new Account(addresses[0],"",ServerProvider.GMAIL),
                new Account(addresses[1],"",ServerProvider.GMAIL),
                new Account(addresses[2],"",ServerProvider.GMAIL)
        };
        // store the accounts
        for (Account a : accounts) {
            storage.store(a);
        }
        // now retrieve all email addresses
        List<String> res = storage.retrieveAllEmailAddresses();
        // TODO need to clean up the folder structure before trying this, otherwise
        //  must just try if the addresses are members of the result.
        // Test that the elements of the setup and result arrays are the same
        Assertions.assertArrayEquals(addresses, res.toArray());
    }


    @Test
    /**
     * @author Hampus Jernkrook
     */
    void testRetrieveEmails() throws OSNotFoundException, IOException, ClassNotFoundException {
        String testAddress = "TESTING-ADDRESS@gmail.com";
        List<String> to = Arrays.asList("lol@gmail.com",
                "lol2@gmail.com",
                "lol3@gmail.com");
        LocalDiscStorage storage = new LocalDiscStorage();
        List<Email> emails = Arrays.asList(
                new Email(testAddress, to, "1", "Email 1"),
                new Email(testAddress, to, "2", "Email 2"),
                new Email(testAddress, to, "3", "Email 3"),
                new Email(testAddress, to, "4", "Email 4")
                );
        String folderName = "testFolder";
        Folder testFolder = new Folder(folderName, emails);
        List<Folder> folders = Arrays.asList(testFolder);
        // store away
        storage.store(testAddress, folders);
        // now retrieve
        List<Email> retrieved = storage.retrieveEmails(testAddress, folderName);
        Email[] retrievedArray = (Email[]) retrieved.toArray(); //TODO will the casting work?
        // check that the emails are the same //TODO WILL THIS WORK?
        Assertions.assertArrayEquals(emails.toArray(), retrievedArray);

        //TODO clean up?
    }

    @Test //TODO keep or remove this one?
    void testRetrieveEmails2() throws Exception {
        LocalDiscStorage storage = new LocalDiscStorage();
        AccountHandler handler = new AccountHandler();
        Account a = handler.createAccount("77grupp@gmail.com", "grupp77group");
        handler.setActiveAccount(a.getEmailAddress());
        ApplicationManager am = new ApplicationManager();
        am.refreshFromServer();
        String folderName = "inbox";
        storage.retrieveEmails(handler.getActiveAccount().getEmailAddress(), folderName);
        // TODO assertion
    }

    @Test //TODO who wrote this and what do you want to test??
    void testStorage() throws Exception
    {
        AccountHandler h = new AccountHandler();
        Account a = h.createAccount("77grupp@gmail.com", "grupp77group");
        LocalDiscStorage l = new LocalDiscStorage();
        l.store(a);
        // TODO assertion
    }

}

