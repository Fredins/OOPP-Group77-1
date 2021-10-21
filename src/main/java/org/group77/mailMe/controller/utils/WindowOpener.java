package org.group77.mailMe.controller.utils;

import javafx.fxml.*;
import javafx.scene.*;
import javafx.scene.layout.*;
import javafx.stage.*;
import org.group77.mailMe.*;
import org.group77.mailMe.controller.*;
import org.group77.mailMe.model.*;

import java.io.*;
import java.util.*;
import java.util.function.*;

/**
 * util class for opening windows
 */
public class WindowOpener {

  /**
   * open master window
   * @param control the control layer
   * @author Martin Fredin
   */
  public static void openMaster(Control control){
    try {
      FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("View/Master.fxml"));
      Pane pane = fxmlLoader.load();
      ((MasterController) fxmlLoader.getController()).init(control);
      Stage stage = new Stage();
      stage.setTitle("MailMe");
      stage.setScene(new Scene(pane, 1800, 1000));
      stage.show();
      stage.setMaximized(true);
    }catch (IOException e){
      e.printStackTrace();
    }
  }
  /**
   * open start page window
   * @param control the control layer
   * @author Martin Fredin
   */
  public static void openStartPage(Control control){
    try {
      FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("View/StartPage.fxml"));
      Pane pane = fxmlLoader.load();
      ((StartPageController) fxmlLoader.getController()).init(control);
      Stage stage = new Stage();
      stage.setTitle("Welcome");
      stage.setScene(new Scene(pane));
      stage.setResizable(false);
      stage.show();

    }catch (IOException e){
      e.printStackTrace();
    }
  }
  /**
   * open add-account window
   * @param control the control layer
   * @author Martin Fredin
   */
  public static void openAddAccount(Control control, Consumer<Node> onClose) {

    try {
      FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("View/AddAccount.fxml"));
      Pane pane = fxmlLoader.load();
      ((AddAccountController) fxmlLoader.getController()).init(control, onClose);
      Stage stage = new Stage();
      stage.setTitle("Add Account");
      stage.setScene(new Scene(pane));
      stage.setResizable(false);
      stage.show();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
  /**
   * open writing window
   * @param control the model
   * @author Martin Fredin
   */
  public static void openWriting(Control control) {
    try {
      FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("View/Writing.fxml"));
      Pane pane = fxmlLoader.load();
      ((WritingController) fxmlLoader.getController()).init(control);
      Stage stage = new Stage();
      stage.setTitle("New MeMail");
      stage.setScene(new Scene(pane));
      stage.setResizable(false);
      stage.show();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
  /**
   * open reply window
   * @param control the control layer
   * @param to the email address which the user is replying to
   * @author Martin Fredin
   */
  public static void openReply(Control control, String to) {
    try {
      FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("View/Writing.fxml"));
      Pane pane = fxmlLoader.load();
      ((WritingController) fxmlLoader.getController()).init(control, to);
      Stage stage = new Stage();
      stage.setTitle("Reply");
      stage.setScene(new Scene(pane));
      stage.setResizable(false);
      stage.show();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  /**
   * open reply window
   * @param draft the unfinished email
   * @author Martin Fredin
   */
  public static void openDraft(Control control, Email draft) {
    try {
      FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("View/Writing.fxml"));
      Pane pane = fxmlLoader.load();
      ((WritingController) fxmlLoader.getController()).init(control, draft);
      Stage stage = new Stage();
      stage.setTitle("Draft");
      stage.setScene(new Scene(pane));
      stage.setResizable(false);
      stage.show();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
