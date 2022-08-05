package com.loginform.loginform;

import TelegramBot.GestioneCommenti;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;

import java.io.File;
import java.io.FileNotFoundException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.Scanner;

public class ControlloCommenti implements Initializable {

    GestioneCommenti g = new GestioneCommenti();

    @FXML
    private ListView<String> commentsBox;

    @FXML
    private Button deleteComment;

    private ArrayList<String> comments =new ArrayList<String>();;

    @Override
    public String toString() {
        return "ControlloCommenti{" +
                "comments=" + comments +
                '}';
    }

    public ControlloCommenti (){

        Scanner scan=null;
        try{
            scan=new Scanner(new File("commenti.txt"));
            scan.nextLine();
            String[] tokens = scan.nextLine().split("#");
            String tit = tokens[1];
            String comm = tokens[3];
             comments.add(tit + " //commento" + comm);

        while(scan.hasNextLine()) {
            String riga = scan.nextLine();
            if (riga.startsWith("titolo notizia")) {

                tokens = riga.split("#");
                tit = tokens[1];
                comm = tokens[3];
                System.out.println(tokens[1]);
                System.out.println(tokens[3]);

                comments.add(tit + " //commento" + comm);

            } else if (riga.startsWith("-")){

                tokens = riga.split("#");
                tit = tokens[1];
                comm = tokens[3];
                //String[] tokens1 = riga.substring(2).split("#");
                //String tit2 = tokens1[1];
                //String comm2 = tokens1[3];
                System.out.println(tokens[1]);
                System.out.println(tokens[3]);

                comments.remove(tit + " //commento" + comm);
            }
        }
    }catch(FileNotFoundException e){
        e.printStackTrace();
    }
        scan.close();
}

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        commentsBox.getItems().addAll(comments);
    }

    @FXML
    public void deleteComment(javafx.scene.input.MouseEvent mouseEvent) {
        String selectedID = commentsBox.getSelectionModel().getSelectedItem();
        commentsBox.getItems().remove(selectedID);
        String [] tokens =selectedID.split(" //commento");
        tokens[0] = tokens[0].substring(1);
        g.rimuoviCommento(tokens[0].trim(), tokens[1].trim());
        comments.remove(selectedID);

    }
}
