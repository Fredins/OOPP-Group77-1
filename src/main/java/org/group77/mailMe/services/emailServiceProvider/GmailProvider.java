package org.group77.mailMe.services.emailServiceProvider;

import org.apache.commons.mail.util.MimeMessageParser;
import org.group77.mailMe.model.Account;
import org.group77.mailMe.model.Email;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

public class GmailProvider extends EmailServiceProviderStrategy {

  public GmailProvider() {
    super("pop.gmail.com",
      "smtp.gmail.com",
      "pop3",
      "smtp",
      995,
      587
    );
  }

  @Override
  protected List<Email> parse(Store store) throws MessagingException {
    javax.mail.Folder inbox = store.getFolder("INBOX");

    List<Email> emails = new ArrayList<>();
    inbox.open(2);

    if (inbox.getMessageCount() != 0) {
      for (Message message : inbox.getMessages()) {
        String from = message.getFrom()[0].toString();
        List<String> to = List.of(Arrays.toString(message.getAllRecipients()));
        String subject = message.getSubject();
        String content = "no content";
        try {
          MimeMessageParser parser = new MimeMessageParser((MimeMessage) message);
          content = parser.parse().getPlainContent();
        } catch (Exception e) {
          e.printStackTrace();
        }

        emails.add(new Email(
          from,
          to,
          subject,
          content
        ));
      }
    }
    return emails;
  }


  /**
   * @param from       - active account
   * @param recipients - List of to-account that email will be sent to.
   * @param subject    - subject.
   * @param content    - content.
   * @return
   * @author Alexey Ryabov
   */
  @Override
  public boolean sendEmail(Account from, List<String> recipients, String subject, String content) throws Exception {
    System.out.println("Preparing to send message.."); // For Testing

    String fromAccount = from.getEmailAddress();
    String fromAccountPassword = from.getPassword();

    Properties props = new Properties();
    setGmailProperties(props);

    for (String recipient : recipients) {
      Message msg = composingMessage(getAuthentication(props, fromAccount, fromAccountPassword), fromAccount, recipient, subject, content);

      Transport.send(msg);

      System.out.println("Message sent successfully!"); // For Testing.
    }
    return true;
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
  private static void setGmailProperties(Properties properties) {
    properties.put("mail.smtp.auth", "true");
    properties.put("mail.smtp.starttls.enable", "true");
    properties.put("mail.smtp.host", "smtp.gmail.com");
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
      System.out.println(recipient); // For testing.
      msg.setRecipient(Message.RecipientType.TO, new InternetAddress(recipient));
      msg.setSubject(subject);
      msg.setText(content);
      return msg;

    } catch (Exception e) {
      e.printStackTrace();
      return null;
    }
  }
}
