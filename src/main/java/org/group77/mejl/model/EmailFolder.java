package org.group77.mejl.model;

import com.sun.mail.imap.IMAPFolder;
import com.sun.mail.imap.IMAPStore;
import com.sun.mail.imap.protocol.ListInfo;

import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;

public class EmailFolder {
    private final IMAPFolder imapFolder;

    public EmailFolder(IMAPFolder imapFolder) {
        this.imapFolder = imapFolder;
    }

    public Message[] getMessages() throws MessagingException {
        return imapFolder.getMessages();
    }

    public void open(int mode) throws MessagingException {
        imapFolder.open(mode);
    }

    public void close() throws MessagingException {
        imapFolder.close();
    }

    @Override
    public String toString() {
        return imapFolder.getName();
    }
}
