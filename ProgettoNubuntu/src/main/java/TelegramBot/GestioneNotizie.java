package TelegramBot;

import com.rometools.rome.feed.synd.SyndEntry;
import com.rometools.rome.feed.synd.SyndFeed;
import com.rometools.rome.io.FeedException;
import com.rometools.rome.io.SyndFeedInput;
import org.xml.sax.InputSource;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.*;

public class GestioneNotizie {

    private ArrayList<String> news; //contiene tutte i link delle notizie da fonte

    public GestioneNotizie(){
        news=new ArrayList<String>();
        Scanner s=null;
        try{
            s=new Scanner(new File("gestioneNotizie.txt"));
            s.nextLine();
            while(s.hasNextLine()){
                String riga=s.nextLine();
                if(riga.substring(0,17).equals("aggiuntaNotizia: ")){
                    news.add(riga.substring(17));
                }else{
                    news.remove(riga.substring(16));
                }
            }

        }catch(FileNotFoundException e){
            System.out.println("file non trovato");
        }
        s.close();
    }

    //NON SERVE
    public void aggiungiLink(String link){
        news.add(link);
        PrintWriter p=null;
        FileWriter f=null;
        try{
            f=new FileWriter("gestioneNotizie.txt",true);
            p=new PrintWriter(f);
            p.println("aggiuntaNotizia: "+link);
        }catch(FileNotFoundException e){
            System.out.println("file non trovato");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        p.close();
    }

    public void rimuoviLink(String link){
        news.remove(link);
        PrintWriter p=null;
        FileWriter f=null;
        try{
            f=new FileWriter("gestioneNotizie.txt",true);
            p=new PrintWriter(f);
            p.println("rimossaNotizia: "+link);
        }catch(FileNotFoundException e){
            System.out.println("file non trovato");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        p.close();
    }
    public ArrayList<String> getNews() {
        return news;
    }

    public void setNews(ArrayList<String> news) {
        this.news = news;
    }



    //SERVE?????
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

}