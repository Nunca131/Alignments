package processing;

import algorithms.GotohBigram;
import algorithms.NWUnigram;
import container.WordDatabase;
import container.generalContainer;

import java.util.ArrayList;

/**
 * Created by nancy on 13.12.16.
 */
public class CreateWordPairs {

    private GotohBigram gotohBigram;
    private NWUnigram nwUnigram;

    public CreateWordPairs(GotohBigram gotohBigram){
        this.gotohBigram = gotohBigram;
    }
    public CreateWordPairs(NWUnigram nwUnigram) {this.nwUnigram = nwUnigram;}

    public void createSameMeaningPairs(WordDatabase database1, WordDatabase database2){

        ArrayList<Float> meanings1 = database1.getAllMeaning();

        ArrayList<String> iDsFromSecondLang;

        for (int i = 0; i < meanings1.size(); i++){
            if (database2.hasMeaning(meanings1.get(i))){
                iDsFromSecondLang = database2.getIDsForMeaning(meanings1.get(i));
                for (String iD :iDsFromSecondLang) {
                    if (container.generalContainer.alnMode == generalContainer.AlnMode.GotohBi)
                        gotohBigram.align(database1.getWordByID(database1.getIDByPos(i)), database2.getWordByID(iD));
                    else if (generalContainer.alnMode == generalContainer.AlnMode.NWA)
                        nwUnigram.align(database1.getIDByPos(i), iD);
                }
            }
        }

    }

    //creates all word pairs
    public void createAllPairs(){

    }
}
