package org.group77.mailMe.services.emailServiceProvider;

import org.group77.mailMe.model.data.*;
import org.group77.mailMe.model.exceptions.*;

import javax.mail.*;
import javax.mail.internet.*;
import java.io.*;
import java.time.*;
import java.util.*;

/**
 * Class is a subclass to the EmailServiceProvider.
 * It is responsible for parsing information from the outlook server,
 * setting outlook properties and creating a session to be able to send an email message.
 * @author Martin Fredin.
 * @author David Zamanian.
 * @author Alexey Ryabov
 */

public class MicrosoftProvider extends EmailServiceProvider {

  /**@author Martin Fredin
   * Input information required to establish connection with the server.
   */
  public MicrosoftProvider() {
    super("outlook.office365.com", "smtp-mail.outlook.com", "imaps", "smtp", 993, 587);
  }

  /**
   * @param from       - active account.
   * @author Alexey Ryabov
   */
  @Override
  public void sendEmail(Account from, Email email) throws ServerException {
    System.out.println("Preparing to send message.."); // For Testing

    String fromAccount = from.emailAddress();
    String fromAccountPassword = String.valueOf(from.password());

    Properties props = new Properties();
    setMicrosoftOutlookProperties(props);


    for (String recipient : email.to()) {
      try{
        Message msg = composingMessage(getAuthentication(props, fromAccount, fromAccountPassword), fromAccount, recipient, email.subject(), email.content());
        Transport.send(msg);
      }catch (Exception e){
        throw new ServerException(e);
      }

      System.out.println("Message sent successfully!"); // For Testing
    }
  }

  @Override
  protected List<Email> parse(Store store) {  // TODO alexey implement parse method with content and attachment
    return null;
  }

    /**
     * @param properties - are properties of the session, are set in the sendEmail method.
     * @param account - active account.
     * @param password - password of the active account.
     * @return - session object.
     * @author Alexey Ryabov
     * Asks server to authorice the session. Returns Authenticated Session.
     */
    private Session getAuthentication(Properties properties, String account, String password) {
      Session session = Session.getInstance(properties, new Authenticator() {
        @Override
        protected PasswordAuthentication getPasswordAuthentication() {
          return new PasswordAuthentication(account, password);
        }
      });
      return session;
    }

    /**
     * @param properties - are connection properties to the server.
     * @author Alexey Ryabov
     */
    private void setMicrosoftOutlookProperties(Properties properties) { // TODO alexey använd hostOut, portOut
      properties.put("mail.smtp.host", "smtp-mail.outlook.com");
      properties.put("mail.smtp.port", "587");
      properties.put("mail.smtp.starttls.enable","true");
      properties.put("mail.smtp.auth", "true");
    }

  /**
   * @param session - session of the connection.
   * @param from - active account.
   * @param recipient - email address of the recipient.
   * @param subject - email subject string.
   * @param content - email content.
   * @return - message object.
   * @throws Exception
   * @author Alexey Ryabov
   * Composes a message, returnt message object..
   */
  private Message composingMessage(Session session, String from, String recipient, String subject, String content) throws Exception {
    try {
      Message msg = new MimeMessage(session);
      msg.setFrom(new InternetAddress(from));
      msg.setRecipient(Message.RecipientType.TO, new InternetAddress(recipient));
      msg.setSubject(subject);
      msg.setText(content);
      return msg;
    } catch (Exception ex) {
      throw new Exception("Something went wrong in EmailBuilder->composingMessage method !!!");
    }
  }
}
