package org.group77.mejl.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.FlowPane;
import org.group77.mejl.model.Email;

public class listItemController extends FlowPane {

    Email email;
    @FXML private Label from;
    @FXML private Label subject;

    /**
     * @Author David Zamanian
     *
     * Initializes the listItems with the emails in them. Sets the labels to the correct text.
     *
     * @param email
     */

    public void init(Email email){
        this.email = email;
        this.from.setText(email.getFrom());
        this.subject.setText(email.getSubject());
    }

    @FXML
    void readEmail() {

        //For testing only
        System.out.println("readEmail - Click!");
    }

    @FXML
    public void openReadingView(){};

}
