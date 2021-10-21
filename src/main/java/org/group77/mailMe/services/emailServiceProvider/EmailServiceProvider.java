package org.group77.mailMe.services.emailServiceProvider;

import org.group77.mailMe.model.*;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.time.ZoneId;
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

    /**
     * @param store - is a list of folders.
     * @return List of emails.
     * @throws MessagingException
     * @author Martin Fredin.
     * @author Hampus Jernkrook (added date support).
     * @author Alexey Ryabov
     */
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
    protected ByteArrayOutputStream attachmentAsStream (InputStream inputStream) {
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

    /**@author Alexey Ryabov
     * Creates a multiplart for an email with text-part and attachment-part.
     * @param email - new email.
     * @return - multipart with content and attachments.
     */
    protected Multipart messageMultiPart (Email email) throws MessagingException, IOException {
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
     * @param session   - session of the connection.
     * @param recipient - email address of the recipient.
     * @return - message object.
     * @author Alexey Ryabov
     * Composes a message, returnt message object..
     */
    protected Message composingMessage(Session session, String recipient, Email email) {
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

    /**
     * @param properties - are properties of the session, are set in the sendEmail method.
     * @param account    - active account.
     * @param password   - password of the active account.
     * @return - session object.
     * @author Alexey Ryabov
     * Asks server to authorice the session. Returns Authenticated Session.
     */
    protected Session getAuthentication(Properties properties, String account, String password) {
        return Session.getInstance(properties, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(account, password);
            }
        });
    }

    /**
     * Get the received date from an email.
     *
     * @param message - the message to find the received date for.
     * @return The received date of the email if it could be found and parsed, else the current date.
     * @author Hampus Jernkrook
     */
    protected LocalDateTime resolveReceivedDate(MimeMessage message) throws MessagingException {
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
        // if header was found then scan it for the date and parse into LocalDateTime format.
        // TODO
        // if no date was found or could be parsed, set current date as received date
        return res;
    }
}
