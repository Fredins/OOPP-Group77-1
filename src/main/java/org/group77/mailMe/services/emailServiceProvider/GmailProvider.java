package org.group77.mailMe.services.emailServiceProvider;

import org.group77.mailMe.model.*;

import javax.mail.*;
import javax.mail.internet.*;
import java.io.InputStream;
import java.time.LocalDateTime;
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
    private void setGmailProperties(Properties properties) {
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");
        properties.put("mail.smtp.host", this.hostOut);
        properties.put("mail.smtp.port", this.portOut);
    }

}