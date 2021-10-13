package org.group77.mailMe.model;
import org.group77.mailMe.model.data.*;
import org.junit.jupiter.api.*;

public class TestAccountFactory {

    /**
     * @author Martin
     */
    @Test
    void testCreateAccount(){
        AccountFactory factory = new AccountFactory();
        Account a = factory.createAccount("77grupp@gmail.com", "password123".toCharArray());
        Account a1 = factory.createAccount("gmail@outlook.com", "password123".toCharArray());
        Assertions.assertNotEquals(a, a1);
    }
}
