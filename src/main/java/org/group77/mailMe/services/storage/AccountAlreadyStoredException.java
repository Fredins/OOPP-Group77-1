package org.group77.mailMe.services.storage;

import org.group77.mailMe.services.storage.StorageException;

/**
 * Exception for when a submitted account already exists in storage.
 *
 * @author Hampus Jernkrook
 */
public class AccountAlreadyStoredException extends StorageException {
    public AccountAlreadyStoredException(String msg) {
        super(msg);
    }
}
