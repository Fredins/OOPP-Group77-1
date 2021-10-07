package org.group77.mailMe.services.storage;

/**
 * Exception for when a submitted account already exists in storage.
 *
 * @author Hampus Jernkrook
 */
public class AccountAlreadyExistsException extends Exception {
    public AccountAlreadyExistsException(String msg) {
        super(msg);
    }
}
