package org.group77.mailMe.services.emailServiceProvider;

import org.group77.mailMe.model.*;
import javax.mail.*;
import javax.mail.internet.*;
import java.io.*;
import java.time.*;
import java.util.*;

/**
 * Class is a subclass to the EmailServiceProvider.
 * It is responsible for parsing information from the outlook server,
 * setting outlook properties and creating a session to be able to send an email message.
 * @author Martin Fredin.
 * @author David Zamanian.
 * @author Alexey Ryabov
 */

public class MicrosoftProvider extends EmailServiceProvider {

    /**@author Martin Fredin
     * Input information required to establish connection with the server.
     */
    public MicrosoftProvider() {
        super("outlook.office365.com",
                "smtp-mail.outlook.com",
                "imaps",
                "smtp",
                993,
                587);
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
        setMicrosoftOutlookProperties(props);

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
    private void setMicrosoftOutlookProperties(Properties properties) {
        properties.put("mail.smtp.starttls.enable","true");
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.host", this.hostOut);
        properties.put("mail.smtp.port", this.portOut);

    }

}