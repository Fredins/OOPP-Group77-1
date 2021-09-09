package org.group77.mejl.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import org.group77.mejl.Main;
import org.group77.mejl.model.*;

import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import java.io.IOException;
import java.util.Collection;
import java.util.function.Function;

public class MainController {
    private final EmailApp emailApp = new EmailApp();
    @FXML
    private TreeView<ImapsFolder> folderTree;
    // IN DEVELOPMENT
    @FXML
    private void initialize(){
        Function<TreeNode<ImapsFolder>, ImapsFolder> dataFunction = c -> c.getT();
        Function<TreeNode<ImapsFolder>, Collection<? extends TreeNode<ImapsFolder>>> childFunction = c -> c.getChildren();

        try{
            TreeNode<ImapsFolder> node = emailApp.getFolderTree(new AccountInformation(
                    "gmail",
                    "imap.gmail.com",
                    993,
                    "imaps",
                    "77grupp@gmail.com",
                    "grupp77group"
            ));
            TreeItemRecursive<TreeNode<ImapsFolder>, ImapsFolder> root = new TreeItemRecursive<>(node, dataFunction, childFunction);
            root.setExpanded(true);
            folderTree.setRoot(root);
        }catch(MessagingException e){
            e.printStackTrace();
        }

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