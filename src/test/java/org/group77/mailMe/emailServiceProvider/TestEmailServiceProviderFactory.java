package org.group77.mailMe.emailServiceProvider;

import static org.junit.Assert.*;

import org.group77.mailMe.model.*;
import org.group77.mailMe.model.data.*;
import org.group77.mailMe.model.exceptions.*;
import org.group77.mailMe.services.emailServiceProvider.EmailServiceProviderFactory;
import org.group77.mailMe.services.emailServiceProvider.EmailServiceProvider;
import org.group77.mailMe.services.emailServiceProvider.GmailProvider;
import org.junit.Test;

/**
 * @author Hampus Jernkrook
 */
public class TestEmailServiceProviderFactory {
    // test that gmail account gets associated with a gmail provider
    @Test
    public void TestGmailAccount() throws EmailDomainNotSupportedException {
        AccountFactory accountFactory = new AccountFactory();
        Account account = accountFactory.createAccount("lol@gmail.com", "1345".toCharArray());
        EmailServiceProvider esp = EmailServiceProviderFactory.createEmailServiceProvider(account);
        assertTrue(esp instanceof GmailProvider);
    }

    /*
    // test that microsoft (hotmail here) account gets associated with a microsoft provider
    @Test
    public void TestMicrosoftAccount() {
        AccountFactory accountFactory = new AccountFactory();
        Account account = accountFactory.createAccount("lol@hotmail.com", "1234");
        EmailServiceProviderFactory espFactory = new EmailServiceProviderFactory();
        EmailServiceProviderStrategy esp = espFactory.getEmailServiceProvider(account);
        assertTrue(esp instanceof MicrosoftProvider);
    }
     */

}
