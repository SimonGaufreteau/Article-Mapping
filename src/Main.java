import parser_utils.FromHTML;
import parser_utils.Pair;
import parser_utils.ParserClass;

import java.io.IOException;
import java.util.*;

public class Main {


    public static void main(String[] args) throws IOException {
        String regex = "[\\Q ,\n.:/-+()%$^'’\"&!?;\\E]";
        int n=3;
        int k=10;

        //Examples with a file :
        String filepath = "Articles/insee-chomage.txt";
        Map<String,Integer> map = ParserClass.getOccurrencesFromFile(filepath,n,regex);
        System.out.println(ParserClass.getBestOccurrences(map,k));

        Map<String,Integer> synonymMap = ParserClass.getOccurrencesSynonymsFromFile(filepath,regex,n);
        System.out.println(ParserClass.getBestOccurrences(synonymMap,k));

        Map<String,Integer> synonymMapV2 = ParserClass.getOccurrencesSynonyms(map);
        System.out.println(ParserClass.getBestOccurrences(synonymMapV2,k));

        Map<Pair<String, String>, Integer> pairMap = ParserClass.getConcurrentPairsFromFile(filepath, n, regex);
        System.out.println(ParserClass.getBestConcurrentPairs(pairMap,k));

        // Example with an article from an URL.
        String url = "https://en.wikipedia.org/wiki/Clare_Stevenson";
        String cssQuery = "p,h1,h2,h3";
        String document= FromHTML.stringFromHTML(url,cssQuery);
        map = ParserClass.getOccurrences(document,n,regex);
        System.out.println(ParserClass.getBestOccurrences(map,k));

        pairMap = ParserClass.getConcurrentPairs(document,n,regex);
        System.out.println(ParserClass.getBestConcurrentPairs(pairMap,k));
    }
}

