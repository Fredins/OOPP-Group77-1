package org.group77.mejl.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import org.group77.mejl.model.Model;

import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import java.util.Arrays;

public class HelloController {
    @FXML
    private TreeView<Folder> folderTree;

    private final Model model = new Model();


    // IN DEVELOPMENT
    @FXML
    private void initialize(){
        try{
            Folder[] folders = model.getFolders();
            TreeItem<Folder> root = new TreeItem<>(folders[1]);
            folderTree.setRoot(root);
            TreeItem<Folder> f2 = new TreeItem<>(folders[2]);
            root.getChildren().add(f2);
            TreeItem<Folder> f3 = new TreeItem<>(folders[3]);
            root.getChildren().add(f3);
            TreeItem<Folder> f4 = new TreeItem<>(folders[4]);
            root.getChildren().add(f4);
            TreeItem<Folder> f5 = new TreeItem<>(folders[5]);
            root.getChildren().add(f5);
        }catch (MessagingException e){
            e.printStackTrace();
        }

        ContextMenu contextMenu = new ContextMenu();
        MenuItem m1 = new MenuItem("do something");
        MenuItem m2 = new MenuItem("do something else");
        contextMenu.getItems().add(m1);
        contextMenu.getItems().add(m2);
        folderTree.setContextMenu(contextMenu);

    }

    // IN DEVELOPMENT
    @FXML
    private void fetchMails() {
        Model model = new Model();
        try{
            Folder[] folders = model.getFolders();
            System.out.println(Arrays.asList(folders));
            Message[] messages = model.getMessages(folders[0]);
            Arrays.asList(messages).forEach(m -> {
                try {
                    System.out.printf("from: %s\nsubject: %s\n\n", m.getFrom()[0], m.getSubject());
                } catch (MessagingException e) {
                    e.printStackTrace();
                }
            });
        }catch (MessagingException e){
            e.printStackTrace();
        }

    }
}