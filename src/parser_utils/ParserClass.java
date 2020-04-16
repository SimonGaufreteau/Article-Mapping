package parser_utils;

import java.io.*;
import java.util.*;


/**
 * Class used to get the trend of an article (most seen words and strongest pairs in sentences). Only static methods, cannot be instantiated.
 * @author GAUFRETEAU Simon
 */
public class ParserClass {

    private ParserClass(){}


    /**
     * Returns the number of occurrences of all words with n or more letters.
     * Note : the article must follow a correct syntax given by the "splitting" param. This param will divide the article around these Strings.
     * @see #getOccurrencesFromFile(String, int, String) (String, int, String)
     */
    public static Map<String,Integer> getOccurrences(String article, int n, String regex){
        if(article==null) return null;
        HashMap<String,Integer> hashMap = new HashMap<>();
        String[] splittedArticle = article.split(regex);

        for (String word:splittedArticle) {
            word =word.toLowerCase();
            if(word.length()>n) {
                //If the word already is in the HashMap, update it
                if (hashMap.containsKey(word)) {
                    hashMap.put(word, hashMap.get(word) + 1);
                } else {
                    hashMap.put(word, 1);
                }
            }
        }
        return hashMap;
    }

    /**
    * Same method but getting a filepath instead. (path for project root)
    * @see #getOccurrences(String, int, String)
     */
    public static Map<String,Integer> getOccurrencesFromFile(String filepath, int n, String regex) throws IOException {
        return getOccurrences(FileUtils.getFileContent(filepath),n,regex);
    }


    /**
    * Returns a ready-to-print String of the k-top values from the map 
    * @see #getOccurrences(String, int, String)
     */
    public static String getBestOccurrences(Map<String,Integer> map, int k){
        int i=0;
        SortingMap <String,Integer>sorting = new SortingMap<>();
        StringBuilder s = new StringBuilder();
        k=Math.min(map.size(),k);
        s.append(String.format("Best %d words :\n",k));
        Map<String, Integer> sortedMap = sorting.sortByValue(map);
        for (String key:sortedMap.keySet()) {
            if(i>=k)break;
            i++;
            s.append(String.format("The word : \"%s\" appeared %d times.\n",key,sortedMap.get(key)));
        }

        return s.toString();
    }

    // ----- PAIRS -----
    /**
    * Finds the most used pairs of words in every sentence of an article.
     */
    public static Map<Pair<String,String>,Integer> getConcurrentPairs(String article, int n, String regex) {
        String[] sentenceSplit = article.split("[.?!]");
        Map<Pair<String,String>,Integer> map = new HashMap<>();
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
                    Pair<String,String> reversed = new Pair<>(words[j], words[i]);
                    if(map.containsKey(pair)){
                        map.put(pair,map.get(pair)+1);
                    }
                    else if(map.containsKey(reversed)){
                        map.put(reversed,map.get(reversed)+1);
                    }
                    else {
                        map.put(pair,1);
                    }
                }
            }
        }
        return map;
    }

    /**
     * Same as {@link #getConcurrentPairs(String, int, String)} but with a filepath.
     * @see FileUtils#getFileContent(String)
     */
    public static Map<Pair<String,String>,Integer> getConcurrentPairsFromFile(String filepath, int n, String regex) throws IOException {
        return getConcurrentPairs(FileUtils.getFileContent(filepath),n,regex);
    }



    /**
     * Returns a ready-to-print String for the ConcurrentPairs
     * @param map Result of the {@link #getConcurrentPairs(String, int, String)} method
     * @param k Number of pairs displayed
     */
    public static String getBestConcurrentPairs(Map<Pair<String, String>, Integer> map, int k){
        StringBuilder s = new StringBuilder("Best pairs :\n");

        SortingMap<Pair<String,String>,Integer> sorting = new SortingMap<>();
        Map<Pair<String, String>, Integer> sortedMap = sorting.sortByValue(map);
        int i=0;
        for (Pair<String,String> key:sortedMap.keySet()) {
            if(i>=k)break;
            i++;
            s.append(String.format("The pair : \"%s\",\"%s\" appeared %d times.\n",key.getKey(),key.getValue(),sortedMap.get(key)));
        }
        return s.toString();
    }


    // ----- THESAURUS -----
    /**
     * Return the thesaurus corresponding to the file thes_fr.dat in the Thes repertory.
     * Format of a line :
     * hanse|1
     * (Nom)|guilde|corporation|association
     * @ : 1 is the number of lines following the word. The size of a line is variable.
    Output : Map<String,ArrayList<String>>
     */
    public static Map<String, ArrayList<String>> getThesaurus(String filepath) throws IOException {
        Map<String,ArrayList<String>> map = new LinkedHashMap<>();
        File f = new File(filepath);
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
                    arrayList.addAll(Arrays.asList(splitLine).subList(1, splitLine.length));
                }
                map.put(key,arrayList);
            }
            catch(Exception e) {
                e.printStackTrace();
            }
            line=br.readLine();
        }
        return map;
    }


    /**
     * Default function with default path for the {@link #getThesaurus(String)} method.
     */
    public static Map<String, ArrayList<String>> getThesaurus() throws IOException {
        return getThesaurus("Thes\\thes_fr.dat");
    }


    /**
     * Same as {@link #getOccurrences(String, int, String)} but with synonyms taken in count (with {@link #getThesaurus(String)}).. Another (easier to understand) version is shown in the {@link #getOccurrencesSynonyms(Map)} version.
     */
    public static Map<String,Integer> getOccurrencesSynonyms(String article, int n, String regex) throws IOException {
        Map<String,ArrayList<String>> thesaurus = getThesaurus();
        Map<String,Integer> map = new HashMap<>();
        String[] lines = article.split("[\n]");
        for(String line : lines){
            String[] splitted = line.split(regex);
            for(String word:splitted){
                word =word.toLowerCase();
                if(word.length()>n) {
                    //If the word already is in the HashMap, update it
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
        }
        return map;
    }

    /**
     * Same as {@link #getOccurrencesSynonyms(String,int,String)} but from a file.
     */
    public static Map<String,Integer> getOccurrencesSynonymsFromFile(String filepath, int n, String regex) throws IOException {
        return getOccurrencesSynonyms(FileUtils.getFileContent(filepath),n,regex);
    }


    /**
     * Another version of the {@link #getOccurrencesSynonyms(String,int,String)}. If you already have the result of the {@link #getOccurrences(String, int, String)} method, you should use this method.
     * If you don't, your should probably consider using the method cited above.
     * @param map Result of the {@link #getOccurrences(String, int, String)} method.
     */
    public static Map<String,Integer> getOccurrencesSynonyms(Map<String,Integer> map) throws IOException {
        Map<String,ArrayList<String>> thesaurus = getThesaurus();
        ArrayList<ArrayList<String>> thesLists= new ArrayList<>();

        //Initializing every synonyms in the map (faster execution of the next loop)
        for(String word : map.keySet()){
            thesLists.add(thesaurus.get(word));
        }

        String[] words = map.keySet().toArray(new String[0]);
        int l=words.length;
        for(int i=0;i<l;i++){
            ArrayList<String> firstWordSynonyms = thesLists.get(i);
             for(int j=i+1;j<l;j++){
                 if(words[j]==null) continue;
                 ArrayList<String> secondWordSynonyms = thesLists.get(j);
                 //If any of the two Arrays of synonyms match with one of the words, add the occurrences to the first in the map.
                 if( (firstWordSynonyms!=null && firstWordSynonyms.contains(words[j])) ||
                         (secondWordSynonyms!=null && secondWordSynonyms.contains(words[i]))){
                     map.replace(words[i],map.get(words[i])+map.get(words[j]));
                     map.remove(words[j]);
                     words[j]=null;
                 }
             }
        }
        return map;
    }
}


