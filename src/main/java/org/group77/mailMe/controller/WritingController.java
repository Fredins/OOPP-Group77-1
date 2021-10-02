package org.group77.mailMe.controller;

import javafx.beans.value.*;
import javafx.fxml.*;
import javafx.scene.control.*;
import javafx.stage.*;
import javafx.util.*;
import org.group77.mailMe.model.*;
import org.group77.mailMe.model.data.*;

import java.io.*;
import java.util.*;

public class WritingController {
  @FXML private TextField toField;
  @FXML private Label fromLabel;
  @FXML private TextField contentField;
  @FXML private Button sendBtn;
  @FXML private Button attachBtn;
  @FXML private TextField subjectField;
  private final List<String> attachments = new ArrayList<>();

  void init(Model m) {
    init(m, null);
  }

  void init(Model m, String to) {
    if (to != null) {
      toField.setText(to);
    }
    fromLabel.setText(m.activeAccount.get().getValue().emailAddress());
    // input handlers
    sendBtn.setOnAction(i -> send(m));
    attachBtn.setOnAction(i -> attachFiles());

    // change handlers
    m.activeAccount.addListener((ChangeListener<? super Pair<Boolean, Account>>) (o, oa, na) -> fromLabel.setText(na.getValue().emailAddress()));
  }

  private void send(Model m) {
    try {
      m.send(
        List.of(toField.getText()),
        subjectField.getText(),
        contentField.getText(),
        attachments
      );
    } catch (Exception e) {
      e.printStackTrace(); // TODO display feedback
    }
  }

  private void attachFiles() {
    FileChooser fileChooser = new FileChooser();
    File selectedFile = fileChooser.showOpenDialog(null);
    if (selectedFile != null) {
      attachments.add(selectedFile.toString());
      System.out.println("File selected >" + " " + selectedFile); // For Testing
    }
  }
}
