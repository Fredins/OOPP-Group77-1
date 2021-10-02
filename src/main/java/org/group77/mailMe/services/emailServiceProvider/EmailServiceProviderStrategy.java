package org.group77.mailMe.services.emailServiceProvider;

import org.group77.mailMe.model.data.*;

import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Store;
import java.util.*;


public abstract class EmailServiceProviderStrategy {

  String hostIn;
  String hostOut;
  String protocolIn;
  String protocolOut;
  int portIn;
  int portOut;

  public EmailServiceProviderStrategy(String hostIn, String hostOut, String protocolIn, String protocolOut, int portIn, int portOut) {
    this.hostIn = hostIn;
    this.hostOut = hostOut;
    this.protocolIn = protocolIn;
    this.protocolOut = protocolOut;
    this.portIn = portIn;
    this.portOut = portOut;
  }

  /**
   * @param account is a account
   * @return List<Folder> is a list of folders
   * @author Martin
   */
  public List<Email> refreshFromServer(Account account) throws MessagingException {
    return parse(connectStore(account));
  }

  /**
   * @param account is a account
   * @return boolean if the connection was successfull
   * @author Martin
   */
  public boolean testConnection(Account account) throws MessagingException {
    connectStore(account);
    return true;
  }

  /**
   * @param account is a account
   * @return Store is a list of folders
   * @author Martin
   */
  private Store connectStore(Account account) throws MessagingException {
    Properties props = new Properties();
    props.setProperty("mail.pop3.ssl.enable", "true");

    Session session = Session.getDefaultInstance((props), null);
    Store store = session.getStore(protocolIn);

    String host = hostIn;
    int port = portIn;
    String address = account.emailAddress();
    String password = String.valueOf(account.password());

    store.connect(
      hostIn,
      portIn,
      address,
      password
    );
    return store;
  }

  /**
   * @param from      - active account
   * @param recipient - to account.
   * @param subject   - subject.
   * @param content   - content.
   * @return
   * @author Alexey Ryabov
   */
  public abstract boolean sendEmail(Account from, List<String> recipient, String subject, String content, List<String> attachments) throws Exception;

  protected abstract List<Email> parse(Store store) throws MessagingException;

}
