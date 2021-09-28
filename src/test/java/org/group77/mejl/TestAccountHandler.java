package org.group77.mejl;
import org.group77.mejl.model.*;
import org.junit.jupiter.api.*;

import java.io.IOException;

public class TestAccountHandler{

    @Test
    void testCreateAccount() throws OSNotFoundException, IOException {
        AccountHandler handler = new AccountHandler();
        Account a = handler.createAccount("grupp77@gmail.com", "password123");
        Account a1 = handler.createAccount("gmail@outlook.com", "password123");
        Assertions.assertNotEquals(a, a1);
    }
}
