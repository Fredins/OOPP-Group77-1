package org.group77.mailMe.controller;

import javafx.fxml.*;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.*;
import org.group77.mailMe.*;
import org.group77.mailMe.controller.utils.*;
import org.group77.mailMe.model.*;
import org.group77.mailMe.model.data.*;

import java.io.*;
import java.util.*;

public class ReadingController {
  @FXML private TextArea contentArea;
  @FXML private Label fromLabel;
  @FXML private Label subjectLabel;
  @FXML private Label toLabel;
  @FXML private Label dateLabel;
  @FXML private Button replyButton;
  @FXML private Button bin;

  /**
   * 1. set initial values for nodes
   * 2. set event handler for node
   * @param model the model
   * @param email the corresponding email
   * @author Martin, David
   */
  void init(Model model, Email email) {
    contentArea.setText(email.content());
    fromLabel.setText(email.from());
    subjectLabel.setText(email.subject());
    toLabel.setText((Arrays.toString(email.to())));
    // TODO date
    bin.setOnAction(i -> {
      try {
        DeleteEmail(model);
      } catch (Exception e) {
        e.printStackTrace();
      }
    });
    replyButton.setOnAction(inputEvent -> WindowOpener.openReply(model, fromLabel.getText()));
  }

  /**
   * Removes brackets from a string. Used to remove "[" and "]" from recipients.
   * @param s
   * @author David Zamanian
   */

  private String removeBrackets(String s){
    s = s.replaceAll("[\\[\\](){}]","");
    return s;
  }

  //TODO Could potentially move these methods below into MasterController but then we need to make some changes in the GUI

  /**
   * Calls the DeleteEmail method in model
   * @param model
   * @throws Exception
   * @author David Zamanian
   */

  @FXML
  private void DeleteEmail(Model model) throws Exception {
    model.DeleteEmail();
  }

  /**
   * Calls the MoveEmail method in model
   * @param model
   * @throws Exception
   * @author David Zamanian
   */

  @FXML
  private void MoveEmail(Model model) throws Exception {
    model.MoveEmail();
  }
}
