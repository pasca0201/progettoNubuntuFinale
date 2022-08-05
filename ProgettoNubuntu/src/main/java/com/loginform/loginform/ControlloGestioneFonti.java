
package com.loginform.loginform;

import TelegramBot.GestioneFonti;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;

import java.net.URL;
import java.util.ResourceBundle;

public class ControlloGestioneFonti implements Initializable {

    GestioneFonti g = new GestioneFonti();

    @FXML
    private ListView<String> raccoltaFonti;

    @FXML
    private TextField fonte;

    @FXML
    void addName(MouseEvent event) {
        raccoltaFonti.getItems().add(fonte.getText());

    }

    @FXML
    void removeName(MouseEvent event) {
        String selectedID = raccoltaFonti.getSelectionModel().getSelectedItem();
        raccoltaFonti.getItems().remove(selectedID);
        g.remove(selectedID);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        raccoltaFonti.getItems().addAll(g.getFonti());
    }
}
