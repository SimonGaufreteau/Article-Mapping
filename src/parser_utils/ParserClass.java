package parser_utils;

// A class parsing an article and generating the number of occurences of a word in a text


//import org.jetbrains.annotations.NotNull;

import java.io.*;
import java.lang.reflect.Array;
import java.util.*;
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



    /*
    * Returns a ready-to-print String of the k-top values of the article given in a file (input : filePath).
    *
     */
    public static String getBestOccurences(Map<String,Integer> map,int k){
        int i=0;
        SortingMap <String,Integer>sorting = new SortingMap<>();
        StringBuilder s = new StringBuilder();
        k=Math.min(map.size(),k);
        s.append(String.format("Best %d words :\n",k));
        Map<String, Integer> sortedmap = sorting.sortByValue(map);
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


    /*
    * Finds the most used pairs of words in every sentence of a file.
     */
    //@NotNull
    public static Map<Pair<String,String>,Integer> getConcurrentPairs(String filepath, int n, String regex) throws IOException {
        File f = new File(filepath);
        BufferedReader br = new BufferedReader(new FileReader(f));

        String line = br.readLine();

        Map<Pair<String,String>,Integer> map = new HashMap<>();
        while (line!=null){
            String[] sentenceSplit = line.split("[.]");
            for(String sentence : sentenceSplit){
                //For each sentence, iterate over all pairs of words and put them into the map or update it (if the pair was already found)
                String[] words = sentence.split(regex);
                for(int i=0;i<words.length-1;i++){
                    if(words[i].length()<=n) continue;
                    for (int j=i+1;j<words.length;j++){
                        //Checking if the pair is not a duplicate
                        if (words[j].length()<=n || words[i].equals(words[j])) continue;

                        //Checking if the pair is not already existing and in reversed form
                        Pair<String,String> pair = new Pair<>(words[i], words[j]);
                        Pair<String,String> reversedpair = new Pair<>(words[j], words[i]);
                        if(map.containsKey(pair)){
                            map.put(pair,map.get(pair)+1);
                        }
                        else if(map.containsKey(reversedpair)){
                            map.put(reversedpair,map.get(reversedpair)+1);
                        }
                        else {
                            map.put(pair,1);
                        }
                    }
                }
            }
            line=br.readLine();
        }
        return map;
    }


    //TODO : Generic method
    private static String getBestConcurrentPairs(Map<Pair<String,String>,Integer> map,String regex, int k){
        StringBuilder s = new StringBuilder("Best pairs :\n");

        SortingMap<Pair<String,String>,Integer> sorting = new SortingMap<>();
        Map<Pair<String, String>, Integer> sortedmap = sorting.sortByValue(map);
        int i=0;
        for (Pair<String,String> key:sortedmap.keySet()) {
            if(i>=k)break;
            i++;
            s.append(String.format("The pair : \"%s\",\"%s\" appeared %d times.\n",key.getKey(),key.getValue(),sortedmap.get(key)));
        }
        return s.toString();
    }

    public static String getBestgetConcurrentPairsFromFile(String pathFile, int n, String regex, int k) throws IOException {
        return getBestConcurrentPairs(getConcurrentPairs(pathFile,n, regex),regex,k);
    }


    /*
    Retourne le thesaurus correspondant au fichier thes_fr.dat dans le repertoire Thes.
    Format of a line :
    hanse|1
    (Nom)|guilde|corporation|association

    Note : 1 est le nombre de lignes suivant le mot. La taille d'une ligne est variable
    Output : Map<String,ArrayList<String>>
     */
    public static Map<String, ArrayList<String>> getThesaurus() throws IOException {
        Map<String,ArrayList<String>> map = new LinkedHashMap<>();
        File f = new File("Thes\\thes_fr.dat");
        BufferedReader br = new BufferedReader(new FileReader(f));

        br.readLine();
        String line=br.readLine();
        while (line!=null){
            String[] splitted = line.split("[|]");
            try{
                int nbLines = Integer.parseInt(splitted[1]);
                //Reading lines nbLines times.
                String key = splitted[0];
                ArrayList<String> arrayList = new ArrayList<>();
                for(int i=0;i<nbLines;i++){
                    line=br.readLine();
                    String[] splitLine = line.split("[|]");
                    for(int j=1;j<splitLine.length;j++){
                        arrayList.add(splitLine[j]);
                    }
                }
                map.put(key,arrayList);
            }
            //catch (NumberFormatException ignored){}
            catch(Exception e) {
                e.printStackTrace();
            }
            line=br.readLine();
        }
        return map;
    }

    public static Map<String,Integer> getOccurenciesSynonyms(String filepath,String regex,int n) throws IOException {
        File file = new File(filepath);
        BufferedReader br = new BufferedReader(new FileReader(file));

        Map<String,ArrayList<String>> thesaurus = getThesaurus();
        Map<String,Integer> map = new HashMap<>();
        String line = br.readLine();
        while(line!=null){
            String[] splitted = line.split(regex);
            for(String word:splitted){
                word =word.toLowerCase();
                if(word.length()>n) {
                    //If the word already is in the hashmap, update it
                    if (map.containsKey(word)) {
                        map.put(word,map.get(word) + 1);
                    } else {
                        boolean isSynonym=false;
                        for(String key:map.keySet()){
                            ArrayList<String> ar = thesaurus.get(key);
                            if(ar!=null && ar.contains(word)){
                                map.put(key,map.get(key)+1);
                                isSynonym=true;
                                break;
                            }
                        }
                        if(!isSynonym){
                            map.put(word, 1);
                        }
                    }
                }
            }
            line = br.readLine();
        }
        return map;
    }


    public static int getNumberOfOccurences(Map<String,Integer> map){
        int sum=0;
        for(String key:map.keySet())
            sum+=map.get(key);
        return sum;
    }

    public static void displayMap(Map<String,ArrayList<String>> map){
        for(String key:map.keySet()){
            System.out.println("Word : "+key);
            System.out.println(map.get(key));
        }
    }
}


