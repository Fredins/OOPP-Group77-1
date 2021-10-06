package org.group77.mailMe.services.storage;

public class AccountAlreadyExistsException extends Exception {
    public AccountAlreadyExistsException(String msg) {
        super(msg);
    }
}
