/**
 * Created by nancy on 01.11.16.
 */
public class Align_Main {

    public static void main(String[] args) {

        Algebra alg = new Algebra();
        generalContainer container = new generalContainer();
        alg.fill("A", "A", 2.);
        alg.fill("T", "T", 2.);
        alg.fill("G", "G", 2.);
        alg.fill("C", "C", 2.);
        alg.fill("^p", "^p", 2.);
        alg.fill("pa", "pa", 2.);
        alg.fill("pa", "pe", 1.);
        alg.fill("a$", "e$", 1.5);
        alg.fill("-p","pp", 1.);


        String[] seqs = {"pampa", "pappe"};
        container.setDim(seqs.length);

       // NeedlemanWunsch nwa = new NeedlemanWunsch(seqs, alg);
        GotohBigram gotohBi = new GotohBigram(3, 3, alg);
        gotohBi.align("papa", "pappe");

        //TODO score runden auf wieviele nachkommastellen??
        //TODO dokumentation!

        //TODO local variant SWA?
        //TODO 4-way NW
        //TODO overhang aln???
        //TODO comments + doc!!!!!!
    }
}
