package org.group77.mailMe.model;

/**
 * Exception to signal that account does not exist or can not be located.
 * @author Elin Hagman.
 */
public class ActiveAccountNotInAccounts extends Exception {
    public ActiveAccountNotInAccounts() {super("Unknown account");}
}
