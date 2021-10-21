package org.group77.mailMe.services.emailServiceProvider;

import org.group77.mailMe.model.exceptions.ServerException;
import org.group77.mailMe.model.data.*;

import javax.mail.*;
import java.io.File;
import java.util.*;


/**
 * Class is a superclass for other provider classes to extend.
 * It is responsible for testing connection to the server, connecting and gathering information from the server.
 * @author Martin Fredin.
 * @author David Zamanian.
 * @author Alexey Ryabov
 */

public abstract class EmailServiceProvider {

  String hostIn;
  String hostOut;
  String protocolIn;
  String protocolOut;
  int portIn;
  int portOut;

  /** @author - David Zamanian, Martin Fredin.
   * @param hostIn - host address for incoming connection.
   * @param hostOut - host address for outgoing connection.
   * @param protocolIn - protocol used for incoming connection.
   * @param protocolOut - protocol used for outgoing connection.
   * @param portIn - port address for incoming connection.
   * @param portOut - port address for outgoing connection.
   */
  public EmailServiceProvider(String hostIn, String hostOut, String protocolIn, String protocolOut, int portIn, int portOut) {
    this.hostIn = hostIn;
    this.hostOut = hostOut;
    this.protocolIn = protocolIn;
    this.protocolOut = protocolOut;
    this.portIn = portIn;
    this.portOut = portOut;
  }

  /**
   * @param account is an account.
   * @return List<Folder> is a list of folders.
   * @author Martin Fredin.
   */
  public List<Email> refreshFromServer(Account account) throws ServerException {
    List<Email> emails;
    try{
      Store store = connectStore(account);
      emails = parse(store);
      // closing the store is important because otherwise the email provider will not mark the emails as popped,
      // because the email provider thinks the client crashed because no QUIT command was executed.
      store.close();
    }catch (MessagingException e){
      throw new ServerException();
    }
    return emails;
  }

  /**
   * @param account is an account.
   * @return boolean if the connection was successful.
   * @author Martin
   * @author Hampus Jernkrook
   */
  public boolean testConnection(Account account) {
    try {
      connectStore(account);
    } catch (ServerException ignore) {
     return false;
    }
    return true;
  }

  /**
   * @param account is an account.
   * @return Store is a list of folders
   * @author Martin Fredin.
   */
  private Store connectStore(Account account) throws ServerException {
    Properties props = new Properties();
    props.setProperty("mail.pop3.ssl.enable", "true");

    Session session = Session.getDefaultInstance((props), null);
    Store store;
    try {
      store = session.getStore(protocolIn);

    String address = account.emailAddress();
    String password = String.valueOf(account.password());

    store.connect(
      hostIn,
      portIn,
      address,
      password
    );
    } catch (MessagingException e) {
      throw new ServerException();
    }
    return store;
  }

  /**
   * @author Alexey Ryabov
   */
  public abstract void sendEmail(Account account, Email email) throws ServerException;

  /** @author Martin Fredin.
   * @author Alexey Ryabov
   * @param store - is a list of folders.
   * @return - list of emails.
   * @throws MessagingException
   */
  protected abstract List<Email> parse(Store store) throws MessagingException;
}
