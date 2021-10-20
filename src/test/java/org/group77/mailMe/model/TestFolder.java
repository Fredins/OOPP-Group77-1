package org.group77.mailMe.model;


import org.group77.mailMe.model.data.Email;
import org.group77.mailMe.model.data.Folder;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TestFolder {

    /**
     * @author Elin
     */
    @Test
    public void testFolderName() {
        String a1 = "Inbox";

        List<Email> emails = Arrays.asList(
                new Email("test@gmail.com", new String[]{"77grupp@gmail.com"}, "Hello", "my message", null, null),
                new Email("test@gmail.com", new String[]{"77grupp@gmail.com"}, "Hello2", "another message", null, null),
                new Email("test@gmail.com", new String[]{"77grupp@gmail.com"}, "Hello3", "very important email", null, null));

        Folder folder = new Folder(a1, emails);

        String a2 = folder.name();

        Assertions.assertEquals(a1,a2);
    }

    /**
     * @author Elin
     */
    @Test
    public void testFolderEmails() {

        List<Email> a1 = Arrays.asList(
                new Email("test@gmail.com", new String[]{"77grupp@gmail.com"}, "Hello", "my message", null, null),
                new Email("test@gmail.com", new String[]{"77grupp@gmail.com"}, "Hello2", "another message", null, null),
                new Email("test@gmail.com", new String[]{"77grupp@gmail.com"}, "Hello3", "very important email", null, null));

        Folder folder = new Folder("Inbox", a1);
        List<Email> a2 = folder.emails();

        Assertions.assertEquals(a1,a2);
    };


}
