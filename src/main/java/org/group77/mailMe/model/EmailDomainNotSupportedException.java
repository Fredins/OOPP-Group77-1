package org.group77.mailMe.model;

/**
 * Exception to signal that a given email address is not supported by the application.
 * @author Hampus Jernkrook
 */
public class EmailDomainNotSupportedException extends Exception{
    public EmailDomainNotSupportedException() {
        super("Email domain not supported");
    }
}
