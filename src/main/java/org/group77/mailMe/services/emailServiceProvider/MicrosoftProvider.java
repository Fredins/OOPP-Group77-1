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
    super("outlook.office365.com",
            "smtp-mail.outlook.com",
            "imaps",
            "smtp",
            993,
            587);
  }

  /**
   * @return - boolean if email is sent successful.
   * @author Alexey Ryabov
   */
  @Override
  public void sendEmail(Account account, Email email) throws ServerException {
    String fromAccount = account.emailAddress();
    String fromAccountPassword = String.valueOf(account.password());

    //Properties for an email with host name and port.
    Properties props = new Properties();
    setMicrosoftOutlookProperties(props);

    // An email is sent to every address in the list.
    for (String recipient : email.to()) {
      try{
        Message msg = composingMessage(getAuthentication(props, fromAccount, fromAccountPassword), recipient, email);
        Transport.send(msg);
      }catch (MessagingException e){
        throw new ServerException(e);
      }
    }
  }

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
        String contentType = message.getContentType();

        //Attachments
        List<Attachment> attachments = new ArrayList<>();
        byte[] fileAsBytes = null;

        //Local Time
        LocalDateTime receivedDate = resolveReceivedDate((MimeMessage) message);  //get the received date of the email

        try {
          if (contentType.contains("multipart")) {
            Multipart multipart = (Multipart) message.getContent();

            int numberOfPart = multipart.getCount();
            for (int partCount = 0; partCount < numberOfPart; partCount++) {
              MimeBodyPart part = (MimeBodyPart) multipart.getBodyPart(partCount);
              if (Part.ATTACHMENT.equalsIgnoreCase(part.getDisposition())) {
                //Read file name as string
                String fileName = part.getFileName();
                //Converting MimeBodyPart as input stream.
                InputStream inputStream = part.getInputStream();
                //Converting attachment as outputstream to array of bytes
                fileAsBytes = attachmentAsStream(inputStream).toByteArray();
                //Adding file and its name to hashmap
                attachments.add(new Attachment(fileName, fileAsBytes, null));

              } else {
                //If content is not an attachment
                content = part.getContent().toString();
              }
            }
          } else if (contentType.contains("text/plain") || contentType.contains("text/html")) {
            Object c = message.getContent();
            if (c != null) {
              content = c.toString();
            }
          }
        } catch (Exception e) {
          e.printStackTrace();
        }

        emails.add(new Email(
                from,
                to,
                subject,
                content,
                attachments,
                receivedDate
        ));
      }
    }
    return emails;
  }

  /**@author Alexey Ryabov
   * Converts inputstream into a bytearrayoutputstream
   * @param inputStream - email attachment part as inputstream.
   * @return - attachment as byte array
   */
  private ByteArrayOutputStream attachmentAsStream (InputStream inputStream) {
    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
    byte[] buffer = new byte[1000000]; // max size per attachment is 1mb = 1000000 byte.
    try {
      for (int numOfBytes; (numOfBytes = inputStream.read(buffer)) != -1;) {
        //Converting inputStream to outputStream
        outputStream.write(buffer, 0, numOfBytes);
      }
    } catch (IOException ex) {
      ex.printStackTrace();
    }
    //Converting outputStream to array of bytes
    return outputStream;
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
   * @param session   - session of the connection.
   * @param recipient - email address of the recipient.
   * @return - message object.
   * @throws Exception
   * @author Alexey Ryabov
   * Composes a message, returnt message object..
   */
  private Message composingMessage(Session session, String recipient, Email email) {
    try {
      Message msg = new MimeMessage(session);
      msg.setFrom(new InternetAddress(email.from())); // From.
      msg.setRecipient(Message.RecipientType.TO, new InternetAddress(recipient)); //To, recipient.
      msg.setSubject(email.subject()); //Subject
      msg.setContent(messageMultiPart(email)); //content split into multipart. Text content, attachment content.
      msg.setSentDate(new Date()); //Local date

      return msg;
    } catch (Exception e) {
      e.printStackTrace();
      return null;
    }
  }

  /**@author Alexey Ryabov
   * Creates an multiplart for an email with text-part and attachment-part.
   * @param email - new email.
   * @return - multipart with content and attachments.
   * @throws MessagingException
   * @throws IOException
   */
  private Multipart messageMultiPart (Email email) throws MessagingException, IOException {
    // Create the Multipart and add MimeBodyParts to it.
    Multipart multipart = new MimeMultipart();
    // Create and fill the first message part.
    MimeBodyPart messageBodyPart = new MimeBodyPart();
    //Content of the message.
    messageBodyPart.setContent(email.content(), "text/html");
    // Add multipart to message.
    multipart.addBodyPart(messageBodyPart);
    // adding attachments:
    for(Attachment attachment : email.attachments()){
      MimeBodyPart mimeBodyPart = new MimeBodyPart();
      mimeBodyPart.attachFile(attachment.file());
      multipart.addBodyPart(mimeBodyPart);
    }
    return multipart;
  }

  /**
   * Get the received date from an email.
   *
   * @param message - the message to find the received date for.
   * @return The received date of the email if it could be found and parsed, else the current date.
   * @throws MessagingException
   * @author Hampus Jernkrook
   */
  private LocalDateTime resolveReceivedDate(MimeMessage message) throws MessagingException {
    LocalDateTime res = LocalDateTime.now(ZoneId.systemDefault());
    // if received date can be extracted directly, use that.
    if (message.getReceivedDate() != null) {
      return message.getReceivedDate()
              .toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
    }
    if (message.getSentDate() != null) {
      return message.getSentDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
    }
    String[] receivedHeaders = message.getHeader("Received");
    // if no received-header was found, return the current date
    System.out.println("\nHEADER IS >> " + Arrays.toString(receivedHeaders) + "\n"); //TODO remove
    // if no recieved-header was found, return the current date
    if (receivedHeaders == null) {
      return res;
    }
    // if header was found then scan it for the date and parse into LocalDateTime format.
    // TODO
    // if no date was found or could be parsed, set current date as received date
    return res;
  }
}
