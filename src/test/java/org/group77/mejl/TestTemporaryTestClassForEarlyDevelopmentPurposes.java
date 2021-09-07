package org.group77.mejl;

import org.group77.mejl.model.AccountInformation;
import org.group77.mejl.model.Model;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class TestTemporaryTestClassForEarlyDevelopmentPurposes {

    @Test
    void ESPStorage(){
        Model m = new Model();
        AccountInformation accountInformation = new AccountInformation(
                "gmail",
                "imap.gmail.com",
                993,
                "imaps",
                "77grupp@gmail.com",
                "grupp77group"
        );
        m.writeESP(accountInformation);
        m.setAcitiveESP("gmail");
        AccountInformation accountInformation1 = m.getActiveESP();

        Assertions.assertEquals(accountInformation, accountInformation1);
    }


    // IN DEVELOPMENT
    @Test
    void connectESP(){
        Model m = new Model();
        AccountInformation accountInformation = new AccountInformation(
                "gmail",
                "imap.gmail.com",
                993,
                "imaps",
                "77grupp@gmail.com",
                "grupp77group"
        );
        Assertions.assertTrue(m.connectESP(accountInformation));
    }
}
