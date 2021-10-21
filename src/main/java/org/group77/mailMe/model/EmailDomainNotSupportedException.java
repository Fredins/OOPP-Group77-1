package org.group77.mailMe.model;

public class EmailDomainNotSupportedException extends Exception{
    public EmailDomainNotSupportedException() {
        super("Email domain not supported");
    }
}
