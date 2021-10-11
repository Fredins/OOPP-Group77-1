package org.group77.mailMe;

import javafx.application.*;
import javafx.scene.*;
import javafx.stage.*;
import org.group77.mailMe.controller.utils.*;
import org.group77.mailMe.model.*;
import org.group77.mailMe.services.storage.LocalDiscStorage;
import org.group77.mailMe.services.storage.Storage;

import java.util.function.*;

public class Main extends Application {
  /**
   * opens different views depending on number of accounts
   */
  @Override
  public void start(Stage stage) throws Exception {

    Storage storage = new LocalDiscStorage();
    Control control = new Control(storage);
    if (control.getAccounts().isEmpty()) {
      Consumer<Node> onClose = node -> {
        ((Stage) node.getScene().getWindow()).close();
        WindowOpener.openMaster(control);
      };
      WindowOpener.openAddAccount(control, onClose);
    } else if(control.getAccounts().size() == 1) {
      WindowOpener.openMaster(control);
      control.setActiveAccount(control.getAccounts().get(0));

    } else {
      WindowOpener.openStartPage(control);
    }

    /* Model model = new Model();
    if (model.accounts.isEmpty()) {
      // consumer that closes active window and open master widow.
      Consumer<Node> onClose = node -> {
        ((Stage) node.getScene().getWindow()).close();
        WindowOpener.openMaster(model);
      };
      WindowOpener.openAddAccount(model, onClose);
    }else if(model.accounts.get().size() == 1){
      WindowOpener.openMaster(model);
      model.activeAccount.set(model.accounts.get().get(0));
    }else{
      WindowOpener.openStartPage(model);
    }*/
  }

  public static void main(String[] args) {
    launch();
  }
}
