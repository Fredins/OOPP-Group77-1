package org.group77.mejl;

import org.group77.mejl.model.*;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * @author Hampus Jernkrook
 */
public class TestEmailServiceProviderFactory {
    // test that gmail account gets associated with a gmail provider
    @Test
    public void TestGmailAccount() {
        AccountFactory accountFactory = new AccountFactory();
        Account account = accountFactory.createAccount("lol@gmail.com", "1234");
        EmailServiceProviderFactory espFactory = new EmailServiceProviderFactory();
        EmailServiceProviderStrategy esp = espFactory.getEmailServiceProvider(account);
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
