package org.group77.mailMe.controller;

import javafx.fxml.*;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.*;
import org.group77.mailMe.*;
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

  void init(Model m, Email e) {
    contentArea.setText(e.content());
    fromLabel.setText(e.from());
    subjectLabel.setText(e.subject());
    toLabel.setText(Arrays.toString(e.to()));
    // TODO date

    replyButton.setOnAction(i -> openWriting(m, fromLabel.getText()));
  }

  private void openWriting(Model m, String to) {
    try {
      FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("Writing.fxml"));
      Pane pane = fxmlLoader.load();
      ((WritingController) fxmlLoader.getController()).init(m, to);
      Stage stage = new Stage();
      stage.setTitle("Reply");
      stage.setScene(new Scene(pane));
      stage.show();
    } catch (IOException e1) {
      e1.printStackTrace();
    }
  }
}
