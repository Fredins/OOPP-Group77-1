package org.group77.mejl.model;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeUtility;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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

    /**
     * @author Martin
     * @param n name of folder
     * @param f is a javax.mail.Folder
     * @return Folder a folder which is not dependent on javax.mail
     * @throws MessagingException is javax.mail can't retrieve mails from folder
     */
    private Folder createFolder(String n, javax.mail.Folder f) throws MessagingException {
        List<Email> emails = Stream.of(f.getMessages())
            .map(m -> {
                        try {
                            return createEmail(m);
                        } catch (MessagingException | IOException e) {
                            e.printStackTrace();
                            return null;
                        }
                    }
                )
            .filter(Objects::nonNull)
            .collect(Collectors.toList());

        return new Folder(n, emails);
    }

    /**
     * @author Martin
     * @param m is the email from the javax.mail api
     * @return Email is an api-independent email object
     * @throws MessagingException if the javax.mail can't retrieve data about message object
     * @throws IOException if the io utils can't parse/unparse the message streams
     */
    private Email createEmail(javax.mail.Message m) throws MessagingException, IOException {
        String encoding = MimeUtility.getEncoding(m.getDataHandler().getDataSource());
        InputStream inputStream = MimeUtility.decode(m.getInputStream(), encoding);

        String content = new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
        List<String> to = List.of(Arrays.toString(m.getAllRecipients()));
        return new Email(
                m.getFrom()[0].toString(),
                to,
                m.getSubject(),
                content
        );

    }

    /**
     * @author Martin
     * @param store is the connection to the ESP
     * @return List<Folder> a list of folders which are not dependent on javax.mail
     */
    @Override
    protected List<Folder> parse(Store store){
        Function<String, javax.mail.Folder> getFolder = s -> {
            try {
                javax.mail.Folder f = store.getFolder(s);
                // store.getFolder doesn't throw exception for some reason but f.open does.
                f.open(javax.mail.Folder.READ_WRITE);
                return f;
            }catch(MessagingException e){
                e.printStackTrace();
                return null;
            }
        };

        Map<String, javax.mail.Folder> map = Map.of(
                "Inbox", getFolder.apply("INBOX"),
                "All Mail", getFolder.apply("[Gmail]/All Mail"),
                "Sent Mail", getFolder.apply("[Gmail]/Sent Mail"),
                "Drafts", getFolder.apply("[Gmail]/Drafts"),
                "Spam", getFolder.apply("[Gmail]/Spam"),
                "Trash", getFolder.apply("[Gmail]/Trash"));

        return map
            .entrySet()
            .stream()
            .filter(e -> e.getValue() != null)
            .map(e -> {
                try {
                    return createFolder(e.getKey(), e.getValue());
                } catch (MessagingException ex) {
                    ex.printStackTrace();
                    return null;
                }
            })
            .filter(Objects::nonNull)
            .collect(Collectors.toList());
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
        System.out.println("Preparing to send message..");

        String fromAccount = from.getEmailAddress();
        String fromAccountPassword = from.getPassword();

        //For Testing Only!
        String TESTmyAccount = "77grupp@gmail.com";
        String TESTmyPassword = "grupp77group";

        Properties props = new Properties();
        setGmailProperties(props);

        for (String recipient : recipients) {
            Message msg = composingMessage(getAuthentication(props, TESTmyAccount, TESTmyPassword), TESTmyAccount, recipient, subject, content);

            Transport.send(msg);

            System.out.println("Message sent successfully!");
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
            msg.setRecipient(Message.RecipientType.TO, new InternetAddress(recipient));
            msg.setSubject(subject);
            msg.setText(content);
            return msg;

        } catch (Exception ex) { throw new Exception("Something went wrong in EmailBuilder->composingMessage method !!!");}
    }
}
