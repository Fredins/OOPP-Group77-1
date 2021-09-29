package org.group77.mejl.model;

import org.apache.commons.mail.util.MimeMessageParser;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.*;

public class GmailProvider extends EmailServiceProviderStrategy {

    public GmailProvider(){
        super("imap.gmail.com",
                "smtp.gmail.com",
                "imaps",
                "smtp",
                993,
                587
        );
    }

    @Override
    protected List<Folder> parse(Store store, int numFirstMsg, int numLastMsg) throws MessagingException {
        LinkedHashMap<String, javax.mail.Folder> map = new LinkedHashMap<>();
        map.put("Inbox", store.getFolder("INBOX"));
        map.put("All Mail", store.getFolder("[Gmail]/All Mail"));
        map.put("Sent Mail", store.getFolder("[Gmail]/Sent Mail"));
        map.put("Drafts", store.getFolder("[Gmail]/Drafts"));
        map.put("Spam", store.getFolder("[Gmail]/Spam"));
        map.put("Trash", store.getFolder("[Gmail]/Trash"));

        List<Folder> folders = new ArrayList<>();

        for (Map.Entry<String, javax.mail.Folder> entry : map.entrySet()) {
            String name = entry.getKey();
            javax.mail.Folder folder = entry.getValue();
            List<Email> emails = new ArrayList<>();
            folder.open(2);
            int count = folder.getMessageCount();

            if (folder.getMessageCount() != 0) {
                int n0 = Math.min(numFirstMsg, count);
                int n1 = Math.min(numLastMsg, count);
                for (Message message : folder.getMessages(n0, n1)) {
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
            folders.add(new Folder(name, emails));
        }
        return folders;
    }




    /**@author Alexey Ryabov
     * @param from - active account
     * @param recipients - List of to-account that email will be sent to.
     * @param subject - subject.
     * @param content - content.
     * @return
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


    /** @author Alexey Ryabov
     * Helper function. Returns Authenticated Session. For testing only.
     * @param properties
     * @param account
     * @param password
     * @return
     */
    private static Session getAuthentication (Properties properties, String account, String password) {
        Session session = Session.getInstance(properties, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(account, password);
            }
        });
        return session;
    }

    /** @author Alexey Ryabov
     * Sets properties. For testing only.
     * @param properties
     */
    private static void setGmailProperties (Properties properties) {
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");
        properties.put("mail.smtp.host", "smtp.gmail.com");
        properties.put("mail.smtp.port", "587");
    }

    /** @author Alexey Ryabov
     * Helper function. Composes a message.
     * @param session
     * @param from
     * @param recipient
     * @param subject
     * @param content
     * @return
     * @throws Exception
     */
    private static Message composingMessage (Session session, String from, String recipient, String subject, String content) throws Exception
    {
        try
        {
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
