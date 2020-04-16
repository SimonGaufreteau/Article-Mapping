package vue;

import parser_utils.FromHTML;
import parser_utils.Pair;
import parser_utils.ParserClass;
import vue.help.ArgValidator;

import java.io.IOException;
import java.util.*;

public class Main {
    private final static String regex = "[\\Q ,\n.:/-+()%$^'’\"&!?;\\E]";
    private static final int n=3;
    private static final int k=10;
    private static final String cssQuery = "p,h1,h2,h3";

    public static void testsExamples() throws IOException {
        //Examples with a file :
        String filepath = "Articles/insee-chomage.txt";
        fileResults(filepath);

        // Example with an article from an URL.
        String url = "https://en.wikipedia.org/wiki/Clare_Stevenson";
        urlResults(url);
    }

    public static void main(String[] args) throws Exception {
        Pair<String, String> pair = ArgValidator.validateArgs(args);
        if (pair != null) {
            switch (pair.getKey()) {
                case "url":
                    urlResults(pair.getValue());
                    break;
                case "file":
                    fileResults(pair.getValue());
                    break;
                case "example":
                    System.out.println("Displaying the sample tests :");
                    testsExamples();
                    break;
            }
        }
    }


    private static void urlResults(String url) throws IOException {
        // Example with an article from an URL.
        String document= FromHTML.stringFromHTML(url,cssQuery);
        Map<String, Integer> map = ParserClass.getOccurrencesSynonyms(document, n, regex);
        System.out.println(ParserClass.getBestOccurrences(map,k));
        Map<Pair<String, String>, Integer> pairMap = ParserClass.getConcurrentPairs(document, n, regex);
        System.out.println(ParserClass.getBestConcurrentPairs(pairMap,k));
    }

    private static void fileResults(String filepath) throws IOException {
        Map<String,Integer> map = ParserClass.getOccurrencesFromFile(filepath,n,regex);
        System.out.println("--- With the synonyms disabled ---");
        System.out.println(ParserClass.getBestOccurrences(map,k));

        /* A method using the file, in general slower than the second method.

        Map<String,Integer> synonymMap = ParserClass.getOccurrencesSynonymsFromFile(filepath,regex,n);
        System.out.println(ParserClass.getBestOccurrences(synonymMap,k));*/

        System.out.println("--- With the synonyms enabled ---");
        Map<String,Integer> synonymMapV2 = ParserClass.getOccurrencesSynonyms(map);
        System.out.println(ParserClass.getBestOccurrences(synonymMapV2,k));

        Map<Pair<String, String>, Integer> pairMap = ParserClass.getConcurrentPairsFromFile(filepath, n, regex);
        System.out.println(ParserClass.getBestConcurrentPairs(pairMap,k));
    }
}

