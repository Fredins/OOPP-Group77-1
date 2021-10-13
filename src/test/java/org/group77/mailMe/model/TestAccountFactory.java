package org.group77.mailMe.model;
import org.group77.mailMe.model.data.*;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.function.*;

public class TestAccountFactory {

    /**
     * @author Martin
     */
    @Test
    void testSupportedDomain(){
        Assertions.assertDoesNotThrow(() ->
          AccountFactory.createAccount("name@gmail.com", "pwd".toCharArray()));
    }

    @Test
    void testNotSupportedDomain(){
        Assertions.assertThrows(EmailDomainNotSupportedException.class, () ->
          AccountFactory.createAccount("gmail@wrongDomain.com", "pwd".toCharArray()));
    }
}
