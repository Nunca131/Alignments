package processing;

import java.util.ArrayList;
import java.util.TreeMap;

/**
 * Created by nancy on 07.02.17.
 */
public class WordCleaning {

    private ArrayList<String> chars;
    private TreeMap<String, String> replacements;

    public WordCleaning(ArrayList<String> chars, TreeMap<String, String> replacements){
        this.chars = chars;
        this.replacements = replacements;
    }


    public String cleanWord(String wordIn){

        wordIn = wordIn.toLowerCase();
        for (String repls : replacements.keySet()){
            while (wordIn.contains(repls)){
                wordIn = wordIn.replace(repls, replacements.get(repls));
            }
        }

        int i = 0;
        while(i < wordIn.length() && chars.contains(wordIn.charAt(i)+""))
            i++;

        //while loop terminated because it contains a unknown character
        if (i < wordIn.length()-1) {
            for (int j = 0; j < wordIn.length(); j++){
                System.err.print(wordIn.charAt(j) + " ");
            }
            System.err.println("contains an unknown character at position " + i);
        }

        return wordIn;
    }
}
