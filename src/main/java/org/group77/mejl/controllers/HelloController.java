package org.group77.mejl.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import org.group77.mejl.model.Model;

import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import java.util.Arrays;

public class HelloController {
    @FXML
    private Label label_fetch;

    @FXML
    private void fetchMails() {
        label_fetch.setText("fetched mails");
        Model model = new Model();
        try{
            Folder[] folders = model.getFolders();
            System.out.println(Arrays.asList(folders));
            Message[] messages = model.getMessages(folders[0]);
            Arrays.asList(messages).forEach(m -> {
                try {
                    System.out.printf("from: %s\nsubject: %s\n\n", m.getFrom()[0], m.getSubject());
                } catch (MessagingException e) {
                    e.printStackTrace();
                }
            });
        }catch (MessagingException e){
            e.printStackTrace();
        }

    }
}