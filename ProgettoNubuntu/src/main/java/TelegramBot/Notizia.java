package TelegramBot;

import java.util.Date;
import java.util.ArrayList;
public class Notizia {
    //help
    private String titolo;
    private Date timeStamp;
    private String descrizione;
    private String autore;
    private String fonte;
    private String link;

    //CREA ARRAYLIST di like 0 dislike, 1 like
    private int like;
    private int dislike;
    private ArrayList<String> commenti;

    public Notizia(String titolo, Date timeStamp, String descrizione, String autore, String fonte, String link,int  like,int dislike,ArrayList<String> commenti) {
        this.titolo = titolo;
        this.timeStamp = timeStamp;
        this.descrizione = descrizione;
        this.autore = autore;
        this.fonte = fonte;
        this.link = link;
        this.like=like;
        this.dislike=dislike;
        this.commenti=commenti;
    }

    public boolean addComm(String comm){
        if(commenti.add(comm)){
            return true;
        }else{
            return false;
        }
    }

    public String toStringCommenti(){
        String s="commenti relativi a notizia con titolo "+titolo+" sono: \n";
        String s1="";
        for(int i=0;i<commenti.size();i++){
            s1=s1+commenti.get(i).toString()+"\n";
        }
        return s+s1;
    }
    public String getAutore() {
        return autore;
    }

    public void setAutore(String autore) {
        this.autore = autore;
    }

    public void setFonte(String fonte) {
        this.fonte = fonte;
    }


    public String getFonte() {
        return fonte;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getDescrizione() {
        return descrizione;
    }

    public void setDescrizione(String descrizione) {
        this.descrizione = descrizione;
    }

    public Date getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(Date timeStamp) {
        this.timeStamp = timeStamp;
    }

    public String getTitolo() {
        return titolo;
    }

    public void setTitolo(String titolo) {
        this.titolo = titolo;
    }


    public ArrayList<String> getCommenti() {
        return commenti;
    }

    public void setCommenti(ArrayList<String> commenti) {
        this.commenti = commenti;
    }

    public int getDislike() {
        return dislike;
    }

    public void setDislike(int dislike) {
        this.dislike = dislike;
    }

    public int getLike() {
        return like;
    }

    public void setLike(int like) {
        this.like = like;
    }
}
