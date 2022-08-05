package com.loginform.loginform;
import TelegramBot.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Scanner;

public class Main {


    public static void main(String[] args) throws IOException {

        UtentiRegistrati gn=new UtentiRegistrati();

        //gn.aggiungiCommento("Papa Francesco riceve Musk: affrontati i temi della tecnologia e della natalità","BABABAB");
      //  gn.rimuoviCommento("Papa Francesco riceve Musk: affrontati i temi della tecnologia e della natalità"," bello");
        System.out.println(gn.getUsername());
        System.out.println(gn.getUsers());
        gn.rimuovi("2111225420", 53L);
        System.out.println(gn.getUsername());
        System.out.println(gn.getUsers());
    }


}