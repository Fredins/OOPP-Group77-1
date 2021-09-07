package org.group77.mejl.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import org.group77.mejl.Main;
import org.group77.mejl.model.EmailApp;

import javax.mail.Folder;
import java.io.IOException;

public class MainController {
    private final EmailApp emailApp = new EmailApp();
    @FXML
    private TreeView<Folder> folderTree;
    // IN DEVELOPMENT
    @FXML
    private void initialize(){
        /*
        try{
            ContextMenu contextMenu = new ContextMenu();
            MenuItem m1 = new MenuItem("do something");
            MenuItem m2 = new MenuItem("do something else");
            contextMenu.getItems().add(m1);
            contextMenu.getItems().add(m2);
            folderTree.setContextMenu(contextMenu);
        }catch (NullPointerException | MessagingException e){
           e.printStackTrace();
        }
         */
    }

    @FXML
    private void openEmailSettings() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("AccountView.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 500, 400);
        Stage stage = new Stage();
        stage.setTitle("mejl");
        stage.setScene(scene);
        stage.show();
    }

    // IN DEVELOPMENT
    @FXML
    private void fetchMails() {
    }
}