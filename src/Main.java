import parser_utils.ParserClass;

import java.io.IOException;
import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class Main {


    public static void main(String[] args) throws IOException {
        String regex = "[\\Q ,\n.:/-+()%$^'\"&!?;\\E]";
        //Pattern p =  Pattern.compile(regex);
        int n=3;
        int k=2000;
        String result = ParserClass.getBestOccurencesFromFile("Articles/insee-chomage.txt",n,regex,k);
        System.out.println(result);
    }
}

