package org.group77.mejl.model;

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

        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");

        String myAccount = "iiredqueen@gmail.com"; // This is only for testing, need to be changed.
        String myPassword = "xxxxxx"; // This is for testing, needs to be changed.

        Session session = Session.getInstance(props, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(myAccount, myPassword);
            }
        });

        Message msg = composingMessage(session, myAccount, recepient);

        Transport.send(msg);
        System.out.println("Message sent successfully!");
    }

    /**
     * TODO: Connect prepareMessage to WritingView
     * 1) setFrom
     * 2) setRecipient
     * 3) setSubject
     * 4) setText
     * **/
    private static Message composingMessage (Session session, String myAccountEmail, String recepient)
    {
        try
        {
            Message msg = new MimeMessage(session);
            msg.setFrom(new InternetAddress(myAccountEmail));
            msg.setRecipient(Message.RecipientType.TO, new InternetAddress(recepient));
            msg.setSubject("Sending a test message!");
            msg.setText("Hello World!!! \n This is my first email.");
            return msg;

        } catch (Exception ex)
        {}
        return null;
    }

}
