package com.loginform.loginform;

import TelegramBot.UtentiRegistrati;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class ControlloGestioneProfili implements Initializable {
    UtentiRegistrati g = new UtentiRegistrati();

    @FXML
    private ListView<String> profiliUtente;

    @FXML
    private Label label;

    @FXML
    private Button removeName;

    @FXML
    private TextField userBox;

    @FXML
    private TextField userBox2;

    @FXML
    private Button addUserBotton;

    private ArrayList<String> utenti = new ArrayList<>();

    @Override
    public String toString() {
        return  "utenti=" + utenti ;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        for(int i=0; i<g.getUsers().size(); i++) {
            profiliUtente.getItems().add(g.getUsers().get(i) + " " + g.getUsername().get(i));
        }
    }

    @FXML
    void addUserBotton(MouseEvent event) {
        profiliUtente.getItems().add(userBox2.getText() + " " + userBox2.getText());
        g.aggiungi(userBox2.getText(), Long.parseLong(userBox.getText()));
    }

    @FXML
    void removeName(MouseEvent event) {
        int selectedID = profiliUtente.getSelectionModel().getSelectedIndex();
        profiliUtente.getItems().remove(selectedID);
        g.rimuovi(userBox2.getText(), Long.parseLong(userBox.getText()));
    }

}

