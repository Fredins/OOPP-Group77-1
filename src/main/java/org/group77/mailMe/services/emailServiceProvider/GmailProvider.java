package org.group77.mailMe.services.emailServiceProvider;

import org.group77.mailMe.model.data.*;
import org.group77.mailMe.model.exceptions.*;

import javax.mail.*;
import javax.mail.internet.*;
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
        setGmailProperties(props);

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

}