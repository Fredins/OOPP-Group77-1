package org.group77.mejl.controller;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import org.group77.mejl.model.ApplicationManager;

public class WritingController {

    // author Alexey Ryabov
    ApplicationManager applicationManager = new ApplicationManager();

    @FXML
    private TextField toTextField;
    @FXML
    private TextField fromTextField;
    @FXML
    private TextField subjectTextField;
    @FXML
    private TextField contentTextfield;
    
    @FXML
    public void sendEmail(){
        try {
            if (applicationManager.sendEmail(toTextField.getText(), subjectTextField.getText(), contentTextfield.getText())) {
                // email was successfully send, close WritingView
            } else {
                // error, give error message?
            };
        }catch (Exception e){
            e.printStackTrace();
        }

    }
}
