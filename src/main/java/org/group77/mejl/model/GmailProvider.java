package org.group77.mejl.model;

import org.group77.mejl.controller.listItemController;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Store;
import javax.mail.internet.MimeUtility;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;
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
                return store.getFolder(s);
            }catch(MessagingException e){
                e.printStackTrace();
                return null;
            }
        };

        Map<String, javax.mail.Folder> map = Map.of(
                "Inbox", getFolder.apply("[INBOX]"),
                "All Mail", getFolder.apply("AllMail"),
                "Sent Mail", getFolder.apply("Sent"),
                "Drafts", getFolder.apply("Drafts"),
                "Trash", getFolder.apply("Trash"));

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
}
