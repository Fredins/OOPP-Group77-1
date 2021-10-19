package org.group77.mailMe.model.exceptions;

public class EmailDomainNotSupportedException extends Exception{
    public EmailDomainNotSupportedException() {
        super("Email domain not supported");
    }
}
