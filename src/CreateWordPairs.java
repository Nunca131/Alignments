import algorithms.GotohBigram;
import container.WordDatabase;

import java.util.ArrayList;

/**
 * Created by nancy on 13.12.16.
 */
public class CreateWordPairs {

    private GotohBigram gotohBigram;

    public CreateWordPairs(GotohBigram gotohBigram){
        this.gotohBigram = gotohBigram;
    }

    public void createPairs(WordDatabase database1, WordDatabase database2){

        ArrayList<Float> meanings1 = database1.getAllMeaning();

        ArrayList<String> wordsFromSecondLang = new ArrayList<>();

        for (int i = 0; i < meanings1.size(); i++){
            if (database2.hasMeaning(meanings1.get(i))){
                wordsFromSecondLang = database1.getWordsForMeaning(meanings1.get(i));

                for (String word :wordsFromSecondLang) {
                    gotohBigram.align(database1.getWordByPos(i), word);
                }
            }
        }

    }
}
