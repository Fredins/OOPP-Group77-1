package org.group77.mailMe.services.emailServiceProvider;

import org.group77.mailMe.model.data.*;

/**
 * @author Hampus Jernkrook.
 * <p>
 * This class determines what email service provider to use depending on
 * the account details.
 */
public class EmailServiceProviderFactory {
  /**
   * @param account - the account that should be connected against some
   *                email service provider.
   * @return an EmailServiceProviderStrategy.
   * @author Hampus Jernkrook
   * <p>
   * Determines what EmailServiceProviderStrategy to use depending on
   * account's ServerProvider enum.
   */
  public static EmailServiceProviderStrategy getEmailServiceProvider(Account account) {
    if (account.provider() == ServerProvider.GMAIL_PROVIDER) {
      return new GmailProvider();
    } else { //TODO should this be `else if microsoft`?
      return new MicrosoftProvider();
    }
  }

}
