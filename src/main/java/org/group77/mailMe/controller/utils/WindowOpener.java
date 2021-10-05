package org.group77.mailMe.controller.utils;

import javafx.fxml.*;
import javafx.scene.*;
import javafx.scene.layout.*;
import javafx.stage.*;
import org.group77.mailMe.*;
import org.group77.mailMe.controller.*;
import org.group77.mailMe.model.*;

import java.io.*;
import java.util.function.*;

public class WindowOpener {

  public static void openMaster(Model model){
    try {
      FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("Master.fxml"));
      Pane pane = fxmlLoader.load();
      ((MasterController) fxmlLoader.getController()).init(model);
      Stage stage = new Stage();
      stage.setTitle("MailMe");
      stage.setScene(new Scene(pane));
      stage.show();
    }catch (IOException e){
      e.printStackTrace();
    }
  }

  public static void openStartPage(Model model){
    // initialize StartPageView and its Controller
    try {
      FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("StartPage.fxml"));
      Pane pane = fxmlLoader.load();
      ((StartPageController) fxmlLoader.getController()).init(model);
      Stage stage = new Stage();
      stage.setTitle("Welcome");
      stage.setScene(new Scene(pane));
      stage.show();

    }catch (IOException e){
      e.printStackTrace();
    }
  }


  public static void openAddAccount(Model model, Consumer<Node> onClose) {

    try {
      FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("AddAccount.fxml"));
      Pane pane = fxmlLoader.load();
      ((AddAccountController) fxmlLoader.getController()).init(model, onClose);
      Stage stage = new Stage();
      stage.setTitle("Add Account");
      stage.setScene(new Scene(pane));
      stage.show();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }


  public static void openWriting(Model model) {
    try {
      FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("Writing.fxml"));
      Pane pane = fxmlLoader.load();
      ((WritingController) fxmlLoader.getController()).init(model);
      Stage stage = new Stage();
      stage.setTitle("New MeMail");
      stage.setScene(new Scene(pane));
      stage.show();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public static void openReply(Model model, String to) {
    try {
      FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("Writing.fxml"));
      Pane pane = fxmlLoader.load();
      ((WritingController) fxmlLoader.getController()).init(model, to);
      Stage stage = new Stage();
      stage.setTitle("Reply");
      stage.setScene(new Scene(pane));
      stage.show();
    } catch (IOException e1) {
      e1.printStackTrace();
    }
  }
}
