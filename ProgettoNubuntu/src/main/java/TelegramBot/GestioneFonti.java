package TelegramBot;

import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

    public class GestioneFonti {
        private ArrayList<String> fonti;

        //costruttore in cui viene popolato arraylist da file
        //se c'è scritto default viene sempre aggiunta
        //aggiungi viene aggiunta
        //rimossa viene rimossa se presente
        public GestioneFonti() {
            fonti=new ArrayList<String>();
            Scanner s=null;
            try{
                s=new Scanner(new File("fonti.txt"));
                while(s.hasNextLine()){
                    String str=s.nextLine();
                    if(str.substring(0,9).equals("default: ")){
                        fonti.add(str.substring(9));
                    }else if(str.substring(0,9).equals("rimossa: ")){
                        fonti.remove(str.substring(9));

                    }else if(str.substring(0,10).equals("aggiunta: ")){
                        fonti.add(str.substring(10));
                    }
                }
            }catch(FileNotFoundException e){
                e.printStackTrace();
            }
            s.close();
        }
        public GestioneFonti(ArrayList<String> fonti) {
            this.fonti = fonti;
        }

        //se viene aggiunta fonte, fonte caricata su file
        public void add(String fonte){
            fonti.add(fonte);
            FileWriter f=null;
            PrintWriter p=null;
            try{
                f=new FileWriter("fonti.txt",true);
                p=new PrintWriter(f);
                p.println("aggiunta: "+fonte);

            }catch(FileNotFoundException e){
                System.out.println("file non trovato");
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            p.close();
        }

        public void remove(String fonte){
            fonti.remove(fonte);
            FileWriter f=null;
            PrintWriter p=null;
            try{
                f=new FileWriter("fonti.txt",true);
                p=new PrintWriter(f);
                p.println("rimossa: "+fonte);

            }catch(FileNotFoundException e){
                System.out.println("file non trovato");
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            p.close();
        }
        public ArrayList<String> getFonti() {
            return fonti;
        }

        public void setFonti(ArrayList<String> fonti) {
            this.fonti = fonti;
        }


    }