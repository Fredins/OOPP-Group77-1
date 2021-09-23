package org.group77.mejl.oldModel;

import javax.mail.Session;
import javax.mail.Transport;
import java.util.Properties;
import javax.mail.*;
import javax.mail.internet.*;



/**
 * EmailBuilder is responsible for composing a email message.
 * @sendEmail is responsible for preparing and authenticating the message.
 * @composingMessage is responsible for to, from subject and msg fields.
 *
 * **/

public class EmailBuilder {

    
    /** TODO:  Connect myAccount & myPassword to AccountHandler? **/
    public static void sendEmail (String recepient) throws Exception
    {
        System.out.println("Preparing to send message..");

        // TODO Refactor myAccount & myPassword. Need to somehow to get password and accont from AccountHandler.
        String myAccount = "77grupp@gmail.com"; // This is only for testing.
        String myPassword = "grupp77group"; // This is for testing.

        Properties props = new Properties();
        setProperties(props);

        Message msg = composingMessage(getAuthentication(props, myAccount, myPassword), myAccount, recepient);

        Transport.send(msg);

        System.out.println("Message sent successfully!");
    }

    //** Returns Authenticated Session **/
    private static Session getAuthentication (Properties properties, String account, String password)
    {
        Session session = Session.getInstance(properties, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(account, password);
            }
        });
        return session;
    }

    /**TODO: Connect Properties to the Connector **/
    private static void setProperties (Properties properties)
    {
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");
        properties.put("mail.smtp.host", "smtp.gmail.com");
        properties.put("mail.smtp.port", "587"); // Need to set port for testing: port 25 does not work? See terminal error.
    }

    /**
     * TODO: Connect composingMessage to WritingView
     * 1) setFrom
     * 2) setRecipient
     * 3) setSubject
     * 4) setText
     * **/
    private static Message composingMessage (Session session, String myAccountEmail, String recepient) throws Exception {
        try
        {
            Message msg = new MimeMessage(session);
            msg.setFrom(new InternetAddress(myAccountEmail));
            msg.setRecipient(Message.RecipientType.TO, new InternetAddress(recepient));
            msg.setSubject("Sending a test message!");
            msg.setText("Hello World!!! \n This is my first email. \n Alexey was here.");
            return msg;

        } catch (Exception ex)
        { throw new Exception("Something went wrong in EmailBuilder->composingMessage method !!!");}
    }

}
