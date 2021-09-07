package org.group77.mejl.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import org.group77.mejl.Main;

import java.io.IOException;

public class AddEmailErrorController {

    @FXML
    private static Button button;

    @FXML
    private Text text;

    private static Stage stage = new Stage();

    @FXML
    public static void Error() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("AddEmailError.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 200, 150);
        stage.setTitle("Error");
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    public void onPressClose(){
        stage.close();
    }


}
