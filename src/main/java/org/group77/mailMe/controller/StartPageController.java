package org.group77.mailMe.controller;

import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import org.group77.mailMe.control.ApplicationManager;

import java.util.List;

public class StartPageController {

    @FXML
    private Label welcomeLabel;

    @FXML
    private VBox accountListVBox;

    /**
     *
     *
     * @param appManager
     */

    public void init(ApplicationManager appManager) {

        // First add buttons for every stored accounts with actionEvent listeners
        initStoredAccounts(appManager);

        // Lastly add an account button
        Button addNewAccountButton = new Button("Add new account");
        accountListVBox.getChildren().add(addNewAccountButton);

        // Structure buttons in VBox
        accountListVBox.setAlignment(Pos.TOP_CENTER);

    }

    private void initStoredAccounts(ApplicationManager appManager) {

        List<String> emailAddresses = appManager.getEmailAddresses();

        for (String emailAddress : emailAddresses) {
            Button accountButton = new Button(emailAddress);
            accountListVBox.getChildren().add(accountButton);

            accountButton.setOnAction(actionEvent ->  {
                String activeAccount = accountButton.getText();
                appManager.setActiveAccount(activeAccount);
                System.out.println("Active account chosen: " + activeAccount);
                System.out.println("Active account in AM: " + appManager.getActiveAccount().getEmailAddress());
            });

        }
    }



}