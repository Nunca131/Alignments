import algorithms.GotohBigram;
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
        String[] newArgs = {"Portuguese.csv","Spanish.csv"};
        args = newArgs;

        container.Algebra alg = new container.Algebra();
        container.generalContainer container = new container.generalContainer();
        /*alg.fill("A", "A", 2.);
        alg.fill("T", "T", 2.);
        alg.fill("G", "G", 2.);
        alg.fill("C", "C", 2.);
        alg.fill("^p", "^p", 2.);
        alg.fill("pa", "pa", 2.);
        alg.fill("pa", "pe", 1.);
        alg.fill("a$", "e$", 1.5);
        alg.fill("pp","-p", 1.);*/

        ScoreReader reader = new ScoreReader("Por_Spa_800_bi.los", alg);
        System.out.println(alg.getSize());
        ArrayList<WordDatabase> data = new ArrayList<>();

        for (String filename: args ) {
            WordDatabase database = new WordDatabase();
            WordListReader.readFile(filename, database);
            data.add(database);
        }


        AlignmentWriter writer = null;
        try {
            writer = new AlignmentWriter("aln.gz");
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
        //TODO score runden auf wieviele nachkommastellen??

        //TODO local variant SWA?
        //TODO 4-way NW
        //TODO overhang aln???
        //TODO comments + doc!!!!!!
    }
}
