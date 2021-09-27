package org.group77.mejl;

import org.group77.mejl.controller.MainController;
import org.group77.mejl.model.Email;
import org.group77.mejl.model.LocalDiscStorage;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class TestMainController {


    /**
     * @Author David Zamanian
     */

    //In Progress
    //@Test
    void testLoadEmails() throws IOException {

        String testAddress = "TESTING-ADDRESS@gmail.com";
        List<String> to = Arrays.asList("lol@gmail.com",
                "lol2@gmail.com",
                "lol3@gmail.com");
        List<Email> emails = Arrays.asList(
                new Email(testAddress, to, "1", "Email 1"),
                new Email(testAddress, to, "2", "Email 2"),
                new Email(testAddress, to, "3", "Email 3"),
                new Email(testAddress, to, "4", "Email 4")
        );

        MainController mc = new MainController();
        mc.loadEmails(emails);
    }
}
