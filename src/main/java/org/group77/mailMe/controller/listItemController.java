package org.group77.mailMe.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;
import org.group77.mailMe.Main;
import org.group77.mailMe.control.ApplicationManager;
import org.group77.mailMe.model.Email;

import java.io.IOException;

public class listItemController extends FlowPane {

  ApplicationManager appManager;
  private MainController parentController;
  Email email;
  @FXML
  private Label from;
  @FXML
  private Label subject;
  @FXML
  private AnchorPane itemPane;

  /**
   * @param email
   * @Author David Zamanian
   * <p>
   * Initializes the listItems with the emails in them. Sets the labels to the correct text.
   */

  public void init(ApplicationManager appManager, Email email, MainController parent) {
    this.appManager = appManager;
    this.parentController = parent;
    this.email = email;
    this.from.setText(email.getFrom());
    this.subject.setText(email.getSubject());
  }

  /**
   * @throws IOException
   * @author David Zamanian
   * <p>
   * opens the readingView in the readingFlowPane in MainController/MainView.
   */


  @FXML
  void readEmail() throws IOException {

    parentController.readingFlowPane.getChildren().clear();
    FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("ReadingView.fxml"));
    parentController.readingFlowPane.getChildren().add(fxmlLoader.load());
    ReadingController controller = fxmlLoader.getController();
    controller.init(appManager, email, parentController);
  }


}
