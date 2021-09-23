package org.group77.mejl.model;

import java.util.List;

public class GmailProvider extends EmailServiceProviderStrategy {


    @Override
    protected List<Folder> parse(javax.mail.Folder[] folders) {
        return null;
    }
}
