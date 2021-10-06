package org.group77.mailMe.controller;

import javafx.event.ActionEvent;
import javafx.fxml.*;
import javafx.geometry.*;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import org.group77.mailMe.Main;
import org.group77.mailMe.controller.utils.*;
import org.group77.mailMe.model.*;
import org.group77.mailMe.model.data.Account;
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

  /**
   *
   * Initializes and populates StartPage view depending on the state of model.
   *
   * @param model holds the application state
   *
   * @author Elin Hagman
   */

  public void init(Model model) {

    initStoredAccounts(model);

    // onClose is a close function for AddAccountView
    Consumer<Node> onClose = node -> {
      ((Stage) node.getScene().getWindow()).close();
      WindowOpener.openMaster(model);
    };
    addAccountButton.setOnAction(actionEvent -> openAddAccount(
                                      actionEvent,
                                      model,
                                      onClose));

  }

  /**
   *
   * Creates a AccountListItemController for each account in model's accounts and displays them in this VBox.
   * Also adds listeners to each AccountListItemController that
   *    1) sets model's active account to the one user pressed on
   *    2) closes StartPage and opens Master
   *
   *
   * @param model holds the application state
   *
   * @author Elin Hagman
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

        // add OnMouseClicked to accountPane TODO: move to AccountListItemController??
        accountPane.setOnMouseClicked(inputEvent -> {
          model.activeAccount.set(account);
          ((Stage)((Node) inputEvent.getSource()).getScene().getWindow()).close();
          WindowOpener.openMaster(model);
        });

      } catch (Exception e) {
        e.printStackTrace();
        // AccountListItem.fxml file cannot be find or any of the javafx components is wrong
      }

    }
  }

  /**
   * Closes Stage that this StartPageController is displayed on
   * and opens and calls openAddAccount() in WindowOpener to open Add Account View.
   *
   * @param actionEvent occurs when user clicks on this addAccountButton
   * @param model holds the application state
   * @param onClose describes how closing Add Account View should be handled
   *
   * @author Elin Hagman
   */

  private void openAddAccount(ActionEvent actionEvent, Model model, Consumer<Node> onClose) {
    ((Stage)((Node) actionEvent.getSource()).getScene().getWindow()).close();
    WindowOpener.openAddAccount(model,onClose);

  }


}