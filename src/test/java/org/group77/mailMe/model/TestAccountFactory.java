package org.group77.mailMe.model;
import org.junit.jupiter.api.*;

public class TestAccountFactory {

    /**
     * @author Martin
     */
    @Test
    void testSupportedDomain(){
        Assertions.assertDoesNotThrow(() ->
          AccountFactory.createAccount("name@gmail.com", "pwd".toCharArray()));
    }

    /**
     * @author Martin
     */
    @Test
    void testNotSupportedDomain(){
        Assertions.assertThrows(EmailDomainNotSupportedException.class, () ->
          AccountFactory.createAccount("gmail@wrongDomain.com", "pwd".toCharArray()));
    }
}
