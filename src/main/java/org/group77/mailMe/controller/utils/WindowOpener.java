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

  /*
   * open master window
   * @param model the model
   * @author Martin
   */
  public static void openMaster(Control control){
    try {
      FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("Master.fxml"));
      Pane pane = fxmlLoader.load();
      ((MasterController) fxmlLoader.getController()).init(control);
      Stage stage = new Stage();
      stage.setTitle("MailMe");
      stage.setScene(new Scene(pane));
      stage.setMinHeight(700);
      stage.setMinWidth(1050);
      stage.show();
    }catch (IOException e){
      e.printStackTrace();
    }
  }
  /**
   * open start page window
   * @param model the model
   * @author Martin
   */
  public static void openStartPage(Control control){
    try {
      FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("StartPage.fxml"));
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
   * @param model the model
   * @author Martin
   */
  public static void openAddAccount(Control control, Consumer<Node> onClose) {

    try {
      FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("AddAccount.fxml"));
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
   * @param model the model
   * @author Martin
   */
  public static void openWriting(Control control) {
    try {
      FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("Writing.fxml"));
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
   * @param model the model
   * @param to the email address which the user is replying to
   * @author Martin
   */
  public static void openReply(Control control, String to) {
    try {
      FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("Writing.fxml"));
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

  /** Call this function when clicking on the filter button in MainView to open the filterView
   *
   * @param model
   * @author David Zamanian
   */

  public static void openFilter(Model model){
    try {
      FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("FilterView.fxml"));
      Pane pane = fxmlLoader.load();
      ((FilterController) fxmlLoader.getController()).init(model);
      Stage stage = new Stage();
      stage.setTitle("Filter");
      stage.setScene(new Scene(pane));
      stage.setResizable(false);
      stage.show();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

}
