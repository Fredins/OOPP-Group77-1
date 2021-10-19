package org.group77.mailMe.services.emailServiceProvider;

import org.apache.commons.mail.util.*;
import org.group77.mailMe.model.data.*;

import javax.mail.*;
import javax.mail.internet.*;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;

/**
 * Class is a subclass to the EmailServiceProvider.
 * It is responsible for parsing information from the gmail server,
 * setting gmail properties and creating a session to be able to send an email message.
 *
 * @author Martin Fredin.
 * @author David Zamanian.
 * @author Alexey Ryabov
 */

public class GmailProvider extends EmailServiceProvider {

    /**
     * @author Martin Fredin
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

    /**
     * @param store - is a list of folders.
     * @return List of emails.
     * @throws MessagingException
     * @author Martin Fredin.
     * @author Hampus Jernkrook (added date support).
     * @author Alexey Ryabov
     */
    @Override
    protected List<Email> parse(Store store) throws MessagingException { // TODO splitta till flera mindre metoder
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
                String attachments = "";
                byte[] fileAsBytes = null;
                Map<byte[], String> listOfFilesAsByteArray = new HashMap<>();
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
                                //Appending filename to a string. TODO maybe not needed??
                                attachments += fileName + ", ";
                                //Converting/reading MimeBodyPart as input stream.
                                InputStream inputStream = part.getInputStream();
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
                                fileAsBytes = outputStream.toByteArray();
                                //Adding file and its name to hashmap
                                listOfFilesAsByteArray.put(fileAsBytes, fileName);
                            } else {
                                content = part.getContent().toString();
                            }
                        }
                        if (attachments.length() > 1) {
                            attachments = attachments.substring(0, attachments.length() - 2);
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
                        listOfFilesAsByteArray,
                        receivedDate
                ));
            }
        }
        return emails;
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

    /**
     * @param from       - active account.
     * @param recipients - List of to-account that email will be sent to.
     * @param subject    - subject text.
     * @param content    - content text.
     * @return - boolean if email is sent successful.
     * @author Alexey Ryabov
     */
    @Override
    public void sendEmail(Account from, List<String> recipients, String subject, String content, List<File> attachments) throws Exception {
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
    }

    /**
     * @param properties - are properties of the session, are set in the sendEmail method.
     * @param account    - active account.
     * @param password   - password of the active account.
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
    private void setGmailProperties(Properties properties) { // TODO använd hostOut coh portOut
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");
        properties.put("mail.smtp.host", "smtp.gmail.com");
        properties.put("mail.smtp.port", "587");
    }

    /**
     * @param session   - session of the connection.
     * @param from      - active account.
     * @param recipient - email address of the recipient.
     * @param subject   - email subject string.
     * @param content   - email content.
     * @return - message object.
     * @throws Exception
     * @author Alexey Ryabov
     * Composes a message, returnt message object..
     */
    private Message composingMessage(Session session, String from, String recipient, String subject, String content, List<File> attachments) throws Exception {
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