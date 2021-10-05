package org.group77.mailMe;

import javafx.application.*;
import javafx.fxml.*;
import javafx.scene.*;
import javafx.scene.layout.*;
import javafx.stage.*;
import javafx.util.Pair;
import org.group77.mailMe.controller.*;
import org.group77.mailMe.model.*;
import org.group77.mailMe.services.storage.*;

import java.io.*;

public class Main extends Application {
  @Override
  public void start(Stage stage) throws IOException, OSNotFoundException {
    FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("Master.fxml"));
    Pane pane = fxmlLoader.load();
    ((MasterController) fxmlLoader.getController()).init(new Model());
    Scene scene = new Scene(pane, 1050, 700);
    stage.setTitle("MailMe");
    stage.setScene(scene);
    stage.show();
  }

  public static void main(String[] args) {
    launch();
  }
}
