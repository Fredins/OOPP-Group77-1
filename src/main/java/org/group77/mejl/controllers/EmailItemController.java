package org.group77.mejl.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import java.util.Arrays;

public class EmailItemController {
    @FXML
    private Label from;

    @FXML
    private Label subject;

    private Message message;

    public void init(Message message) throws MessagingException {
       this.message = message;
       from.setText(String.valueOf(message.getFrom()[0]));
       subject.setText(message.getSubject());
    }

    @FXML
    void readEmail() {

    }
}
