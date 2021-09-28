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
    ApplicationManager applicationManager;

    @FXML
    private TextField toTextField;
    @FXML
    private TextField fromTextField;
    @FXML
    private TextField subjectTextField;
    @FXML
    private TextField contentTextField;

    /**
     * @Author David Zamanian
     *
     * @param applicationManager
     */

    public void init(ApplicationManager applicationManager){
        this.applicationManager = applicationManager;
    }

    /**
     * @Author David Zamanian
     *
     * init method for replying. Set from when click in reply button in readingView
     *
     * @param applicationManager
     */

    public void init(ApplicationManager applicationManager, String to){
        this.applicationManager = applicationManager;
        this.toTextField.setText(to);
    }



    /** @author Alexey Ryabov
     * From GUI, this method initialises sendEmail method in application manager.
     */
    @FXML
    public void sendEmail(){
        try {
            ApplicationManager app = new ApplicationManager();
            init(app);
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
