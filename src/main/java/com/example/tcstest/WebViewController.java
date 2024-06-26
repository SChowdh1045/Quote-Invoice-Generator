package com.example.tcstest;

import com.example.tcstest.errors.ErrorModal;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.concurrent.Worker;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Stage;
import lombok.Setter;
import org.springframework.boot.SpringApplication;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class WebViewController implements Initializable {
    public static Stage webViewStage; // Adds a reference to the stage
    public static Stage originalStage;

    @FXML
    private WebView webView; // refers to the webView in scene builder

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        WebEngine engine = webView.getEngine();
        engine.load("http://localhost:8080/");
    }

    // This static method is called in 'OAuth2Controller.java'.
    // After the user signs in, the Google login stage will close and the original login stage will switch to the PDF generator thing stage
    public static void handleCloseGoogle(boolean emailExists){
        Platform.runLater(() -> {
            if (webViewStage != null) {
                webViewStage.close();
            }

            if(emailExists){
                Parent root;
                try {
                    root = FXMLLoader.load(WebViewController.class.getResource("/fxml/dashboard.fxml"));
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }

                originalStage.setTitle("Dashboard");
                originalStage.setScene(new Scene(root));
                originalStage.setResizable(false);
                originalStage.show();
            } else{
                ErrorModal.showErrorModal("User with this email does not exist");
            }
        });
    }

}
