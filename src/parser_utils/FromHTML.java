package parser_utils;

import org.jsoup.*;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;

/**
 * A basic class using JSoup to gather text from a web page.
 * @author GAUFRETEAU Simon
 */
public class FromHTML {


    public static Elements queryFromHTML(String url,String cssQuery) throws IOException {
        System.out.println("Waiting to connect to the url... ("+url+")");
        long time = System.currentTimeMillis();
        Connection connect = Jsoup.connect(url);
        System.out.println("Connected to the url in "+(System.currentTimeMillis()-time)+" milliseconds.");
        Document doc = connect.get();
        return queryFromHTML(doc,cssQuery);
    }
    public static Elements queryFromHTML(Document doc,String cssQuery){
        return doc.select(cssQuery);
    }

    public static String stringFromHTML(String url,String cssQuery) throws IOException {
        Elements el = queryFromHTML(url,"p,h1,h2,h3");
       return el.text();
    }

}
