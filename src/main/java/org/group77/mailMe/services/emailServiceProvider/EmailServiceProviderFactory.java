package org.group77.mailMe.services.emailServiceProvider;

import org.group77.mailMe.model.data.*;
import org.group77.mailMe.model.exceptions.*;

/**
 * @author Hampus Jernkrook.
 * <p>
 * This class determines what email service provider to use depending on
 * the account details.
 */
public abstract class EmailServiceProviderFactory {

  /**
   * @param account - the account that should be connected against some
   *                email service provider.
   * @return an EmailServiceProviderStrategy.
   * @author Hampus Jernkrook
   * Determines what EmailServiceProviderStrategy to use depending on
   * account's ServerProvider enum.
   */
  public static EmailServiceProvider createEmailServiceProvider(Account account) throws ServerException {
    if (account.provider() == ServerProvider.GMAIL) {
      return new GmailProvider();
    }
    else if(account.provider() == ServerProvider.MICROSOFT){
      return new MicrosoftProvider();
    } else{
      throw new ServerException();
    }
  }
}
