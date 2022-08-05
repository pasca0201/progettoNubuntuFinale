package com.loginform.loginform;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;


public class Controller {

    // Strings which hold css elements to easily re-use in the application
    protected
    String successMessage = String.format("-fx-text-fill: GREEN;");
    String errorMessage = String.format("-fx-text-fill: RED;");
    String errorStyle = String.format("-fx-border-color: RED; -fx-border-width: 2; -fx-border-radius: 5;");
    String successStyle = String.format("-fx-border-color: #A9A9A9; -fx-border-width: 2; -fx-border-radius: 5;");

    // Import the application's controls
    @FXML
    private Label invalidLoginCredentials;
    @FXML
    private Button LoginButton;
    @FXML
    private Button cancelButton;
    @FXML
    private TextField loginUsernameTextField;
    @FXML
    private TextField loginPasswordPasswordField;
    @FXML
    private Button gestioneFontiButton;
    @FXML
    private Button rimuoviNotizieButton;
    @FXML
    private Button rimuoviCommentiButton;
    @FXML
    private Button gestioneProfiliButton;


    // PRIMA PAGINA - GESTONE PULSANTI ACCESSO ADMIN
    @FXML
    protected void onCancelButtonClick() {
        Stage stage = (Stage) cancelButton.getScene().getWindow();
        stage.close();
    }

    @FXML
    protected void onLoginButtonClick() {

        if (loginUsernameTextField.getText().isBlank() || loginPasswordPasswordField.getText().isBlank()) {
            invalidLoginCredentials.setText("The Login fields are required!");
            invalidLoginCredentials.setStyle(errorMessage);

            if (loginUsernameTextField.getText().isBlank()) {
                loginUsernameTextField.setStyle(errorStyle);
            } else if (loginPasswordPasswordField.getText().isBlank()) {
                loginPasswordPasswordField.setStyle(errorStyle);
            }
        } else if (loginUsernameTextField.getText().equals("lucacarlotta@unibo.it")
                && loginPasswordPasswordField.getText().equals("1234")) {
            invalidLoginCredentials.setText("Login Successful!");
            invalidLoginCredentials.setStyle(successMessage);
            loginUsernameTextField.setStyle(successStyle);
            loginPasswordPasswordField.setStyle(successStyle);
            LoginButton.setOnMouseClicked((event) -> {
                        FXMLLoader fxmlLoader2 = new FXMLLoader();
                        fxmlLoader2.setLocation(getClass().getResource("seconda-pagina.fxml"));
                        Scene scene;
                        try {
                            scene = new Scene(fxmlLoader2.load(), 600, 400);
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                        Stage stage = new Stage();
                        stage.setTitle("Comandi admin");
                        stage.setScene(scene);
                        stage.show();
                    }
            );

        } else {
            invalidLoginCredentials.setText("Wrong credentials");
            invalidLoginCredentials.setStyle(errorMessage);

        }
    }

    //SECONDA POAGINA - GESTIONE PULSANTI PER AZIONI
    @FXML
    protected void gestioneFontiButton() throws IOException{

        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("paginaGestioneFonti.fxml"));
        Scene fontiScene = new Scene(loader.load(), 500,500);
        Stage stage = new Stage();
        stage.setScene(fontiScene);
        stage.setTitle("Gestione Fonti");
        stage.show();

    }

    @FXML
    protected void rimuoviNotizieButton(ActionEvent event) throws IOException{

        Stage stage = (Stage) rimuoviNotizieButton.getScene().getWindow();
        stage.close();
        Stage primary = new Stage();
        Parent loader =  FXMLLoader.load(getClass().getResource("paginaRImozioneNotizie.fxml"));
        primary.setScene(new Scene(loader, 500, 500));
        primary.setTitle("Gestione Notizie");
        primary.show();

    }

    @FXML
    protected void rimuoviCommentiButton(ActionEvent event) throws IOException{

        FXMLLoader loader = new FXMLLoader(getClass().getResource("paginaGestioneCommenti.fxml"));
        Parent paginaGestioneCommenti = loader.load();
        Scene commentiScene = new Scene(paginaGestioneCommenti, 500,500);
        Stage stage = (Stage) rimuoviCommentiButton.getScene().getWindow();
        stage.setScene(commentiScene);
        stage.setTitle("Gestione Commenti");
        stage.show();

    }

    @FXML
    protected void gestioneProfiliButton(ActionEvent event) throws IOException{

        FXMLLoader loader = new FXMLLoader(getClass().getResource("paginaGestioneProfili.fxml"));
        Parent paginaGestioneProfili = loader.load();
        Scene profiliScene = new Scene(paginaGestioneProfili, 500,500);
        Stage stage = new Stage();
        stage.setScene(profiliScene);
        stage.setTitle("Gestione Profili");
        stage.show();

    }

    //

}

