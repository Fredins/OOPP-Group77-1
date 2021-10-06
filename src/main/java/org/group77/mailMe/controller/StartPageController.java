package org.group77.mailMe.controller;

import javafx.event.ActionEvent;
import javafx.fxml.*;
import javafx.geometry.*;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.util.Pair;
import org.group77.mailMe.Main;
import org.group77.mailMe.controller.utils.*;
import org.group77.mailMe.model.*;
import org.group77.mailMe.model.data.Account;

import java.io.IOException;
import java.util.function.Consumer;


public class StartPageController {

  @FXML
  private BorderPane startPageBorderPane;

  @FXML
  private Label welcomeLabel;

  @FXML
  private Button addAccountButton;

  @FXML
  private FlowPane accountsFlowPane;

  @FXML
  private VBox accountsVbox;

  public void init(Model model) {


    // Add buttons for every stored accounts with actionEvent listeners
    initStoredAccounts(model);
    addAccountButton.setOnAction(actionEvent -> openAddAccount(
                                      actionEvent,
                                      model,
                                      node -> ((Stage) node.getScene().getWindow()).close()));

  }

  /**
   *
   * Creates a label for each account in model's accounts and displays them in this VBox.
   * Also adds listeners to each label that
   *    1) sets model's active account to the one user pressed on
   *    2) closes StartPage and opens Master
   *
   *
   * @param model that holds the application state
   */

  private void initStoredAccounts(Model model) {
    for (Account account : model.accounts) {

      try {

        // load AccountListItem.fxml
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("AccountListItem.fxml"));
        Pane accountPane = fxmlLoader.load();
        ((AccountListItemController) fxmlLoader.getController()).init(account);

        // add it to VBox
        accountsVbox.getChildren().add(accountPane);

        // add Listener to it TODO: move to AccountListItem.fxml??
        accountPane.setOnMouseClicked(inputEvent -> {
          model.activeAccount.set(account);
          ((Stage)((Node) inputEvent.getSource()).getScene().getWindow()).close();
          WindowOpener.openMaster(model);
        });


      } catch (Exception e) {
        e.printStackTrace();
      }

      // TODO: ev fit labels to dimensions
      // add OnMouseClicked listeners to each email address label


    }
  }

  private void openAddAccount(ActionEvent actionEvent, Model model, Consumer<Node> onClose) {
    ((Stage)((Node) actionEvent.getSource()).getScene().getWindow()).close();
    WindowOpener.openMaster(model);
    WindowOpener.openAddAccount(model,onClose);
    // TODO: add account doesnt open a new StartPage.. so i do it here instead, maybe there is a better solution?
  }


}