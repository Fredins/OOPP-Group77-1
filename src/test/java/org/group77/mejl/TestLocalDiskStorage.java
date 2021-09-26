package org.group77.mejl;
import org.group77.mejl.model.*;
import org.junit.jupiter.api.Test;

import java.io.IOException;

public class TestLocalDiskStorage {


    @Test
    void testRetrieveEmails() throws Exception {
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

    @Test
    void testStorage() throws Exception
    {
        AccountHandler h = new AccountHandler();
        Account a = h.createAccount("77grupp@gmail.com", "grupp77group");
        LocalDiscStorage l = new LocalDiscStorage();
        l.store(a);
        // TODO assertion
    }

}

