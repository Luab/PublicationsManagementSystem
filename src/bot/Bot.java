package bot;

import bot.*;
import org.json.JSONArray;
import org.json.JSONObject;
import ru.pumas.DbHelper;
import ru.pumas.Publication;
import ru.pumas.PublicationSet;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

public class Bot {
    private static Boolean isOn = true;
    static URL requestUpdates() throws MalformedURLException {
        return requestUpdates(null);
    }

    static URL requestUpdates(Integer offset) throws MalformedURLException {
        String s = "https://api.telegram.org/bot"+ BuildVars.BOT_TOKEN+"/getUpdates";
        if (offset != null) {
            s += "?offset=" + offset;
        }
        return new URL(s);
    }

    /**
     * @param text
     * @param replyTo may be <code>null</code>
     * @param chatId
     */
    static void sendMessage(String text, Integer replyTo, int chatId) throws IOException {
        String s = "https://api.telegram.org/bot"+ BuildVars.BOT_TOKEN+"/sendMessage?";
        QueryString q = new QueryString();
        // chat_id=47289384&text=Test&reply_to_message_id=52
        q.add("chat_id", String.valueOf(chatId));
        q.add("text", text);
        if (replyTo != null) {
            q.add("reply_to_message_id", replyTo.toString());
        }
        URL url = new URL(s + q.getQuery());
        url.openStream();
        System.err.println(url);
    }
    static void sendMarkdownMessage(String text, Integer replyTo, int chatId) throws IOException {
        String s = "https://api.telegram.org/bot"+ BuildVars.BOT_TOKEN+"/sendMessage?";
        QueryString q = new QueryString();
        // chat_id=47289384&text=Test&reply_to_message_id=52
        q.add("chat_id", String.valueOf(chatId));
        q.add("text", text);
        q.add("parse_mode", "Markdown");
        if (replyTo != null) {
            q.add("reply_to_message_id", replyTo.toString());
        }
        URL url = new URL(s + q.getQuery());
        url.openStream();
        System.err.println(url);
    }

    static int lastUpdateId = 0;
    static List<Rule> rules = new ArrayList<>();

    static void makeRules() {

        rules.add(new Rule() {
            @Override
            public boolean check(String txt, Message msg) throws IOException, SQLException {
                if(txt.matches("^\\/search.*$")){
                    String request = txt.split("^/search")[1];
                    PublicationSet result = DbHelper.searchPublicationSet(request,0,10);
                    String respond="";
                    while(result.next()) {
                        Publication publication=result.getPublication();
                        respond+="["+publication.getTitle()+"]"+"("+publication.getLink()+")\n";
                    }
                    sendMarkdownMessage(respond,msg.getMessageId(),msg.getChat().getId());
                    return true;
                }
                return false;
            }
        });
    }
    public static void turnOff(){
        isOn=false;
    }
    /**
     * When an object implementing interface <code>Runnable</code> is used
     * to create a thread, starting the thread causes the object's
     * <code>run</code> method to be called in that separately executing
     * thread.
     * <p/>
     * The general contract of the method <code>run</code> is that it may
     * take any action whatsoever.
     *
     * @see Thread#run()
     */
    public static void run() {
        lastUpdateId = 0;
        isOn = true;
        makeRules();
        while(isOn) {
            try {


                URL url = requestUpdates(lastUpdateId);
                System.err.println(url);
                Scanner in = new Scanner(new InputStreamReader(url.openStream()));
                String json = "";
                sendMessage("I am working", 1, 47289384);
                while (in.hasNextLine()) {
                    json += in.nextLine();
                }
                System.err.println(json);
                JSONObject response = new JSONObject(json);
                JSONArray result = response.getJSONArray("result");
                for (int i = 0; i < result.length(); i++) {
                    JSONObject update = result.getJSONObject(i);
                    Update up = Update.getUpdate(update);
                    lastUpdateId = up.getUpdate_id() + 1;
                    Message msg = up.getMessage();
                    String txt = msg.getText().toLowerCase();
                    for (Rule rule : rules) {
                        Boolean x = rule.check(txt, msg);
                    }

                }
                Thread.sleep(10000);
            } catch (Throwable e) {
                try {
                    sendMessage("I am broken " + e.getMessage(), 1, 47289384);
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        }
    }
}

