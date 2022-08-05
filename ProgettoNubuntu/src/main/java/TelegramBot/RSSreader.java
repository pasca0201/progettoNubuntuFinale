package TelegramBot;
import com.rometools.rome.feed.synd.SyndEntry;
import com.rometools.rome.feed.synd.SyndFeed;
import com.rometools.rome.io.FeedException;
import com.rometools.rome.io.SyndFeedInput;
import org.xml.sax.InputSource;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.*;

public class RSSreader {

    private ArrayList<Notizia> notizieCultura;

    private ArrayList<Notizia> notizieBusiness;


    private ArrayList<Notizia> notiziePolitica;

    private ArrayList<Notizia> notizieTurismo;

    private ArrayList<Notizia> notizieDiversaCategoria=new ArrayList<Notizia>();

//private HashMap<String,ArrayList<Notizia>> news; //hashmap: chiave sourceURL, valore arraylist notizie

    public RSSreader() throws IOException {//inizializza 4 arraylist e li mette nell'HashMap
        notizieCultura = popolaNotizie("https://www.ilsole24ore.com/rss/cultura.xml");
        notizieBusiness = popolaNotizie("https://www.ilsole24ore.com/rss/finanza--business.xml");
        notiziePolitica = popolaNotizie("https://www.ilsole24ore.com/rss/italia--politica.xml");
        notizieTurismo = popolaNotizie("https://www.ilsole24ore.com/rss/economia--turismo.xml");

        //AGGIUNGO A NOTIZIEDIVERSACATEGORIA LE NOTIZIE DELLE FONTI PRESENTI SUL FILE DI TESTO
        /*Scanner s=null;
        try{
            s=new Scanner(new File("fonti.txt"));
            while(s.hasNextLine()){
                String str=s.nextLine();
                if(str.substring(0,9).equals("rimossa: ")){
                    notizieDiversaCategoria.removeAll(popolaNotizie(str.substring(9)));
                }else if(str.substring(0,10).equals("aggiunta: ")){
                    notizieDiversaCategoria.addAll(popolaNotizie(str.substring(10)));
                }
            }
        }catch(FileNotFoundException e){
            e.printStackTrace();
        }
        s.close();*/

        //uso gestionefonti per popolare,sopra ho usato file
        GestioneFonti gf=new GestioneFonti();
        System.out.println("FONTI" +gf.getFonti().toString());
        //salto fonti di default partendo da contatore 4
        for(int i=4;i<gf.getFonti().size();i++){
            System.out.println("singola "+gf.getFonti().get(i));
            notizieDiversaCategoria.addAll(popolaNotizie(gf.getFonti().get(i)));
        }

    }

   /*public void aggiungiNews(String sourceURL){
        if(!news.containsKey(sourceURL)) {
            news.put(sourceURL, popolaNotizie(sourceURL));
        }
    }
    //notizie di cultura/politica/turismo/business non possono essere rimosse;
    public void rimuoviNews(String sourceURL){
        if(!sourceURL.equals("https://www.ilsole24ore.com/rss/finanza--business.xml") && !sourceURL.equals("https://www.ilsole24ore.com/rss/mondo.xml") && !sourceURL.equals("https://www.ilsole24ore.com/rss/cultura.xml") && !sourceURL.equals("https://www.ilsole24ore.com/rss/economia--turismo.xml")) {
            news.remove(sourceURL, popolaNotizie(sourceURL));
        }
    }*/

    public ArrayList<Notizia> popolaNotizie(String sourceURL){

        ArrayList<Notizia> news=new ArrayList<Notizia>();
        try {
            URL feedUrl = new URL(sourceURL);

            SyndFeedInput input = new SyndFeedInput();

            try {
                SyndFeed feed = input.build(new InputSource(feedUrl.openStream()));

                List<SyndEntry> entries = feed.getEntries();

                Iterator<SyndEntry> itEntries = entries.iterator();


                while (itEntries.hasNext()) {
                    SyndEntry entry = itEntries.next();
                    Notizia notizia=new Notizia(entry.getTitle(),entry.getPublishedDate(),entry.getDescription().getValue(),entry.getAuthor(), sourceURL,entry.getLink(),0,0,new ArrayList<String>());
                    if(entry.getAuthor().toString().equals("")){
                        notizia.setAutore("autore non specificato");
                    }else if(entry.getDescription().getValue().toString().equals("")){
                        notizia.setDescrizione("descrizione assente");
                    }

                    news.add(notizia);

                    Scanner scan=null;

                    //CONTROLLO SU GESTIONENOTIZIE SE è STATA RIMOSSA NOTIZIA, IN QUEL CASO LA RIMUOVO DA ARRAYLIST
                    try{
                        scan=new Scanner(new File("gestioneNotizie.txt"));
                        while(scan.hasNextLine()){
                            String riga=scan.nextLine();
                            if(riga.length()>16 && riga.substring(0,16).equals("rimossaNotizia: ")){
                                for(int i=0;i<news.size();i++){
                                    if(news.get(i).getLink().equals(riga.substring(16))){
                                        news.remove(news.get(i));
                                    }
                                }
                            }
                        }
                    }catch(FileNotFoundException e){

                    }
                    scan.close();

                }

            } catch (IllegalArgumentException | FeedException | IOException e) {
                // Errore lettura feed
                e.printStackTrace();
            }

        } catch (MalformedURLException e) {
            // Errore indirizzo e accesso ai feed
            e.printStackTrace();
        }
        return news;
    }

    //toString ArrayList di notizie
    public String toString(ArrayList<Notizia> news){
        String s="";
        for(int i=0;i<news.size();i++){
            s=s+" TITOLO: "+news.get(i).getTitolo()+"\n"+" DATA: "+news.get(i).getTimeStamp()+"\n"+" DESCRIZIONE: "+news.get(i).getDescrizione()+"\n"+" AUTORE: "+news.get(i).getAutore()+"\n"+" FONTE: "+news.get(i).getFonte()+"\n"+" LINK: "+news.get(i).getLink()+"\n";
        }
        return s;
    }

    //toString notizia singola
    public String toString(Notizia news){
        String s="";
        s="TITOLO: "+news.getTitolo()+"\n"+" DATA: "+news.getTimeStamp()+"\n"+" DESCRIZIONE: "+news.getDescrizione()+"\n"+" AUTORE: "+news.getAutore()+"\n"+" FONTE: "+news.getFonte()+"\n"+" LINK: "+news.getLink()+"\n";
        return s;
    }

    /*public HashMap<String, ArrayList<Notizia>> getNews() {
        return news;
    }
    public void setNews(HashMap<String, ArrayList<Notizia>> news) {
        this.news = news;
    }*/

    public ArrayList<Notizia> getNotiziePolitica() {
        return notiziePolitica;
    }

    public void setNotiziePolitica(ArrayList<Notizia> notiziePolitica) {
        this.notiziePolitica = notiziePolitica;
    }

    public ArrayList<Notizia> getNotizieCultura() {
        return notizieCultura;
    }

    public void setNotizieCultura(ArrayList<Notizia> notizieCultura) {
        this.notizieCultura = notizieCultura;
    }
    public ArrayList<Notizia> getNotizieDiversaCategoria() {
        return notizieDiversaCategoria;
    }

    public void setNotizieDiversaCategoria(ArrayList<Notizia> notizieDiversaCategoria) {
        this.notizieDiversaCategoria = notizieDiversaCategoria;
    }

    public ArrayList<Notizia> getNotizieTurismo() {
        return notizieTurismo;
    }

    public void setNotizieTurismo(ArrayList<Notizia> notizieTurismo) {
        this.notizieTurismo = notizieTurismo;
    }

    public ArrayList<Notizia> getNotizieBusiness() {
        return notizieBusiness;
    }

    public void setNotizieBusiness(ArrayList<Notizia> notizieBusiness) {
        this.notizieBusiness = notizieBusiness;
    }
}