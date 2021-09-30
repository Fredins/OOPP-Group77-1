package org.group77.mailMe.oldControllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.FlowPane;
import javafx.stage.Stage;
import org.group77.mailMe.Main;
import org.group77.mailMe.oldModel.*;

import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.function.Function;

public class MainController {
    private final EmailApp emailApp = new EmailApp();
    @FXML
    private FlowPane flowPaneTrees;
    @FXML
    private FlowPane flowPane;
    // IN DEVELOPMENT

    @FXML
    private void loadEmailFolders() {
        Function<Tree<EmailFolder>, EmailFolder> funcValue = Tree::getT;
        Function<Tree<EmailFolder>, Collection<? extends Tree<EmailFolder>>> funcChildren = Tree::getChildren;
        try{
            List<Tree<EmailFolder>> trees = emailApp.getTrees();
            if(trees.size() == 0){
                return;
            }
            trees.forEach(tree -> {
                TreeItemRecursive<Tree<EmailFolder>, EmailFolder> root = new TreeItemRecursive<>(tree, funcValue, funcChildren);
                root.setExpanded(true);

                TreeView<EmailFolder> treeView = new TreeView<>();
                // TODO maybe fix this width thing
                treeView.setMaxWidth(150.0);
                treeView.setRoot(root);
                treeView.setOnMouseClicked(e -> {
                    EmailFolder f = treeView.getSelectionModel().getSelectedItem().getValue();
                    try {
                        f.open(Folder.READ_WRITE);
                        Message[] messages = f.getMessages();
                        loadMails(messages);
                        f.close();
                    } catch (MessagingException | IOException ex) {
                        ex.printStackTrace();
                    }
                });
                flowPaneTrees.getChildren().add(treeView);
            });
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    @FXML
    private void initialize(){
            loadEmailFolders();
    }

    private void loadMails(Message[] messages) throws IOException, MessagingException {
        flowPane.getChildren().clear();
        for (Message m: messages) {
            FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("ListItemView.fxml"));
            flowPane.getChildren().add(fxmlLoader.load());
            EmailItemController controller = fxmlLoader.getController();
            controller.init(m);
        }


    }

    @FXML
    private void openEmailSettings() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("AddAccountView.fxml"));
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
