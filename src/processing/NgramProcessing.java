package algorithms;

import container.NgramCount;
import container.generalContainer;

/**
 * Created by nancy on 09.01.17.
 */
public class NgramProcessing {

    /**
     * takes a word as input and calculates all bigrams an unigrams which a store in the respective NgramCounts (TreeMaps)
     * @param word word as a String without whitespaces
     * @param bigram TreeMap containing the bigrams of a language
     * @param unigram TreeMap containing the unigrams of a language
     */
    public void word2Ngrams(String word, NgramCount bigram, NgramCount unigram){

        if (generalContainer.nGramMode.toString().equals("bigram"))
            word = "^"+word+"$";

        for (int i = 0; i < word.length()-1; i++){
            bigram.addCount(""+word.charAt(i)+word.charAt(i+1));
            unigram.addCount(""+word.charAt(i));
        }
        unigram.addCount(""+word.charAt(word.length()-1));
    }
}
