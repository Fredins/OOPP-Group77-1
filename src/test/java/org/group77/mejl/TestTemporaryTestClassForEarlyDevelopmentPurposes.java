package org.group77.mejl;

import org.group77.mejl.model.ESP;
import org.group77.mejl.model.Model;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class TestTemporaryTestClassForEarlyDevelopmentPurposes {

    @Test
    void ESPStorage(){
        Model m = new Model();
        ESP esp = new ESP(
                "gmail",
                "imap.gmail.com",
                993,
                "imaps",
                "77grupp@gmail.com",
                "grupp77group"
        );
        m.writeESP(esp);
        m.setAcitiveESP("gmail");
        ESP esp1 = m.getActiveESP();

        Assertions.assertEquals(esp, esp1);
    }


    // IN DEVELOPMENT
    @Test
    void connectESP(){
        Model m = new Model();
        ESP esp = new ESP(
                "gmail",
                "imap.gmail.com",
                993,
                "imaps",
                "77grupp@gmail.com",
                "grupp77group"
        );
        Assertions.assertTrue(m.connectESP(esp));
    }
}
