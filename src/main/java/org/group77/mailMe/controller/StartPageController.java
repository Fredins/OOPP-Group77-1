package org.group77.mailMe.controller;

import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import org.group77.mailMe.control.ApplicationManager;

import java.util.List;

public class StartPageController {

    @FXML
    private Label welcomeLabel;

    @FXML
    private ListView<Label> accountsListView;

    /**
     *
     *
     * @param appManager
     */

    public void init(ApplicationManager appManager) {

        // Add buttons for every stored accounts with actionEvent listeners
        initStoredAccounts(appManager);


    }

    private void initStoredAccounts(ApplicationManager appManager) {

        List<String> emailAddresses = appManager.getEmailAddresses();

        for (String emailAddress : emailAddresses) {
            Label accountLabel = new Label(emailAddress);
            accountsListView.getItems().add(accountLabel);
            accountLabel.setPrefWidth(accountsListView.getPrefWidth());
            accountLabel.setAlignment(Pos.CENTER);

        }
    }



    public ListView<Label> getAccountsListView() {
        return this.accountsListView;
    }



}