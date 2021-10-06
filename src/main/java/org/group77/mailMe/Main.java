package org.group77.mailMe;

import javafx.application.*;
import javafx.scene.*;
import javafx.stage.*;
import javafx.util.Pair;
import org.group77.mailMe.controller.utils.*;
import org.group77.mailMe.model.*;
import org.group77.mailMe.services.storage.*;

import java.io.*;
import java.util.function.*;

public class Main extends Application {
  /**
   * opens different views depending on number of accounts
   */
  @Override
  public void start(Stage stage) throws IOException, OSNotFoundException {
    Model model = new Model();
    if (model.accounts.isEmpty()) {
      // consumer that closes active window and open master widow.
      Consumer<Node> onClose = node -> {
        ((Stage) node.getScene().getWindow()).close();
        WindowOpener.openMaster(model);
      };
      WindowOpener.openAddAccount(model, onClose);
    }else if(model.accounts.size() == 1){
      WindowOpener.openMaster(model);
      model.activeAccount.set(model.accounts.get(0));
    }else{
      WindowOpener.openStartPage(model);
    }
  }

  public static void main(String[] args) {
    launch();
  }
}
