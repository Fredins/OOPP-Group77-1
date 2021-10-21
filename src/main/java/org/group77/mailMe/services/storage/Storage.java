package org.group77.mailMe.services.storage;

import org.group77.mailMe.model.Account;
import org.group77.mailMe.model.Folder;

import java.util.*;

/**
 * Interface for a storage solution
 *
 * @author Martin Fredin
 * @author Elin Hagman
 * @author David Zamanian
 */
public interface Storage {

    void store(Account account) throws StorageException;

    void store(Account account, List<Folder> folders) throws StorageException;

    void store(Account account, Folder folder) throws StorageException;

    void storeKnownRecipients(Account account, List<String> suggestions) throws StorageException;

    List<Account> retrieveAccounts() throws StorageException;

    List<Folder> retrieveFolders(Account account) throws StorageException;

    List<String> retrieveKnownRecipients(Account account);
}

