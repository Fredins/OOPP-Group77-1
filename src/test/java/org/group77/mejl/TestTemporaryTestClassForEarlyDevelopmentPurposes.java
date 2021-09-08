package org.group77.mejl;

import org.group77.mejl.model.AccountInformation;
import org.group77.mejl.model.EmailApp;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class TestTemporaryTestClassForEarlyDevelopmentPurposes {

    @Test
    void testAddEmail(){
        AccountInformation info = new AccountInformation(
                "gmail",
                "imap.gmail.com",
                993,
                "imaps",
                "77grupp@gmail.com",
                "grupp77group"
        );
        AccountInformation wrongInfo = new AccountInformation(
                "gmail",
                "imap.gmail.com",
                993,
                "imaps",
                "77grupp@gmail.com",
                "nopassword"
        );
        EmailApp emailApp = new EmailApp();
        String res = emailApp.addEmail(info.getIdentifier(),
                info.getHost(),
                info.getPort(),
                info.getProtocol(),
                info.getUser(),
                info.getPassword()
        );
        String res1 = emailApp.addEmail(
                wrongInfo.getIdentifier(),
                wrongInfo.getHost(),
                wrongInfo.getPort(),
                wrongInfo.getProtocol(),
                wrongInfo.getUser(),
                wrongInfo.getPassword()
        );
        Assertions.assertTrue(res.equals("account successfully added"));
        Assertions.assertFalse(res.equals(res1));
    }

}
