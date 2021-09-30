package org.group77.mailMe.model;


import java.io.Serializable;
import java.util.Objects;

/**
 * Represents an account connected to some email service.
 */
public class Account implements Serializable {
    private final String emailAddress;
    private final String password;
    private final ServerProvider provider;

    /**
     * Constructor.
     *
     * @param emailAddress - the email address of the Account.
     * @param password     - password of the account. Must be the same as on the email server.
     * @param provider     - A ServerProvider.
     */
    public Account(String emailAddress, String password, ServerProvider provider) {
        this.emailAddress = emailAddress;
        this.password = password;
        this.provider = provider;
    }

    /**
     * @return the email address of the account
     */
    public String getEmailAddress() {
        return emailAddress;
    }

    /**
     * @return the password of the account
     */
    public String getPassword() {
        return password;
    }

    /**
     * @return the ServerProvider associated with the account.
     */
    public ServerProvider getServerProvider() {
        return provider;
    }

    /**
     * Checks if two accounts are the same.
     *
     * @param obj -  the object to compare against
     * @return true iff this instance and the input object has the
     * same email address, password and server provider.
     * @author Martin Fredin, revised by Hampus Jernkrook.
     */
    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Account)) {
            return false;
        }
        if (!(Objects.equals(this.getEmailAddress(), ((Account) obj).getEmailAddress()))) {
            return false;
        }
        if (!(Objects.equals(this.getPassword(), ((Account) obj).getPassword()))) {
            return false;
        }
        if (!(Objects.equals(this.getServerProvider(), ((Account) obj).getServerProvider()))) {
            return false;
        }
        return true;
    }

}
