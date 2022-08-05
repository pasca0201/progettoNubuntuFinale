package TelegramBot;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

public class GestioneCommenti {

    private HashMap<String,String> commenti;//titolo commento

    public GestioneCommenti(){
        commenti=new HashMap<String,String>();
        Scanner scan=null;
        try{
            scan=new Scanner(new File("commenti.txt"));
            scan.nextLine();
            //prima riga sarà sempre da inserire
            String[] tokens = scan.nextLine().split("#");
            String tit = tokens[1];
            String comm = tokens[3];
            commenti.put(tit, comm);

            while(scan.hasNextLine()){

                String riga=scan.nextLine();

                if(riga.substring(0,14).equals("titolo notizia")) {

                    tokens = riga.split("#");
                    tit = tokens[1];
                    comm = tokens[3];
                    String commPrec="";

                    if(commenti.containsKey(tokens[1])){
                        //valori associati titolo
                        commPrec=commenti.get(tokens[1]);
                        //System.out.println(commPrec);
                        comm=comm+" "+commPrec;
                        commenti.put(tit, comm);
                    }else{
                        commenti.put(tit, comm);
                    }
                }else{
                    tokens = riga.split("#");
                    tit = tokens[1];
                    comm = tokens[3];
                    String commPrec="";
                    if(commenti.containsKey(tokens[1])){
                        //valori associati titolo
                        commPrec=commenti.get(tokens[1]);
                        //System.out.println(commPrec);
                        comm=commPrec;
                        commenti.remove(tit, comm);
                        //rimpiazzo il commento rimosso con "" nella stringa concatenazione di commenti
                        comm=comm.replaceAll(tokens[3],"");

                        //System.out.println("replace"+comm.replaceAll(tokens[3]," "));

                        //metto il nuovo commento come valore della chiave
                        commenti.put(tit, comm);
                    }
                }
            }

        }catch(FileNotFoundException e){
            e.printStackTrace();
        }
        scan.close();
    }

    //metto titolo senza spazi
    public void aggiungiCommento(String titolo,String commento) {
        String commPrec="";
        if (commenti.containsKey(" "+titolo)) {
            System.out.println("ENTRATI");
            //valori associati titolo
            commPrec = commenti.get(" "+titolo);
            //System.out.println("PREC "+commPrec);
            commenti.remove(" "+titolo, commPrec);


            //metto il nuovo commento come valore della chiave
            commenti.put(" "+titolo, commPrec+"  "+commento);
        }else{
            commenti.put(" "+titolo,commento);
        }
        PrintWriter p = null;
        try {
            FileWriter f = new FileWriter("commenti.txt", true);
            p = new PrintWriter(f);
            p.println("titolo notizia# "+ titolo + "# commento# " + commento);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        p.close();
    }

    public void rimuoviCommento(String titolo,String commento) {
        String commPrec="";
        if (commenti.containsKey(" "+titolo)) {
            //valori associati titolo
            commPrec = commenti.get(" "+titolo);
            //System.out.println(commPrec);
            commenti.remove(" "+titolo, commPrec);
            //rimpiazzo il commento rimosso con "" nella stringa concatenazione di commenti
            commPrec = commPrec.replaceAll(commento, "");


            //metto il nuovo commento come valore della chiave
            commenti.put(" "+titolo, commPrec);
        }
        PrintWriter p = null;
        try {
            FileWriter f = new FileWriter("commenti.txt", true);
            p = new PrintWriter(f);
            p.println("- " + "titolo notizia# " + titolo + "# commento# " + commento);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        p.close();
    }


    public HashMap<String, String> getCommenti() {
        return commenti;
    }

    public void setCommenti(HashMap<String, String> commenti) {
        this.commenti = commenti;
    }
}