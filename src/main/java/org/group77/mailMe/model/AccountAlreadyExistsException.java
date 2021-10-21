package org.group77.mailMe.model;

public class AccountAlreadyExistsException extends Exception {
    public AccountAlreadyExistsException(String msg) {
        super(msg);
    }
}
