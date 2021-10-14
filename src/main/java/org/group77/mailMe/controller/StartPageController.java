package org.group77.mailMe.controller;

import javafx.event.ActionEvent;
import javafx.fxml.*;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import org.group77.mailMe.Main;
import org.group77.mailMe.controller.utils.*;
import org.group77.mailMe.model.ActiveAccountNotInAccounts;
import org.group77.mailMe.model.Control;
import org.group77.mailMe.model.data.Account;

import java.io.IOException;
import java.util.function.Consumer;


public class StartPageController {
  @FXML private BorderPane startPageBorderPane;
  @FXML private Label welcomeLabel;
  @FXML private Button addAccountButton;
  @FXML private FlowPane accountsFlowPane;
  @FXML private VBox accountsVbox;

  /**
   * Initializes and populates StartPage view depending on the state of model.
   *
   * Displays all accounts in model's accounts-attribute and handles onMouseClick on them
   * that sets the active account to the one that is clicked on by user.
   * Adds action handler to this addAccountButton.
   *
   * @param control holds the application state
   * @author Elin Hagman
   */
  public void init(Control control) {
    initStoredAccounts(control);
    addAccountButton.setOnAction(actionEvent -> openAddAccount(
                                      actionEvent,
                                      control));
  }

  /**
   * Responsible for displaying the accounts in model's accounts-attribute.
   *
   * Creates a AccountListItemController for each account in model's accounts and displays them in this VBox.
   * Also adds onMouseClicked handlers to each AccountListItemController that:
   *    1) sets model's active account to the one user pressed on
   *    2) closes StartPage and opens MasterView
   * @param control holds the application state
   * @author Elin Hagman
   */

  private void initStoredAccounts(Control control) {
    for (Account account : control.getAccounts().get()) {
      try {
        // load AccountListItem.fxml
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("AccountListItem.fxml"));
        Pane accountPane = fxmlLoader.load();
        ((AccountListItemController) fxmlLoader.getController()).init(account);
        // add it to VBox
        accountsVbox.getChildren().add(accountPane);
        // add OnMouseClicked to accountPane TODO: move to AccountListItemController??
        accountPane.setOnMouseClicked(inputEvent -> {
          ((Stage)((Node) inputEvent.getSource()).getScene().getWindow()).close();
          WindowOpener.openMaster(control);
          try {
            control.setActiveAccount(account);
          } catch (ActiveAccountNotInAccounts e) {
            e.printStackTrace(); // TODO: give error message to user
          }
          // control.setActiveAccount(account)
        });
      } catch (IOException e) {
        e.printStackTrace();
        // AccountListItem.fxml file cannot be find or any of the javafx components is wrong
      }
    }
  }

  /**
   * Closes Stage that this StartPageController is displayed on,
   * calls openAddAccount() in WindowOpener to open Add Account View.
   * @param actionEvent occurs when user clicks on this addAccountButton
   * @param control holds the application state
   * @author Elin Hagman
   */
  private void openAddAccount(ActionEvent actionEvent, Control control) {
    ((Stage)((Node) actionEvent.getSource()).getScene().getWindow()).close();
    // onClose is a function for AddAccountView that determines how closing the view should be handled
    Consumer<Node> onClose = node -> {
      ((Stage) node.getScene().getWindow()).close();
      WindowOpener.openMaster(control);
    };
    WindowOpener.openAddAccount(control,onClose);

  }
}