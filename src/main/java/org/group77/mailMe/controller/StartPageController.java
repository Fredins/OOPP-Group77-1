package org.group77.mailMe.controller;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.group77.mailMe.Main;
import org.group77.mailMe.control.ApplicationManager;

import java.io.IOException;
import java.util.List;

public class StartPageController {

    @FXML
    private Label welcomeLabel;

    @FXML
    private BorderPane startPageBorderPane;

    @FXML
    private Button addAccountButton;

    @FXML
    private ListView<Label> accountsListView = new ListView<Label>();

    /**
     *
     *
     * @param appManager
     */

    public void init(ApplicationManager appManager) {

        startPageBorderPane.setCenter(accountsListView);
        accountsListView.setPrefSize(startPageBorderPane.getPrefWidth()-20,startPageBorderPane.getPrefHeight()-20);
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

    public Button getAddAccountButton() {
        return this.addAccountButton;
    }


}