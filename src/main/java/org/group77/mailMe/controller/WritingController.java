package org.group77.mailMe.controller;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import org.group77.mailMe.control.ApplicationManager;

import java.util.Arrays;
import java.util.List;

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
   * @param applicationManager
   * @Author David Zamanian
   */
  public void init(ApplicationManager applicationManager) {
    this.applicationManager = applicationManager;
  }

  /**
   * @param applicationManager
   * @Author David Zamanian
   * <p>
   * init method for replying. Set from when click in reply button in readingView
   */
  public void init(ApplicationManager applicationManager, String to) {
    this.applicationManager = applicationManager;
    this.toTextField.setText(to);
  }


  /**
   * @author Alexey Ryabov
   * From GUI, this method initialises sendEmail method in application manager.
   */
  @FXML
  public void sendEmail() {
    try {
      if (applicationManager.sendEmail(fromTextFieldToListOfRecipients(toTextField.getText()), subjectTextField.getText(), contentTextField.getText())) {
        // email was successfully send, close WritingView
      } else {
        // error, give error message?
      }
      ;
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  /**
   * @param textfield - toTextField.
   * @return list of recipints mail adresses.
   * @author Alexey Ryabov
   * This method converts TextField string to list of recipients separated with ";" -sign.
   */
  private List<String> fromTextFieldToListOfRecipients(String textfield) {
    //String textFieldToString = textfield.toString();
    String strings[] = textfield.split(";");
    List list = Arrays.asList(strings);

    //System.out.println(list); // For Testing.
    return list;
  }

}
