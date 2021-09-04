package org.group77.mejl.controllers;

import javafx.beans.property.ObjectProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import org.group77.mejl.Main;
import org.group77.mejl.model.IdentifierAndFolder;
import org.group77.mejl.model.Model;

import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import java.io.IOException;
import java.util.Arrays;
import java.util.Properties;

public class HelloController {
    @FXML
    private final Model model = new Model();

    @FXML
    private TreeView<IdentifierAndFolder> folderTree;

    // IN DEVELOPMENT
    @FXML
    private void initialize(){
        try{
            IdentifierAndFolder[] identifierAndFolders = model.getIdentifierAndFolder();
            TreeItem<IdentifierAndFolder> root = new TreeItem<>(identifierAndFolders[0]);

            for (int i = 1; i < identifierAndFolders.length; i++) {
                root.getChildren().add(new TreeItem<>(identifierAndFolders[i]));
            }
            folderTree.setRoot(root);

            ContextMenu contextMenu = new ContextMenu();
            MenuItem m1 = new MenuItem("do something");
            MenuItem m2 = new MenuItem("do something else");
            contextMenu.getItems().add(m1);
            contextMenu.getItems().add(m2);
            folderTree.setContextMenu(contextMenu);
        }catch (NullPointerException | MessagingException ignored){
        }


    }

    @FXML
    private void openEmailSettings() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("EmailSettings.fxml"));
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