package TelegramBot;

import org.telegram.telegrambots.meta.api.objects.User;

import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

public class UtentiRegistrati {

    private ArrayList<Long> users=new ArrayList<Long>(); //salvo user.getId perchè id degli utenti sono tutti diversi

    private ArrayList<String> username=new ArrayList<String>();

    public UtentiRegistrati(){
        Scanner s=null;
        try{
            s=new Scanner(new File("utentiRegisatrati.txt"));
            s.nextLine();
            while(s.hasNextLine()){
                String riga=s.nextLine();
                //System.out.println(riga);
                if(riga.substring(0,3).equals("r: ")){
                    String[] tokens = riga.substring(3).split(",");
                    Long i = Long.parseLong(tokens[0]);
                    users.add(i);
                    String usern = tokens[1];
                    username.add(usern);
                }else{
                    String[] tokens = riga.substring(3).split(",");
                    Long i = Long.parseLong(tokens[0]);
                    users.remove(i);
                    String usern = tokens[1];
                    username.remove(usern);
                }
            }

        }catch(FileNotFoundException e){
            System.out.println("file non trovato");
        }

        //PER DEBUG
        System.out.println(users.toString());

        s.close();
    }

    public void aggiungi(String username,Long userId) {
        this.username.add(username);
        this.users.add(userId);
        PrintWriter p = null;
        try {
            FileWriter f = new FileWriter("utentiRegistrati.txt", true);
            p = new PrintWriter(f);
            p.println("r: " + userId + "," + username);
        } catch (FileNotFoundException e) {
            System.out.println("file non trovato");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        p.close();
    }

    public void rimuovi(String username,Long userId) {
        this.username.remove(username);
        this.users.remove(userId);
        PrintWriter p = null;
        try {
            FileWriter f = new FileWriter("utentiRegistrati.txt", true);
            p = new PrintWriter(f);
            p.println("n: " + userId + "," + username);
        } catch (FileNotFoundException e) {
            System.out.println("file non trovato");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        p.close();
    }



    public ArrayList<String> getUsername() {
        return username;
    }

    public void setUsername(ArrayList<String> username) {
        this.username = username;
    }

    public ArrayList<Long> getUsers() {
        return users;
    }

    public void setUsers(ArrayList<Long> users) {
        this.users = users;
    }
}