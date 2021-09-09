package org.group77.mejl.model;

import com.sun.mail.imap.IMAPFolder;
import com.sun.mail.imap.IMAPStore;
import com.sun.mail.imap.protocol.ListInfo;

import javax.mail.Folder;

public class ImapsFolder{
    private final IMAPFolder imapFolder;

    public ImapsFolder(IMAPFolder imapFolder) {
        this.imapFolder = imapFolder;
    }

    @Override
    public String toString() {
        return imapFolder.getName();
    }
}
