package org.group77.mailMe.services.emailServiceProvider;

import org.group77.mailMe.model.Account;
import org.group77.mailMe.model.ServerProvider;

/**
 * @author Hampus Jernkrook.
 * <p>
 * This class determines what email service provider to use depending on
 * the account details.
 */
public abstract class EmailServiceProviderFactory {

    /**
     * Determines what EmailServiceProviderStrategy to use depending on
     * account's ServerProvider enum.
     *
     * @param account - the account that should be connected against some
     *                email service provider.
     * @return an EmailServiceProviderStrategy.
     * @throws ServerException if email server is not supported
     * @author Hampus Jernkrook
     */
    public static EmailServiceProvider createEmailServiceProvider(Account account) throws ServerException {
        if (account.provider() == ServerProvider.GMAIL) {
            return new GmailProvider();
        } else if (account.provider() == ServerProvider.MICROSOFT) {
            return new MicrosoftProvider();
        } else {
            throw new ServerException();
        }
    }
}
