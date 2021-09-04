package org.group77.mejl.model;

import javax.mail.Folder;
import java.util.Locale;

public class IdentifierAndFolder {
    private final String identifier;
    private Folder folder;

    public IdentifierAndFolder(Folder folder){
        this.identifier = folder.getName().toLowerCase(Locale.ROOT);
        this.folder = folder;
    }

    public IdentifierAndFolder(String identifier){
        this.identifier = identifier;
    }

    public Folder getFolder() {
        return folder;
    }

    public String getIdentifier() {
        return identifier;
    }

    @Override
    public String toString() {
        return identifier;
    }
}
