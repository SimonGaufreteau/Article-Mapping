package parser_utils;

import org.jsoup.*;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;

public class FromHTML {

    public static Elements queryFromHTMl(String url,String cssQuery) throws IOException {
        Document doc = Jsoup.connect(url).get();
        return queryFromHTML(doc,cssQuery);
    }
    public static Elements queryFromHTML(Document doc,String cssQuery){
        return doc.select(cssQuery);
    }

}
