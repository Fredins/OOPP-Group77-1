package org.group77.mailMe.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import org.group77.mailMe.Main;
import org.group77.mailMe.control.ApplicationManager;
import org.group77.mailMe.model.Email;

import java.io.IOException;

public class ReadingController {
  private MainController parentController;
  private Email email;
  ApplicationManager appManager;

  @FXML
  private Label fromLabel;
  @FXML
  private Label subjectLabel;
  @FXML
  private Label toLabel;
  @FXML
  private ScrollPane scrollPane;
  @FXML
  private AnchorPane anchorPaneInScrollPane;
  @FXML
  private TextArea contentTextArea;
  @FXML
  private Button answerButton;

  /**
   * @param email The email that will be displayed in the readingView
   * @author David Zamanian
   * <p>
   * Constructor. Sets all the fileds to the correct attributes
   */
  public void init(ApplicationManager appManager, Email email, MainController parent) {
    this.appManager = appManager;
    this.parentController = parent;
    this.email = email;
    this.toLabel.setText(String.valueOf(email.getTo()));
    this.subjectLabel.setText(email.getSubject());
    this.fromLabel.setText(email.getFrom());
    this.contentTextArea.setText(email.getContent());
  }

  /**
   * @throws IOException
   * @Author David Zamanian
   * <p>
   * Create a new writingController and open WritingView from reply. Set the fromTextField to fromLabel.getText
   */
  @FXML
  public void openWritingViewFromAnswer() throws IOException {
    FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("WritingView.fxml"));
    Scene scene = new Scene(fxmlLoader.load(), 600, 400);
    WritingController w = fxmlLoader.getController();
    w.init(appManager, fromLabel.getText());
    Stage stage = new Stage();
    stage.setTitle("New Reply");
    stage.setScene(scene);
    stage.show();

  }


}