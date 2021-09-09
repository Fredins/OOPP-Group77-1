package org.group77.mejl;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.group77.mejl.model.EmailBuilder;

import java.io.IOException;

public class Main extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("MainView.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 900, 700);
        stage.setTitle("mejl");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) throws Exception{
        EmailBuilder.sendEmail("iiredqueen@gmail.com"); // For testing purposes.
        launch();
    }
}