package org.group77.mailMe.controller;

import javafx.beans.value.*;
import javafx.collections.*;
import javafx.fxml.*;
import javafx.scene.control.*;
import javafx.stage.*;
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
  private List<String> attachments = new ArrayList<>();

  void init(Model m){
    init(m, null);
  }

  void init(Model m, String to){
    if(to != null){
      toField.setText(to);
    }
    // input handlers
    sendBtn.setOnAction(i -> m.send());
    attachBtn.setOnAction(i -> attachFiles());

    // change handlers
    m.activeAccount.addListener((ChangeListener<? super Account>) (o, ov, nv) -> fromLabel.setText(nv.emailAddress()));
  }



  public void attachFiles() {
    FileChooser fileChooser = new FileChooser();
    File selectedFile = fileChooser.showOpenDialog(null);
    if(selectedFile != null){
      attachments.add(selectedFile.toString());
      System.out.println("File selected >" + " " + selectedFile); // For Testing
    }
  }
}
