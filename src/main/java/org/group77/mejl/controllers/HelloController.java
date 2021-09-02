package org.group77.mejl.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import org.group77.mejl.model.Model;

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
            Message[] messages = model.checkMails("INBOX");
            for (Message m: messages){
               System.out.printf("from: %s\nsubject: %s\n\n", m.getFrom()[0], m.getSubject());
            }
        } catch (MessagingException e){
            label_fetch.setText("fetching mails failed");
        }

    }
}