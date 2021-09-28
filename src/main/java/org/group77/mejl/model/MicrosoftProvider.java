package org.group77.mejl.model;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.*;

public class MicrosoftProvider extends EmailServiceProviderStrategy {

    public MicrosoftProvider() {
        super("sluta", "störa", "mig", "tack", 1, 2);
    }

    @Override
    protected List<Folder> parse(Store store) {
        return null;
    }


    /**@author Alexey Ryabov
     * @param from - active account
     * @param recipients - List of to-account emails will be sent to.
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
        setMicrosoftOutlookProperties(props);

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
    private static void setMicrosoftOutlookProperties (Properties properties) {
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
