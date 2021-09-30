package org.group77.mejl.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.Window;
import org.group77.mejl.model.ApplicationManager;

import java.io.File;
import java.util.*;

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
            if (attachFileToEmail()[1] != "True") {
                applicationManager.sendEmail(fromTextFieldToListOfRecipients(toTextField.getText()), subjectTextField.getText(), contentTextField.getText(), null);
            } else {
                // error, give error message?
                applicationManager.sendEmail(fromTextFieldToListOfRecipients(toTextField.getText()), subjectTextField.getText(), contentTextField.getText(), attachFileToEmail()[0]);
            };
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    /** @author Alexey Ryabov
     * This method converts TextField string to list of recipients separated with ";" -sign.
     * @param textfield - toTextField.
     * @return list of recipints mail adresses.
     */
    private List<String> fromTextFieldToListOfRecipients (String textfield) {
        //String textFieldToString = textfield.toString();
        String strings[] = textfield.split(";");
        List list = Arrays.asList(strings);

        //System.out.println(list); // For Testing.
        return list;
    }

    /** @author Alexey Ryabov
     * From GUI, this method initialises attachment method in application manager.
     */
    @FXML
    public String[] attachFileToEmail() {

        FileChooser fileChooser = new FileChooser();
        FileChooser.ExtensionFilter extentionFilter = new FileChooser.ExtensionFilter("TXT files (*.txt)", "*.txt");
        fileChooser.getExtensionFilters().add(extentionFilter);
        File selectedFile = fileChooser.showOpenDialog(null);
        if (selectedFile.exists()) {
            return new String[]{selectedFile.toString(), "True"};
        } else {
            return new String[] {null, "False"};
        }
    }

}
