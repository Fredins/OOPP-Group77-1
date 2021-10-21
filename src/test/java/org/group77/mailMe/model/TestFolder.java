package org.group77.mailMe.model;


import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TestFolder {

    private static Folder folder;

    @BeforeEach
    public void setup() {
        List<Email> emails = new ArrayList<>();
        emails.add(new Email("test@gmail.com", new String[]{"77grupp@gmail.com"}, "Hello", "my message", null, null));
        emails.add(new Email("test@gmail.com", new String[]{"77grupp@gmail.com"}, "Hello2", "another message", null, null));
        emails.add(new Email("test@gmail.com", new String[]{"77grupp@gmail.com"}, "Hello3", "very important email", null, null));

        folder = new Folder("inbox",emails);
    }
    /**
     * @author Elin Hagman
     */
    @Test
    public void testFolderName() {
        String expectedName = "inbox";


        Assertions.assertEquals(expectedName,folder.name());
    }

    /**
     * @author Elin Hagman
     */
    @Test
    public void testFolderEmails() {

        List<Email> expectedEmails = new ArrayList<>();
        expectedEmails.add(new Email("test@gmail.com", new String[]{"77grupp@gmail.com"}, "Hello", "my message", null, null));
        expectedEmails.add(new Email("test@gmail.com", new String[]{"77grupp@gmail.com"}, "Hello2", "another message", null, null));
        expectedEmails.add(new Email("test@gmail.com", new String[]{"77grupp@gmail.com"}, "Hello3", "very important email", null, null));


        Assertions.assertEquals(expectedEmails,folder.emails());
    }

    @Test
    public void testAddEmail() {

        Email newEmail = new Email(
                "anna@gmail.com",
                new String[]{"greta@gmail.com"},
                "Subject...",
                "Content...",
                null);

        folder.addEmail(newEmail);

        Assertions.assertTrue(folder.emails().contains(newEmail));
    }

    @Test
    public void testAddEmailSize() {

        int sizeBeforeAdd = folder.emails().size();
        Email newEmail = new Email(
                "anna@gmail.com",
                new String[]{"greta@gmail.com"},
                "Subject...",
                "Content...",
                null);

        folder.addEmail(newEmail);

        Assertions.assertEquals(sizeBeforeAdd + 1, folder.emails().size());
    }

    @Test
    public void testDeleteEmail() {
        Email emailToBeDeleted = folder.emails().get(0);

        folder.deleteEmail(emailToBeDeleted);

        Assertions.assertFalse(folder.emails().contains(emailToBeDeleted));
    }

    @Test
    public void testDeleteEmailSize() {
        int sizeBeforeDelete = folder.emails().size();
        folder.deleteEmail(folder.emails().get(0));

        Assertions.assertEquals(sizeBeforeDelete -1,folder.emails().size());
    }


}
