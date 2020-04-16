package vue;

import parser_utils.FromHTML;
import parser_utils.Pair;
import parser_utils.ParserClass;
import vue.help.ArgValidator;

import java.io.IOException;
import java.util.*;

/**
 * Exec class for the command-line type display. To see a more advanced-level display with graphs, check {@link vue.graph.MainGraph}. Uses the {@link ArgValidator} class.
 * @author GAUFRETEAU Simon
 */
public class Main {
    private final static String regex = "[\\Q ,\n.:/-+()%$^'’\"&!?;\\E]";
    private static final int n=3;
    private static final int k=10;
    private static final String cssQuery = "p,h1,h2,h3";


    /**
     * Displays some samples. Sample files can be found in the "Articles" dir.
     */
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


    /**
     * Displays the results of {@link ParserClass} for the given URL. Throws and Exception if the url isn't correct or impossible to access. Check {@link FromHTML} class for more details.
     * @see ParserClass#getOccurrencesSynonyms(String, int, String)
     * @see ParserClass#getConcurrentPairs(String, int, String)
     */
    private static void urlResults(String url) throws IOException {
        // Example with an article from an URL.
        String document= FromHTML.stringFromHTML(url,cssQuery);
        Map<String, Integer> map = ParserClass.getOccurrencesSynonyms(document, n, regex);
        System.out.println(ParserClass.getBestOccurrences(map,k));
        Map<Pair<String, String>, Integer> pairMap = ParserClass.getConcurrentPairs(document, n, regex);
        System.out.println(ParserClass.getBestConcurrentPairs(pairMap,k));
    }

    /**
     * Displays the results of {@link ParserClass} for the given filepath. Absolute and relative file paths are accepted.
     * @see ParserClass
     * @see ParserClass#getOccurrencesFromFile(String, int, String)
     * @see ParserClass#getConcurrentPairsFromFile(String, int, String)
     */
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

