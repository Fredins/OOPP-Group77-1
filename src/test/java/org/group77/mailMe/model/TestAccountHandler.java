package org.group77.mailMe.model;
import org.group77.mailMe.model.data.*;
import org.group77.mailMe.oldmodel.*;
import org.group77.mailMe.services.storage.OSNotFoundException;
import org.junit.jupiter.api.*;

import java.io.IOException;

public class TestAccountHandler{

    @Test
    void testCreateAccount() throws OSNotFoundException, IOException {
        AccountHandler handler = new AccountHandler();
        Account a = handler.createAccount("grupp77@gmail.com", "password123".toCharArray());
        Account a1 = handler.createAccount("gmail@outlook.com", "password123".toCharArray());
        Assertions.assertNotEquals(a, a1);
    }
}
