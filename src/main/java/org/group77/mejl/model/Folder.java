package org.group77.mejl.model;

import java.util.*;

/**
 * @author Hampus Jernkrook
 */
public class Folder {
    private String name;
    private List<Email> emails;

    public Folder(String name, List<Email> emails) {
        this.name = name;
        this.emails = emails;
    }

    public String getName() {
        return name;
    }

    public List<Email> getEmails() {
        return emails;
    }
}
