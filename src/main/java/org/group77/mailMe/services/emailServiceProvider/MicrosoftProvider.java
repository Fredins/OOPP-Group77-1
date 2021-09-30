package org.group77.mailMe.services.emailServiceProvider;

import org.group77.mailMe.model.Account;
import org.group77.mailMe.model.Email;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.List;
import java.util.Properties;

public class MicrosoftProvider extends EmailServiceProviderStrategy {

  public MicrosoftProvider() {
    super("outlook.office365.com", "smtp-mail.outlook.com", "imaps", "smtp", 993, 587);
  }


  /**
   * @param from       - active account
   * @param recipients - List of to-account emails will be sent to.
   * @param subject    - subject.
   * @param content    - content.
   * @return
   * @author Alexey Ryabov
   * TODO Replace TESTAccount, TESTAccountPassword with fromAccount, fromAccountPassword.
   */
  @Override
  public boolean sendEmail(Account from, List<String> recipients, String subject, String content) throws Exception {
    System.out.println("Preparing to send message.."); // For Testing

    String fromAccount = from.getEmailAddress();
    String fromAccountPassword = from.getPassword();

    Properties props = new Properties();
    setMicrosoftOutlookProperties(props);

    for (String recipient : recipients) {
      Message msg = composingMessage(getAuthentication(props, fromAccount, fromAccountPassword), fromAccount, recipient, subject, content);

      Transport.send(msg);

      System.out.println("Message sent successfully!"); // For Testing
    }
    return true;
  }

  @Override
  protected List<Email> parse(Store store) throws MessagingException {
    return null;
  }

  /**
   * @param properties
   * @param account
   * @param password
   * @return
   * @author Alexey Ryabov
   * Helper function. Returns Authenticated Session. For testing only.
   */
  private static Session getAuthentication(Properties properties, String account, String password) {
    Session session = Session.getInstance(properties, new Authenticator() {
      @Override
      protected PasswordAuthentication getPasswordAuthentication() {
        return new PasswordAuthentication(account, password);
      }
    });
    return session;
  }

  /**
   * @param properties
   * @author Alexey Ryabov
   * Sets properties. For testing only.
   */
  private static void setMicrosoftOutlookProperties(Properties properties) {
    properties.put("mail.smtp.starttls.enable", "true");
    properties.put("mail.smtp.auth", "true");
    properties.put("mail.smtp.host", "smtp-mail.outlook.com");
    properties.put("mail.smtp.port", "587");
  }

  /**
   * @param session
   * @param from
   * @param recipient
   * @param subject
   * @param content
   * @return
   * @throws Exception
   * @author Alexey Ryabov
   * Helper function. Composes a message.
   */
  private static Message composingMessage(Session session, String from, String recipient, String subject, String content) throws Exception {
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
