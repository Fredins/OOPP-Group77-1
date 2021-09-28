package org.group77.mejl.controller;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import org.group77.mejl.model.ApplicationManager;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Stack;

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
    private TextField contentTextField;

    /** @author Alexey Ryabov
     *
     */
    @FXML
    public void sendEmail(){
        try {
            if (applicationManager.sendEmail(fromTextFieldToListOfRecipients(toTextField), subjectTextField.getText(), contentTextField.getText())) {
                // email was successfully send, close WritingView
            } else {
                // error, give error message?
            };
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    /** @author Alexey Ryabov
     * This method converts TextField string to list of recipients separated with "," -sign.
     * @param textfield - toTextField.
     * @return list of recipints mail adresses.
     */
    private List<String> fromTextFieldToListOfRecipients (TextField textfield) {
        String textFieldToString = textfield.toString();
        String strings[] = textFieldToString.split(",");
        List list = Arrays.asList(strings);

        return list;
    }

}
