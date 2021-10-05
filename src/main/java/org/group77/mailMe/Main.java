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
    Model model = new Model();
    if (model.accounts.isEmpty()) {
      openAddAccount(model);
    }else if(model.accounts.size() == 1){
      model.activeAccount.set(new Pair<>(true, model.accounts.get(0)));
      openMaster(model);
    }else{
      openStartPage(model);
    }
  }

  private void openMaster(Model model){
      // initialize StartPageView and its Controller
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

  private void openStartPage(Model model){
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

  void openAddAccount(Model m) {
    System.out.println("open account");
    try {
      FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("AddAccount.fxml"));
      Pane pane = fxmlLoader.load();
      ((AddAccountController) fxmlLoader.getController()).init(m);
      Stage stage = new Stage();
      stage.setTitle("Add Account");
      stage.setScene(new Scene(pane));
      stage.show();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public static void main(String[] args) {
    launch();
  }
}
