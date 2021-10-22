package org.group77.mailMe.model;

/**
 * Exception to signal that account does not exist or can not be located.
 * @author Elin Hagman.
 */
public class AccountNotFoundException extends Exception {
    public AccountNotFoundException() {super("Unknown account");}
}
