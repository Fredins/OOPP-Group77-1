package org.group77.mailMe.services.emailServiceProvider;

import org.apache.commons.mail.util.*;
import org.group77.mailMe.model.data.*;

import javax.mail.*;
import javax.mail.internet.*;
import java.io.File;
import java.util.*;

/**
 * Class is a subclass to the EmailServiceProvider.
 * It is responsible for parsing information from the gmail server,
 * setting gmail properties and creating a session to be able to send an email message.
 * @author Martin Fredin.
 * @author David Zamanian.
 * @author Alexey Ryabov
 */

public class GmailProvider extends EmailServiceProvider {

  /**@author Martin Fredin
   * Input information required to establish connection with the server.
   */
  public GmailProvider() {
    super("pop.gmail.com",
          "smtp.gmail.com",
          "pop3",
          "smtp",
          995,
          587
    );
  }

  /** @author Martin Fredin.
   * @param store - is a list of folders.
   * @return List of emails.
   * @throws MessagingException
   */
  @Override
  protected List<Email> parse(Store store) throws MessagingException {
    javax.mail.Folder inbox = store.getFolder("INBOX");

    List<Email> emails = new ArrayList<>();
    inbox.open(2);

    if (inbox.getMessageCount() != 0) {
      for (Message message : inbox.getMessages()) {
        String from = message.getFrom()[0].toString();
        String[] to = Arrays.stream(message.getAllRecipients()).map(Address::toString).toArray(String[]::new);
        String subject = message.getSubject();
        String content = "no content";
        try {
          MimeMessageParser parser = new MimeMessageParser((MimeMessage) message);
          //content = parser.parse().getPlainContent();
          content = parser.parse().getHtmlContent();
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
   * @param from       - active account.
   * @param recipients - List of to-account that email will be sent to.
   * @param subject    - subject text.
   * @param content    - content text.
   * @return - boolean if email is sent successful.
   * @author Alexey Ryabov
   */
  @Override
  public boolean sendEmail(Account from, List<String> recipients, String subject, String content, List<File> attachments) throws Exception {
    //System.out.println("Preparing to send message.."); // For Testing

    String fromAccount = from.emailAddress();
    String fromAccountPassword = String.valueOf(from.password());

    Properties props = new Properties();
    setGmailProperties(props);
  
    // An email is sent to every address in the list.
    for (String recipient : recipients) {
      Message msg = composingMessage(getAuthentication(props, fromAccount, fromAccountPassword), fromAccount, recipient, subject, content, attachments);

      Transport.send(msg);

      //System.out.println("Message sent successfully!"); // For Testing.
    }
    return true;
  }

  /**
   * @param properties - are properties of the session, are set in the sendEmail method.
   * @param account - active account.
   * @param password - password of the active account.
   * @return - session object.
   * @author Alexey Ryabov
   * Asks server to authorice the session. Returns Authenticated Session.
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
   * @param properties - are connection properties to the server.
   * @author Alexey Ryabov
   */
  private static void setGmailProperties(Properties properties) {
    properties.put("mail.smtp.auth", "true");
    properties.put("mail.smtp.starttls.enable", "true");
    properties.put("mail.smtp.host", "smtp.gmail.com");
    properties.put("mail.smtp.port", "587");
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
  private static Message composingMessage(Session session, String from, String recipient, String subject, String content, List<File> attachments) throws Exception {
    try {
      Message msg = new MimeMessage(session);
      msg.setFrom(new InternetAddress(from));
      System.out.println(recipient); // For testing.
      msg.setRecipient(Message.RecipientType.TO, new InternetAddress(recipient));
      msg.setSubject(subject);
      //msg.setText(content);
      // Create the Multipart and add MimeBodyParts to it.
      Multipart multipart = new MimeMultipart();
      // Create and fill the first message part.
      MimeBodyPart messageBodyPart = new MimeBodyPart();
      //Content of the message.
      messageBodyPart.setContent(content, "text/html");
      // Add multipart to message.
      multipart.addBodyPart(messageBodyPart);
      msg.setContent(multipart);
      // adding attachments:
      if (attachments.size() > 0) {
        // For every file in attachments list, created new MimeBodyPart and add it to Multipart.
        for (File file : attachments) {
          MimeBodyPart mimeBodyPart = new MimeBodyPart();
          mimeBodyPart.attachFile(file);
          multipart.addBodyPart(mimeBodyPart);
        }
      }
      msg.setSentDate(new Date());
      return msg;
    } catch (Exception e) {
      e.printStackTrace();
      return null;
    }
  }
}
