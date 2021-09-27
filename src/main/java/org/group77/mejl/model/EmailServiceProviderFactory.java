package org.group77.mejl.model;

/** @author Hampus Jernkrook.
 *
 * This class determines what email service provider to use depending on
 * the account details.
 */
public class EmailServiceProviderFactory {
    /** @author Hampus Jernkrook
     *
     * Determines what EmailServiceProviderStrategy to use depending on
     * account's ServerProvider enum.
     * @param account - the account that should be connected against some
     *                email service provider.
     * @return an EmailServiceProviderStrategy.
     */
    public static EmailServiceProviderStrategy getEmailServiceProvider(Account account){
        if (account.getServerProvider() == ServerProvider.GMAIL) {
            return new GmailProvider();
        } else { //TODO should this be `else if microsoft`?
            return new MicrosoftProvider();
        }
    }

}
