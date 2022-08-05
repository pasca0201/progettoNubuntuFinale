package com.loginform.loginform;

import TelegramBot.GestioneNotizie;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseEvent;

import java.net.URL;
import java.util.ResourceBundle;

public class ControlloRimozioneNotizie implements Initializable {

    GestioneNotizie g = new GestioneNotizie();

    @FXML
    private ListView<String> raccoltaNotizie;

    @FXML
    private Button removeNews;

    @FXML
    public void removeNews(MouseEvent mouseEvent) {
        String selectedID = raccoltaNotizie.getSelectionModel().getSelectedItem();
        raccoltaNotizie.getItems().remove(selectedID);
        g.rimuoviLink(selectedID);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        raccoltaNotizie.getItems().addAll(g.getNews());
    }
}
