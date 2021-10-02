package org.group77.mailMe.control;

import org.group77.mailMe.model.Account;
import org.group77.mailMe.model.AccountHandler;
import org.group77.mailMe.model.Email;
import org.group77.mailMe.model.Folder;
import org.group77.mailMe.services.emailServiceProvider.EmailServiceProviderFactory;
import org.group77.mailMe.services.emailServiceProvider.EmailServiceProviderStrategy;
import org.group77.mailMe.services.storage.LocalDiscStorage;
import org.group77.mailMe.services.storage.OSNotFoundException;
import org.group77.mailMe.services.storage.Storage;

import javax.mail.MessagingException;
import java.io.IOException;
import java.util.List;

public class ApplicationManager {

  Storage storage;
  AccountHandler accountHandler;

  /**
   * @author Hampus Jernkrook
   */
  public ApplicationManager() {
    accountHandler = new AccountHandler();
    try {
      this.storage = new LocalDiscStorage();
    } catch (Exception e) {
      System.out.println(e.getMessage()); //TODO REFINE THIS (hampus)
    }
  }

  /**
   * @param emailAddress the Account's emailAddress
   * @param password     the Account's password
   * @return boolean, true if account was successfully stored,
   * false if account could not be stored.
   * @author Elin Hagman
   * <p>
   * Creates an Account and tries to store it.
   * Only stores the account if it connects to the server and it is not already stored.
   */
  public boolean addAccount(String emailAddress, String password) throws Exception {

    Account account = accountHandler.createAccount(emailAddress, password);
    EmailServiceProviderStrategy espStrategy = EmailServiceProviderFactory.getEmailServiceProvider(account);

    if (espStrategy.testConnection(account)) {

      return storage.store(account);

    } else {
      return false;
    }

  }

  public boolean setActiveAccount(String emailAddress) {
    try {
      Account account = storage.retrieveAccount(emailAddress);
      accountHandler.setActiveAccount(account);
    } catch (Exception e) {
      e.printStackTrace();
      return false;
    }
    return true;
  }

  public List<String> getEmailAddresses() {
    return storage.retrieveAllEmailAddresses();
  }

  /**
   * @param folderName the name of the desired email folder
   * @return
   * @throws OSNotFoundException    If the operating system is not found
   * @throws IOException            If there are any problems when locating the file
   * @throws ClassNotFoundException Of the classes required is not on the classpath?
   * @author David Zamanian
   * <p>
   * calls getEmails with foldername in accountHandler
   */
  public List<Email> retrieveEmails(String folderName) throws IOException, ClassNotFoundException {
    return storage.retrieveEmails(getActiveAccount().getEmailAddress(), folderName);
  }

  /**
   * @param recipient - From the GUI FieldText.
   * @param subject   - From the GUI FieldText.
   * @param content   - From the GUI FieldText.
   * @return - Needs more work.
   * @author Alexey Ryabov
   */
  public boolean sendEmail(List<String> recipient, String subject, String content, List<String> attachments) throws Exception {
    //AccountHandler accountHandler = new AccountHandler();
    //EmailServiceProviderFactory espf = new EmailServiceProviderFactory();

    Account activeAccount = accountHandler.getActiveAccount();
    EmailServiceProviderStrategy espStrategy = EmailServiceProviderFactory.getEmailServiceProvider(activeAccount);

    if (espStrategy.sendEmail(activeAccount, recipient, subject, content, attachments)) {
      return true;
    } else {
      return false;
    }
  }

  /**
   * @return List of folders that has been updated from server
   * @throws MessagingException if the folders updated from server cannot be stored
   * @author Elin Hagman
   * <p>
   * Refreshes the currently active account´s folders from server and stores them.
   * If the refreshed fodlers cannot be stored, an exception will
   * be thrown instead of returning the folders.
   */
  public void refreshFromServer() throws Exception {
    Account account = accountHandler.getActiveAccount();
    EmailServiceProviderStrategy espStrategy = EmailServiceProviderFactory.getEmailServiceProvider(account);
    List<Email> newEmails = espStrategy.refreshFromServer(account);
    // if there are any previously stored emails, add them to the list of new emails to store.
    // else just directly store what the server returned.
    try {
      newEmails.addAll(retrieveEmails("Inbox"));
    } catch (IOException | ClassNotFoundException e) {
      // nothing
    }
    storage.store(getActiveAccount().getEmailAddress(), new Folder("Inbox", newEmails));
  }

  public Account getActiveAccount() {
    return accountHandler.getActiveAccount();
  }

  // TODO: hantera exception här

  /**
   * @author Alexey Ryabov
   * TODO Test this method
   */
  public boolean storeAccount(Account account) throws Exception {
    try {
      storage.store(account);
      return true;
    } catch (Exception e) {
      e.printStackTrace();
    }
    return false;
  }

  public boolean storeFolders(List<Folder> folders) throws IOException {
    return storage.store(accountHandler.getActiveAccount().getEmailAddress(), folders);
  }


}
