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
            model.getFolders();
            model.getMessages("[Gmail]/Drafts");
        }catch (MessagingException e){
            e.printStackTrace();
        }

    }
}