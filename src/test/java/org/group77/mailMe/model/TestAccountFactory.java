package org.group77.mailMe.model;
import org.junit.jupiter.api.*;

public class TestAccountFactory {

    /**
     * @author Martin Fredin
     */
    @Test
    void testGmail() throws EmailDomainNotSupportedException {
        Account account = AccountFactory.createAccount("name@gmail.com", "pwd".toCharArray());
        Assertions.assertEquals(account.provider(), ServerProvider.GMAIL);
    }

    /**
     * @author Martin Fredin
     */
    @Test
    void testMicrosoft() throws EmailDomainNotSupportedException {
        Account account = AccountFactory.createAccount("name@outlook.com", "pwd".toCharArray());
        Assertions.assertEquals(account.provider(), ServerProvider.MICROSOFT);
    }

    /**
     * @author Martin Fredin
     */
    @Test
    void testNotSupportedDomain(){
        Assertions.assertThrows(EmailDomainNotSupportedException.class, () ->
          AccountFactory.createAccount("gmail@wrongDomain.com", "pwd".toCharArray()));
    }


}
