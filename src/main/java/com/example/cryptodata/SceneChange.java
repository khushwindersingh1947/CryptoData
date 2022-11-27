package com.example.cryptodata;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class SceneChange {

        public static void changeScene(ActionEvent event, String fxmlFileName) throws IOException {
            FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource(fxmlFileName));
            Scene scene = new Scene(fxmlLoader.load());

            Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
            stage.setTitle("Coin Details");
            stage.setScene(scene);
            stage.show();
        }
}

