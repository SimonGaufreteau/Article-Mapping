import parser_utils.Pair;
import parser_utils.ParserClass;

import java.io.IOException;
import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class Main {


    public static void main(String[] args) throws IOException {
        String regex = "[\\Q ,\n.:/-+()%$^'’\"&!?;\\E]";
        //Pattern p =  Pattern.compile(regex);
        int n=3;
        int k=10;
        String result = ParserClass.getBestOccurencesFromFile("Articles/insee-chomage.txt",n,regex,k);
        System.out.println(result);

        String bestpairs = ParserClass.getBestgetConcurrentPairsFromFile("Articles/insee-chomage.txt",n,regex,k);
        System.out.println(bestpairs);

        n=3;
        String psg = ParserClass.getBestOccurencesFromFile("Articles/psg-dortmund.txt",n,regex,k);
        System.out.println(psg);


        String bestpairsPSG = ParserClass.getBestgetConcurrentPairsFromFile("Articles/psg-dortmund.txt",n,regex,k);
        System.out.println(bestpairsPSG);

    }
}

