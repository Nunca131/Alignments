import algorithms.CountNgrams;
import algorithms.GotohBigram;
import container.NgramCount;
import container.WordDatabase;
import io.AlignmentWriter;
import io.ScoreReader;
import io.WordListReader;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by nancy on 01.11.16.
 */
public class Align_Main {

    public static void main(String[] args) {
        String[] newArgs = {"Portuguese_singleMeaning.csv", "Spanish_singleMeaning.csv"};
        //String[] newArgs = {"-h"};
        args = newArgs;

        if (args.length == 1 && (args[0] == "-h" || args[0] == "--help")) {
            Runtime rt = Runtime.getRuntime();
            try {
                Process pr = rt.exec("cat help.txt");
                System.out.println("invoked command line?");
            } catch (IOException e) {
                e.printStackTrace();
            }
            return;
        }

        //container.generalContainer.trainScores = true;

        container.Algebra alg = new container.Algebra();
        container.generalContainer container = new container.generalContainer();


        ScoreReader reader = new ScoreReader("Por_Spa_800_biAndUni.los", alg);
        System.out.println(alg.getSize());
        ArrayList<WordDatabase> data = new ArrayList<>();

        ArrayList<CountNgrams> counts = new ArrayList<>();

        for (String filename: args ) {
            WordDatabase database = new WordDatabase();
            if (container.trainScores) {
                CountNgrams countNgrams = new CountNgrams();
                WordListReader.readFile(filename, database, countNgrams);
                counts.add(countNgrams);
            }
            else
                WordListReader.readFile(filename, database);
            data.add(database);
        }


        AlignmentWriter writer = null;
        try {
            writer = new AlignmentWriter("Por_Spa_singletons.aln.gz");
        } catch (IOException e) {
            e.printStackTrace();
        }


        // NeedlemanWunsch nwa = new NeedlemanWunsch(seqs, alg);
        for (int i = 0; i < data.size()-1; i++){
            GotohBigram gotohBi = new GotohBigram(30, 30, alg, data.get(i), data.get(i+1), writer);
            CreateWordPairs pairs = new CreateWordPairs(gotohBi);
            pairs.createPairs(data.get(i), data.get(i+1));
        }


        writer.close();

        //TODO local variant SWA?
        //TODO 4-way NW
        //TODO overhang aln???
        //TODO comments + doc!!!!!!
    }
}
