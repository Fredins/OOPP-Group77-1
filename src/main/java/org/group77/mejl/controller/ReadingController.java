package org.group77.mejl.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.layout.AnchorPane;
import org.group77.mejl.model.Email;

public class ReadingController {
    private Email email;
    @FXML private Label fromLabel;
    @FXML private Label subjectLabel;
    @FXML private Label toLabel;
    @FXML private ScrollPane scrollPane;
    @FXML private AnchorPane anchorPaneInScrollPane;
    @FXML private TextArea contentTextArea;

    /**
     * @author David Zamanian
     *
     * Constructor. Sets all the fileds to the correct attributes
     *
     * @param email The email that will be displayed in the readingView
     */

    public void init(Email email){
        this.email = email;
        this.toLabel.setText(String.valueOf(email.getTo()));
        this.subjectLabel.setText(email.getSubject());
        this.fromLabel.setText(email.getFrom());
        this.contentTextArea.setText(email.getContent());
    }



}
