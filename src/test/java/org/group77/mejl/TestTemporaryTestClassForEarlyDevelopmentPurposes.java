package org.group77.mejl;

import org.group77.mejl.model.AccountInformation;
import org.junit.jupiter.api.Test;

public class TestTemporaryTestClassForEarlyDevelopmentPurposes {

    @Test
    void ESPStorage(){
        AccountInformation accountInformation = new AccountInformation(
                "gmail",
                "imap.gmail.com",
                993,
                "imaps",
                "77grupp@gmail.com",
                "grupp77group"
        );

    }


    // IN DEVELOPMENT
    @Test
    void connectESP(){
        AccountInformation accountInformation = new AccountInformation(
                "gmail",
                "imap.gmail.com",
                993,
                "imaps",
                "77grupp@gmail.com",
                "grupp77group"
        );
    }
}
