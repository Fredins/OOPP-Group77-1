package org.group77.mejl;
import org.group77.mejl.model.*;
import org.junit.jupiter.api.Test;

import java.io.IOException;

public class TestLocalDiskStorage {


    @Test
    void testRetrieveEmails() throws Exception {
        LocalDiscStorage storage = new LocalDiscStorage();
        AccountHandler handler = new AccountHandler();
        Account a = handler.createAccount("grupp77@gmail.com", "grupp77group");
        handler.setActiveAccount(a.getEmailAddress());
        ApplicationManager am = new ApplicationManager();
        am.refreshFromServer();
        String folderName = "inbox";
        storage.retrieveEmails(handler.getActiveAccount().getEmailAddress(), folderName);
    }

    @Test
    void testStorage() throws Exception
    {
        AccountHandler h = new AccountHandler();
        Account a = h.createAccount("grupp77@gmail.com", "grupp77group");
        LocalDiscStorage l = new LocalDiscStorage();
        l.store(a);
    }

}

