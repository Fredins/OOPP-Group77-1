package org.group77.mejl.model;
import java.util.*;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;


public abstract class EmailServiceProviderStrategy {

    String hostIn;
    String hostOut;
    String protocolIn;
    String protocolOut;
    int portIn;
    int portOut;

    public EmailServiceProviderStrategy(String hostIn ,String hostOut, String protocolIn, String protocolOut, int portIn, int portOut){
        this.hostIn = hostIn;
        this.hostOut = hostOut;
        this.protocolIn = protocolIn;
        this.protocolOut = protocolOut;
        this.portIn = portIn;
        this.portOut = portOut;

    }

    /**
     * @author Martin
     * @param account is a account
     * @return List<Folder> is a list of folders
     */
    public List<Folder> refreshFromServer(Account account) throws MessagingException{
        return parse(connectStore(account));
    }

    /**
     * @author Martin
     * @param account is a account
     * @return boolean if the connection was successfull
     */
    public boolean testConnection(Account account) throws MessagingException {
        connectStore(account);
        return true;
    }

    /**
     * @author Martin
     * @param account is a account
     * @return Store is a list of folders
     */
    private Store connectStore(Account account) throws MessagingException {
        Session session = Session.getDefaultInstance((new Properties()), null);
        Store store = session.getStore(protocolIn);

        String host = hostIn;
        int port = portIn;
        String address = account.getEmailAddress();
        String password = account.getPassword();

        store.connect(
                hostIn,
                portIn,
                account.getEmailAddress(),
                account.getPassword()
        );
        return store;
    }

    /**@author Alexey Ryabov
     * @param from - active account
     * @param recipient - to account.
     * @param subject - subject.
     * @param content - content.
     * @return
     */
    public boolean sendEmail(Account from, String recipient, String subject, String content) throws Exception {
        System.out.println("Preparing to send message..");

        String fromAccount = from.getEmailAddress();
        String fromPassword = from.getPassword();

        //For Testing Only!
        String TESTmyAccount = "77grupp@gmail.com";
        String TESTmyPassword = "grupp77group";

        Properties props = new Properties();
        setProperties(props);

        Message msg = composingMessage(getAuthentication(props, TESTmyAccount, TESTmyPassword), TESTmyAccount, recipient, subject, content);

        Transport.send(msg);

        System.out.println("Message sent successfully!");
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
    private static void setProperties (Properties properties)
    {
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");
        properties.put("mail.smtp.host", "smtp.gmail.com");
        properties.put("mail.smtp.port", "587"); // Need to set port for testing: port 25 does not work? See terminal error.
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

    protected abstract List<Folder> parse(Store store) throws MessagingException;

}
