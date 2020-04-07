import parser_utils.FromHTML;
import parser_utils.ParserClass;

import java.io.IOException;
import java.util.*;

public class Main {


    public static void main(String[] args) throws IOException {
        String regex = "[\\Q ,\n.:/-+()%$^'’\"&!?;\\E]";
        //Pattern p =  Pattern.compile(regex);
        int n=3;
        int k=10;
        /*String result = ParserClass.getBestOccurencesFromFile("Articles/insee-chomage.txt",n,regex,k);
        System.out.println(result);

        String bestpairs = ParserClass.getBestgetConcurrentPairsFromFile("Articles/insee-chomage.txt",n,regex,k);
        System.out.println(bestpairs);

        n=3;
        String psg = ParserClass.getBestOccurencesFromFile("Articles/psg-dortmund.txt",n,regex,k);
        System.out.println(psg);


        String bestpairsPSG = ParserClass.getBestgetConcurrentPairsFromFile("Articles/psg-dortmund.txt",n,regex,k);
        System.out.println(bestpairsPSG);*/
        /*Map<String,ArrayList<String>> thesaurus = ParserClass.getThesaurus();
        ParserClass.displayMap(thesaurus);*/
        /*Map<String,Integer> occBasic = ParserClass.getOccurencesFromFile("Articles/insee-chomage.txt",n,regex);
        Map<String,Integer> occThes = ParserClass.getOccurenciesSynonyms("Articles/insee-chomage.txt",regex,n);
        System.out.println(ParserClass.getBestOccurences(occThes,k));

        System.out.println("Number of occurencies with the basic method : ");
        System.out.println(ParserClass.getNumberOfOccurences(occBasic));
        System.out.println("For "+occBasic.size()+" words in the map.");
        System.out.println("Number of occurencies with the synonym method : ");
        System.out.println(ParserClass.getNumberOfOccurences(occThes));
        System.out.println("For "+occThes.size()+" words in the map.");*/

        // Example with an article from an URL.
        String url = "https://en.wikipedia.org/wiki/Clare_Stevenson";
        String cssQuery = "p,h1,h2,h3";
        String document= FromHTML.stringFromHTML(url,cssQuery);
        Map<String, Integer> map = ParserClass.getOccurrences(document,n,regex);
        System.out.println(ParserClass.getBestOccurrences(map,k));
    }
}

