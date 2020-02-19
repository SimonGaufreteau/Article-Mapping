package parser_utils;

// A class parsing an article and generating the number of occurences of a word in a text


import java.io.*;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class ParserClass {

    private ParserClass(){}

    /*
    *
    * Returns the number of occurences of all words with n or more letters.
    * Note : the article must follow a correct syntax given by the "splitting" param. This param will divide the article around these Strings.
     */
    public static Map<String,Integer> getOccurences(String article,int n,String regex){
        HashMap<String,Integer> hashMap = new HashMap<>();
        String[] splittedArticle = article.split(regex);

        for (String word:splittedArticle) {
            word =word.toLowerCase();
            if(word.length()>n) {
                //If the word already is in the hashmap, update it
                if (hashMap.containsKey(word)) {
                    hashMap.put(word, hashMap.get(word) + 1);
                } else {
                    hashMap.put(word, 1);
                }
            }
        }
        return hashMap;
    }

    /*
    * Same method but getting a filepath instead. (path for project root)
    *
     */
    public static Map<String,Integer> getOccurencesFromFile(String filepath,int n,String regex) throws IOException {
        File file = new File(filepath);
        BufferedReader br = new BufferedReader(new FileReader(file));

        StringBuilder s = new StringBuilder();
        String line = br.readLine();
        while(line!=null){
            s.append(line);
            line = br.readLine();
        }

        return getOccurences(s.toString(),n,regex);
    }

    public static Map<String, Integer> sortByValue(final Map<String, Integer> wordCounts) {
        return wordCounts.entrySet()
                .stream()
                .sorted((Map.Entry.<String, Integer>comparingByValue()).reversed())
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new));
    }

    /*
    * Returns a ready-to-print String of the k-top values of the article given in a file (input : filePath).
    *
     */
    private static String getBestOccurences(Map<String,Integer> map,int k){
        int i=0;
        StringBuilder s = new StringBuilder();
        s.append(String.format("Best %d words :\n",k));

        Map<String, Integer> sortedmap = ParserClass.sortByValue(map);
        for (String key:sortedmap.keySet()) {
            if(i>=k)break;
            i++;
            s.append(String.format("The word : \"%s\" appeared %d times.\n",key,sortedmap.get(key)));
        }

        return s.toString();
    }

    public static String getBestOccurencesFromFile(String filepath,int n,String regex,int k) throws IOException {
        return String.format("Article given : %s\n%s",filepath,getBestOccurences(getOccurencesFromFile(filepath, n, regex),k));
    }
}
