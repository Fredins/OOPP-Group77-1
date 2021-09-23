package org.group77.mejl.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.group77.mejl.Main;
import org.group77.mejl.model.ApplicationManager;

import java.io.IOException;

public class MainController {

        ApplicationManager appManager;
        
        @FXML
        public void refresh(){};

        @FXML
        public void switchAccount(){};

        @FXML
        public void openAddAccountView(){};

        @FXML
        public void loadEmails(){};

        @FXML
        private void loadFolders(){};

        @FXML
        public void openWritingView(){};

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
