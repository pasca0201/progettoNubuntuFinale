package TelegramBot;


import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.*;
import java.util.*;

public class MyBot extends TelegramLongPollingBot {

    int i=0;
    int countLike=0;
    int countDislike=0;
    ArrayList<Notizia> news=new ArrayList<Notizia>();


    //SETTARE DA FILE QUELLI VECCHI!!!!?????
    HashMap<String,String> commenti=new HashMap<String,String>();//titolo notizia,commento

    HashMap<String,Integer> like=new HashMap<String,Integer>();//titolo,like

    HashMap<String,Integer> dislike=new HashMap<String,Integer>();//titolo,dislike
    String insiemeCommenti="";

    RSSreader reader=new RSSreader();

    List<List<InlineKeyboardButton>> rigaCollection=creaPulsanteNotizia();

    List<List<InlineKeyboardButton>> likeDislikeCommento=creaPulsanteLikeDislikeCommento();

    List<List<InlineKeyboardButton>> rigenera=rigeneraNotizie();

    List<List<InlineKeyboardButton>> registrati=pulsanteUtenti();

    UtentiRegistrati utenti=new UtentiRegistrati();

    public MyBot() throws IOException {
    }

    public void onUpdateReceived(Update update) {
        SendMessage message = new SendMessage();

        if (update.hasMessage() && update.getMessage().hasText()) {
            String mess = update.getMessage().getText().toString();
            User user=update.getMessage().getFrom();
            Integer id=update.getMessage().getMessageId();
            if (mess.equals("/start")) {
                i=0;
                InlineKeyboardMarkup inLineKey=new InlineKeyboardMarkup();
                inLineKey.setKeyboard(registrati);
                message.setReplyMarkup(inLineKey);
                message.setChatId(update.getMessage().getChatId().toString());
                message.setText("BENVENUTO!\nSU QUESTA PIATTAFORMA POTRAI CONSULTARE LE NOTIZIE RECENTI PER RIMANERE SEMPRE AGGIORNATO\nSEI REGISTRATO?");

                System.out.println("messId "+message.getChatId()+" user-username "+user.getUserName()+" username-name "+user.getFirstName()+" messaggio "+mess);
            }else if(mess.equals("sceltarapida")){
                //keyboard
                InlineKeyboardMarkup inLineKey=new InlineKeyboardMarkup();
                inLineKey.setKeyboard(rigaCollection);
                message.setReplyMarkup(inLineKey);
                message.setChatId(update.getMessage().getChatId().toString());
                message.setText("Ecco le notizie che puoi consultare");
                System.out.println("messId "+message.getChatId()+" user-username "+user.getUserName()+" username-name "+user.getFirstName()+" messaggio "+mess);

                //FORSE COSI SI FA IN DUE RIPARTENDO DA 0
                i=0;

            }else if(mess.substring(0,9).equals("commento:")){

                System.out.println(news.get(i).getTitolo());

                String comm=mess.substring(9);
                message.setChatId(update.getMessage().getChatId().toString());
                message.setText("il commento digitato e':"+comm);
                insiemeCommenti=insiemeCommenti+"  "+ comm;
                //se il titolo è presente, aggiunge commento alla stringa dei commenti precedenti
                if(commenti.containsKey(news.get(i).getTitolo().toString())) {
                    commenti.put(news.get(i).getTitolo().toString(), insiemeCommenti);
                }else{
                    //titolo non presente, stringa dei commenti diventa solo il nuovo commeto
                    insiemeCommenti=comm;
                    commenti.put(news.get(i).getTitolo().toString(), insiemeCommenti);
                }
                Notizia n=news.get(i);
                n.addComm(comm);
                if(news.get(i).getCommenti()!=null){
                    System.out.println("CARICATO");
                }
                System.out.println(news.get(i).getCommenti().toString());
                //NON SICURA
                System.out.println(commenti.keySet().toString()+ "   "+commenti.values().toString());
                System.out.println(n.getCommenti().toString());

                //AGGIUNGE COMMENTI SU FILE COMMENTI.TXT
                aggiungiInfoSuFileEsistente("commenti.txt","titolo notizia: "+news.get(i).getTitolo()+": commento: "+comm.trim());
            }else{
                message.setChatId(update.getMessage().getChatId().toString());
                message.setText("questo comando non è previsto");
                System.out.println("messId "+message.getChatId()+" user-username "+user.getUserName()+" username-name "+user.getFirstName()+" messaggio "+mess);
            }
            try {
                execute(message);
            } catch (TelegramApiException e) {
                e.printStackTrace();
            }
        }else if(update.hasCallbackQuery()) {

            String data = update.getCallbackQuery().getData();
            User user=update.getCallbackQuery().getFrom();
            Message m=update.getCallbackQuery().getMessage();
            if (data.equals("/cultura")) {
                news=reader.getNotizieCultura();
                callBackNews(update, reader, news, likeDislikeCommento,i,rigenera);
            } else if (data.equals("/politica")) {
                news=reader.getNotiziePolitica();
                callBackNews(update, reader, news, likeDislikeCommento,i,rigenera);
            } else if (data.equals("/turismo")) {
                news=reader.getNotizieTurismo();
                callBackNews(update, reader,news, likeDislikeCommento,i,rigenera);
            } else if (data.equals("/business")) {
                news = reader.getNotizieBusiness();
                callBackNews(update, reader, news, likeDislikeCommento, i, rigenera);
            }else if(data.equals("/categorieDifferenti")){
                news=reader.getNotizieDiversaCategoria();
                callBackNews(update, reader, news, likeDislikeCommento,i,rigenera);

                System.out.println(reader.getNotizieDiversaCategoria().get(i).getTitolo());

            }else if (data.equals("/like")) {
                Notizia n=news.get(i);
                if(!like.containsKey(news.get(i).getTitolo())){
                    countLike=1;
                    like.put(news.get(i).getTitolo(),countLike);
                }else{
                    countLike=countLike+1;
                    like.put(news.get(i).getTitolo(),countLike);
                }
                news.get(i).setLike(countLike);
                try {
                    message.setChatId(String.valueOf(update.getCallbackQuery().getMessage().getChatId()));
                    message.setText("like aggiunto correttamente alla notizia avente titolo "+news.get(i).getTitolo()+" numero di like "+news.get(i).getLike()+"\n");
                    execute(message);
                } catch (TelegramApiException e) {
                    e.printStackTrace();
                }
                System.out.println("numero like "+news.get(i).getLike());
                System.out.println(like.keySet().toString()+ "   "+like.values().toString());
            }else if(data.equals("/dislike")){
                Notizia n=news.get(i);

                System.out.println(news.get(i).getDislike()+"   "+i);

                if(!dislike.containsKey(news.get(i).getTitolo())){
                    countDislike=1;
                    dislike.put(news.get(i).getTitolo(),countDislike);
                }else{
                    countDislike=countDislike+1;
                    dislike.put(news.get(i).getTitolo(),countDislike);
                }
                news.get(i).setDislike(countDislike);
                try {
                    message.setChatId(String.valueOf(update.getCallbackQuery().getMessage().getChatId()));
                    message.setText("dislike aggiunto correttamente alla notizia avente titolo "+news.get(i).getTitolo()+" numero di dislike "+news.get(i).getDislike());
                    execute(message);
                } catch (TelegramApiException e) {
                    e.printStackTrace();
                }
                System.out.println("numero dislike "+news.get(i).getDislike());
                System.out.println(dislike.keySet().toString()+ "   "+dislike.values().toString());
            }else if(data.equals("/nuovaNotizia")) {
                i++;
                callBackNews(update,reader,news,likeDislikeCommento,i,rigenera);
            }else if(data.equals("/rigenera")){
                i=0;
                callBackNews(update,reader,news,likeDislikeCommento,i,rigenera);
            }else if(data.equals("/cambia")){
                i=0;
                InlineKeyboardMarkup inLineKey=new InlineKeyboardMarkup();
                inLineKey.setKeyboard(rigaCollection);
                message.setReplyMarkup(inLineKey);
                message.setText("Ecco le notizie che puoi consultare");
                try {
                    message.setChatId(String.valueOf(update.getCallbackQuery().getMessage().getChatId()));
                    execute(message);
                } catch (TelegramApiException e) {
                    e.printStackTrace();
                }
            }else if(data.equals("/commento")){
                //String mess = update.getMessage().getText().toString();
                try {
                    message.setChatId(String.valueOf(update.getCallbackQuery().getMessage().getChatId()));
                    message.setText("digita il tuo commento--> digita il messaggio preceduto da commento:");
                    execute(message);
                } catch (TelegramApiException e) {
                    e.printStackTrace();
                }
            }else if(data.equals("/si")){
                if(!registrato(update.getCallbackQuery().getFrom().getId())) {
                    message.setChatId(String.valueOf(update.getCallbackQuery().getMessage().getChatId()));
                    message.setText("hai digitato SI ma non risulti registrato, digita /start e clicca su NO per registrarti");
                }else{
                    message.setChatId(String.valueOf(update.getCallbackQuery().getMessage().getChatId()));
                    message.setText("digita sceltarapida");
                }

                try {
                    execute(message);
                } catch (TelegramApiException e) {
                    e.printStackTrace();
                }

            }else if(data.equals("/no")){
                if(!registrato(update.getCallbackQuery().getFrom().getId())) {
                    aggiungiInfoSuFileEsistente("utentiRegistrati.txt", update.getCallbackQuery().getFrom().getId().toString()+","+update.getCallbackQuery().getFrom().getUserName());

                    utenti.getUsers().add(update.getCallbackQuery().getFrom().getId());
                    utenti.getUsername().add(update.getCallbackQuery().getFrom().getUserName());

                    message.setChatId(String.valueOf(update.getCallbackQuery().getMessage().getChatId()));
                    message.setText("registrato correttamente digita sceltarapida");

                    //DEBUG VEDO SE E' AGGIUNTO CORRETTAMENTE
                    // System.out.println(utenti.getUsers().toString());
                }else{
                    message.setChatId(String.valueOf(update.getCallbackQuery().getMessage().getChatId()));
                    message.setText("risulti già registrato, digita sceltarapida");
                }
                try {
                    execute(message);
                } catch (TelegramApiException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    //FALLO DA FILE
    public boolean registrato(Long id){
        Scanner s=null;
        try{
            s=new Scanner(new File("utentiRegistrati.txt"));
            s.nextLine();
            while(s.hasNextLine()){
                String[] tokens=s.nextLine().substring(3).split(",");
                Long i = Long.parseLong(tokens[0]);
                if (i.equals(id)) {
                    System.out.println("uguali");
                    return true;
                }
            }

        }catch(FileNotFoundException e){
            System.out.println("file non trovato");
        }
        return false;
    }




    public void aggiungiInfoSuFileEsistente(String nomeFile,String daScrivere){
        FileWriter f=null;
        PrintWriter p=null;
        try{
            f=new FileWriter(nomeFile,true);
            p=new PrintWriter(f);
            p.println(daScrivere);

        }catch(FileNotFoundException e){
            System.out.println("file non trovato");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        p.close();
    }

    public void callBackNews(Update update, RSSreader reader, ArrayList<Notizia> news, List<List<InlineKeyboardButton>> likeDislikeCommento, int i, List<List<InlineKeyboardButton>> rigeneraNotizie) {
        SendMessage message=new SendMessage();
        message.setChatId(String.valueOf(update.getCallbackQuery().getMessage().getChatId()));
        InlineKeyboardMarkup inLineKey = new InlineKeyboardMarkup();
        inLineKey.setKeyboard(likeDislikeCommento);

        InlineKeyboardMarkup inLineKey1 = new InlineKeyboardMarkup();
        inLineKey1.setKeyboard(rigeneraNotizie);

        System.out.println(i);

        if(i<news.size()) {
            message.setReplyMarkup(inLineKey);
            message.setText(reader.toString(news.get(i)));
        }else{
            message.setReplyMarkup(inLineKey1);
            message.setText("notizie finite: rigenera le precedenti");
        }
        try {
            execute(message);

        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
        aggiungiInfoSuFileEsistente("ricordaNotizieGenerate.txt",message.getText().toString()+"\n");
    }

    public List<List<InlineKeyboardButton>> rigeneraNotizie(){
        InlineKeyboardButton InlineKeyboardButtonRigenera=new InlineKeyboardButton();
        InlineKeyboardButtonRigenera.setText("RIGENERA");
        InlineKeyboardButtonRigenera.setCallbackData("/rigenera");

        //bottone cambia categoria
        InlineKeyboardButton InlineKeyboardButtonCambiaCategoria=new InlineKeyboardButton();
        InlineKeyboardButtonCambiaCategoria.setText("CAMBIA CATEGORIA");
        InlineKeyboardButtonCambiaCategoria.setCallbackData("/cambia");

        //riga 1
        List<InlineKeyboardButton> riga1=new LinkedList<>();
        riga1.add(InlineKeyboardButtonRigenera);
        riga1.add(InlineKeyboardButtonCambiaCategoria);

        //riga collection
        List<List<InlineKeyboardButton>> rigaCollection=new LinkedList<>();
        rigaCollection.add(riga1);

        return rigaCollection;
    }

    public List<List<InlineKeyboardButton>> pulsanteUtenti(){
        InlineKeyboardButton InlineKeyboardButtonUtenteSi=new InlineKeyboardButton();
        InlineKeyboardButtonUtenteSi.setText("SI");
        InlineKeyboardButtonUtenteSi.setCallbackData("/si");

        InlineKeyboardButton InlineKeyboardButtonUtenteNo=new InlineKeyboardButton();
        InlineKeyboardButtonUtenteNo.setText("NO");
        InlineKeyboardButtonUtenteNo.setCallbackData("/no");

        //riga 1
        List<InlineKeyboardButton> riga1=new LinkedList<>();
        riga1.add(InlineKeyboardButtonUtenteSi);
        riga1.add(InlineKeyboardButtonUtenteNo);

        //riga collection
        List<List<InlineKeyboardButton>> rigaCollection=new LinkedList<>();
        rigaCollection.add(riga1);

        return rigaCollection;
    }


    public List<List<InlineKeyboardButton>> creaPulsanteNotizia(){
        //bottone cultura
        InlineKeyboardButton InlineKeyboardButtonCultura=new InlineKeyboardButton();
        InlineKeyboardButtonCultura.setText("CULTURA");
        InlineKeyboardButtonCultura.setCallbackData("/cultura");
        //bottone politica
        InlineKeyboardButton InlineKeyboardButtonPolitica=new InlineKeyboardButton();
        InlineKeyboardButtonPolitica.setText("POLITICA");
        InlineKeyboardButtonPolitica.setCallbackData("/politica");
        //bottone turismo
        InlineKeyboardButton InlineKeyboardButtonTurismo=new InlineKeyboardButton();
        InlineKeyboardButtonTurismo.setText("TURISMO");
        InlineKeyboardButtonTurismo.setCallbackData("/turismo");
        //bottone business
        InlineKeyboardButton InlineKeyboardButtonBusiness=new InlineKeyboardButton();
        InlineKeyboardButtonBusiness.setText("BUSINESS");
        InlineKeyboardButtonBusiness.setCallbackData("/business");

        //bottone categorie differenti
        InlineKeyboardButton InlineKeyboardButtonCategorieDifferenti=new InlineKeyboardButton();
        InlineKeyboardButtonCategorieDifferenti.setText("CATEGORIE DIFFERENTI");
        InlineKeyboardButtonCategorieDifferenti.setCallbackData("/categorieDifferenti");

        //riga 1
        List<InlineKeyboardButton> riga1=new LinkedList<>();
        riga1.add(InlineKeyboardButtonCultura);
        riga1.add(InlineKeyboardButtonPolitica);
        //riga 2
        List<InlineKeyboardButton> riga2=new LinkedList<>();
        riga2.add(InlineKeyboardButtonTurismo);
        riga2.add(InlineKeyboardButtonBusiness);

        //riga 3
        List<InlineKeyboardButton> riga3=new LinkedList<>();
        riga3.add(InlineKeyboardButtonCategorieDifferenti);

        //riga collection
        List<List<InlineKeyboardButton>> rigaCollection=new LinkedList<>();
        rigaCollection.add(riga1);
        rigaCollection.add(riga2);
        rigaCollection.add(riga3);
        return rigaCollection;
    }

    public List<List<InlineKeyboardButton>> creaPulsanteLikeDislikeCommento(){
        //bottone like
        InlineKeyboardButton InlineKeyboardButtonLike=new InlineKeyboardButton();
        InlineKeyboardButtonLike.setText("LIKE");
        InlineKeyboardButtonLike.setCallbackData("/like");

        //bottone dislike
        InlineKeyboardButton InlineKeyboardButtonDislike=new InlineKeyboardButton();
        InlineKeyboardButtonDislike.setText("DISLIKE");
        InlineKeyboardButtonDislike.setCallbackData("/dislike");

        //bottone commento
        InlineKeyboardButton InlineKeyboardButtonCommento=new InlineKeyboardButton();
        InlineKeyboardButtonCommento.setText("COMMENTO");
        InlineKeyboardButtonCommento.setCallbackData("/commento");

        //bottone genera nuova notizia
        InlineKeyboardButton InlineKeyboardButtonNuovaNotizia=new InlineKeyboardButton();
        InlineKeyboardButtonNuovaNotizia.setText("PROSSIMA NOTIZIA");
        InlineKeyboardButtonNuovaNotizia.setCallbackData("/nuovaNotizia");

        //bottone cambia categoria
        InlineKeyboardButton InlineKeyboardButtonCambiaCategoria=new InlineKeyboardButton();
        InlineKeyboardButtonCambiaCategoria.setText("CAMBIA CATEGORIA");
        InlineKeyboardButtonCambiaCategoria.setCallbackData("/cambia");

        //riga 1
        List<InlineKeyboardButton> riga1=new LinkedList<>();
        riga1.add(InlineKeyboardButtonLike);
        riga1.add(InlineKeyboardButtonDislike);

        //riga 2
        List<InlineKeyboardButton> riga2=new LinkedList<>();
        riga2.add(InlineKeyboardButtonCommento);
        riga2.add(InlineKeyboardButtonNuovaNotizia);

        //riga 3
        List<InlineKeyboardButton> riga3=new LinkedList<>();
        riga3.add(InlineKeyboardButtonCambiaCategoria);

        //riga collection
        List<List<InlineKeyboardButton>> rigaCollection=new LinkedList<>();
        rigaCollection.add(riga1);
        rigaCollection.add(riga2);
        rigaCollection.add(riga3);
        return rigaCollection;
    }



    @Override
    public String getBotUsername() {
        return "Progetto Bot Telegram";
    }

    @Override
    public String getBotToken() {
        return "5497434269:AAHIxBfthPq0y3Z01YUnBoW2GXENi0-TSmc";
    }
}

