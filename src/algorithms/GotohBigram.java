package algorithms;

import java.util.ArrayList;
import container.*;
import io.AlignmentWriter;
import io.WordListReader;

/**
 * Created by nancy on 23.11.16.
 */
public class GotohBigram {

    private Double[][] match;
    private Double[][] insert;
    private Double[][] delete;
    private Algebra algebra;
    private int dim1;
    private int dim2;

    private WordDatabase wordDatabase1;
    private WordDatabase wordDatabase2;

    private ArrayList<Double> maxList;

    private enum AlnCase{M, I, D}

    ArrayList<String> aln1Seq = new ArrayList<>();
    ArrayList<String> aln2Seq = new ArrayList<>();
    ArrayList<Double> scores = new ArrayList<>();


    double alnScore = -1000.;

    AlignmentWriter writer;

    /**
     *
     * @param dim1 length of x-axis of first alignment matrix allocation
     * @param dim2 length of y-axis of first alignment matrix allocation
     * @param alg algebra that contains alignment scores
     * @param wordDatabase1 contains all words, their IDs, and meanings of first language
     * @param wordDatabase2 contains all words, their IDs, and meanings of second language
     * @param writer open Stream for alignment output
     */
    public GotohBigram(int dim1, int dim2, Algebra alg,
                       WordDatabase wordDatabase1, WordDatabase wordDatabase2,
                       AlignmentWriter writer){



        this.dim1 = dim1;
        this.dim2 = dim2;

        this.algebra = alg;
        init();

        this.maxList = new ArrayList<>();

        this.wordDatabase1 = wordDatabase1;
        this.wordDatabase2 = wordDatabase2;

        this.writer = writer;

    }

    /**
     * aligns two sequences in Gotoh style based on their bigrams
     * @param id1 wordID of first sequence in first database
     * @param id2 wordID of second sequence in second database
     */
    public void align(String id1, String id2){
        String seq1 = "^" + this.wordDatabase1.getWordByID(id1) + "$";
        String seq2 = "^" + this.wordDatabase2.getWordByID(id2) + "$";
        //System.out.println(seq1 + " " + seq2);

        if (seq1.length() > this.dim1 || seq2.length() > this.dim2){
            this.dim1 = seq1.length();
            this.dim2 = seq2.length();
            init();
        }

        calcScores(seq1, seq2);
        //for checking the matrices on cmd line (will not be shown in alignment file)
        /*matrixToString(match, seq1.length(), seq2.length());
        System.out.println();
        matrixToString(insert, seq1.length(), seq2.length());
        System.out.println();
        matrixToString(delete, seq1.length(), seq2.length());*/
        backtracking(seq1, seq2);
        System.out.println(id1 + " " + id2);
        createAlnLines(id1, id2);
    }

    /**
     * Calculates the maximum of maxList (local variable: ArrayList<Double>) and returns its maximum as double
     * @return double of biggest entry
     */
    private double max(){
        double max = -100000.;
        if(this.maxList.size() == 1)
            System.err.println("max call on singleton");
        for(int i = 1; i < this.maxList.size(); i++){
            if (this.maxList.get(i-1) > this.maxList.get(i))
                max = Math.max(this.maxList.get(i-1), max);

            else
                max = Math.max(max, this.maxList.get(i));
        }

        return max;
    }

    private void init(){
        this.match = new Double[dim1][dim2];
        this.insert = new Double[dim1][dim2];
        this.delete = new Double[dim1][dim2];

        match[0][0] = 0.;
        delete[0][0]= -100000.;
        insert[0][0]= -100000.;

        for(int i = 1; i < this.dim1; i++){
            match[i][0] = -100000.;
            insert[i][0] = -100000.;
            if (i > 1)
                delete[i][0] = delete[i-1][0] + generalContainer.gapExtend;
            else
                delete[i][0] = generalContainer.gapOpen;
        }

        for (int j = 1; j < this.dim2; j++){
            match[0][j] = -100000.;
            delete[0][j] = -100000.;
            if (j > 1)
                insert[0][j] = insert[0][j-1] + generalContainer.gapExtend;
            else
                insert[0][j] = generalContainer.gapOpen;
        }
    }

    private void calcScores(String seq1, String seq2){

        this.maxList.clear();

        for (int i = 1; i < seq1.length(); i++){
            for (int j = 1; j < seq2.length(); j++){
                //filling match table in i,j
                this.maxList.add(match[i-1][j-1] +
                        algebra.getScore(seq1.substring(i-1, i+1), seq2.substring(j-1, j+1)));
                this.maxList.add(insert[i-1][j-1] +
                        algebra.getScore("-"+seq1.substring(i, i+1), seq2.substring(j-1, j+1)));
                this.maxList.add(delete[i-1][j-1] +
                        algebra.getScore(seq1.substring(i-1, i+1), "-"+seq2.substring(j, j+1)));
                match[i][j] = max();
                this.maxList.clear();

                //filling insert table in i,j
                this.maxList.add(match[i][j-1] +
                        algebra.getScore(seq1.substring(i, i+1)+"-", seq2.substring(j-1, j+1)));
                this.maxList.add(insert[i][j-1] +
                        algebra.getScore("--", seq2.substring(j-1, j+1)));
                this.maxList.add(delete[i][j-1] +
                        algebra.getScore(seq1.substring(i, i+1)+"-", "-"+seq2.substring(j ,j+1)));
                insert[i][j] = max();
                this.maxList.clear();

                //filling delete table in i,j
                this.maxList.add(match[i-1][j] +
                        algebra.getScore(seq1.substring(i-1, i+1), seq2.substring(j, j+1)+"-"));
                this.maxList.add(insert[i-1][j] +
                        algebra.getScore("-"+seq1.substring(i, i+1), seq2.substring(j, j+1)+"-"));
                this.maxList.add(delete[i-1][j] + algebra.getScore(seq1.substring(i-1, i+1), "--"));
                delete[i][j] = max();
                this.maxList.clear();
            }
        }
    }

    /**
     * Writes alignment matrix on terminal (not in a file!). Since the matrix usually has a bigger
     * space allocated the lengths of both sequences is necessary. First sequence is written vertically,
     * second sequence horizontally
     * @param matrix alignment matrix
     * @param length1 length of first input sequence
     * @param length2 length of second input sequence
     */
        private void matrixToString(Double[][] matrix, int length1, int length2){
        for (int i = 0; i < length1; i++){
            for (int j = 0; j < length2; j++){
                System.out.print(matrix[i][j]+"\t");
            }
            System.out.println();
        }
    }

    /**
     * Given filled alignment matrices, backtracking can be performed in order to get the perfect alignment of both
     * sequences seq1 and seq2 and the respective alignment scores
     * @param seq1 first input sequence
     * @param seq2 second input sequence
     */
    private void backtracking(String seq1, String seq2){
        aln1Seq.clear();
        aln2Seq.clear();
        scores.clear();

        AlnCase alnCase = AlnCase.M;

        String lastChar1 = "";
        String lastChar2 = "";

        int i = seq1.length()-1;
        int j = seq2.length()-1;

        //TODO:enforcing $$ match? Always start from M
        if (match[i][j] >= delete[i][j] && match[i][j] >= insert[i][j]){
                alnCase = AlnCase.M; //m>d,i
            alnScore = match[i][j];
            lastChar1 = seq1.substring(i);
            lastChar2 = seq2.substring(j);
        }
        if (delete[i][j] >= insert[i][j] && delete[i][j] > match[i][j]){
            alnCase = AlnCase.D; //d>m,i
            alnScore = delete[i][j];
            lastChar1 = seq1.substring(i);
            lastChar2 = "-";
        }
        if (insert[i][j] > delete[i][j] && insert[i][j] > match[i][j]){
            alnCase = AlnCase.I;
            alnScore = insert[i][j];
            lastChar1 = "-";
            lastChar2 = seq2.substring(j);
        }

        //currentMatrix gets the current "case" M/D/I
        while(i > 0 || j > 0){
            this.aln1Seq.add(lastChar1);
            this.aln2Seq.add(lastChar2);

            if(i > 0 && j > 0){
                switch (alnCase){
                    case M:
                        if (this.match[i][j] == this.delete[i-1][j-1] + this.algebra.getScore(
                                seq1.substring(i-1, i)+lastChar1, "-"+lastChar2)){
                            alnCase = AlnCase.D;
                            this.scores.add(this.algebra.getScore(
                                    seq1.substring(i-1, i)+lastChar1, "-"+lastChar2));
                            lastChar1 = seq1.substring(i-1, i);
                            lastChar2 = "-";
                        }
                        else if (this.match[i][j] == this.insert[i-1][j-1] + this.algebra.getScore(
                                "-"+lastChar1, seq2.substring(j-1, j)+lastChar2)){
                            alnCase = AlnCase.I;
                            this.scores.add(this.algebra.getScore(
                                    "-"+lastChar1, seq2.substring(j-1, j)+lastChar2));
                            lastChar1 = "-";
                            lastChar2 = seq2.substring(j-1, j);
                        }
                        //else if? in that way errors could be caught here...
                        else if (this.match[i][j] == this.match[i-1][j-1] + this.algebra.getScore(
                               seq1.substring(i-1, i)+lastChar1, seq2.substring(j-1, j)+lastChar2)){
                            this.scores.add(this.algebra.getScore(
                                    seq1.substring(i-1, i)+lastChar1, seq2.substring(j-1, j)+lastChar2));
                            lastChar1 = seq1.substring(i-1, i);
                            lastChar2 = seq2.substring(j-1, j);
                        }
                        else{
                            System.err.println("Could not find right matching case");
                        }
                        i = i-1;
                        j = j-1;
                        break;
                    case I:
                        if (this.insert[i][j] == this.match[i][j-1] + this.algebra.getScore(
                                seq1.substring(i-1, i)+lastChar1, seq2.substring(j-1, j) + lastChar2)){
                            alnCase = AlnCase.M;
                            this.scores.add(this.algebra.getScore(
                                    seq1.substring(i-1, i)+lastChar1, seq2.substring(j-1, j)+lastChar2));
                            lastChar1 = seq1.substring(i, i+1);
                            lastChar2 = seq2.substring(j-1, j);
                        }
                        else if (this.insert[i][j] == this.delete[i][j-1] + this.algebra.getScore(
                                seq1.substring(i-1, i)+lastChar1, "-" + lastChar2)){
                            alnCase = AlnCase.D;
                            this.scores.add(this.algebra.getScore(
                                    seq1.substring(i-1, i)+lastChar1, "-"+lastChar2));
                            lastChar1 = seq1.substring(i, i+1);
                            lastChar2 = "-";
                        }
                        else if (this.insert[i][j] == this.insert[i][j-1] + this.algebra.getScore(
                                "-"+lastChar1, seq2.substring(j-1, j) + lastChar2)){
                            this.scores.add(this.algebra.getScore(
                                    "-"+lastChar1, seq2.substring(j-1, j)+lastChar2));
                            lastChar1 = "-";
                            lastChar2 = seq2.substring(j-1, j);
                        }
                        else{
                            System.err.println("Could not find right insertion case");
                        }
                        j = j - 1;
                        break;
                    case D:
                        if (this.delete[i][j] == this.match[i-1][j] + this.algebra.getScore(
                                seq1.substring(i-1, i)+lastChar1, seq2.substring(j-1, j)+lastChar2)) {
                            alnCase = AlnCase.M;
                            this.scores.add(this.algebra.getScore(
                                    seq1.substring(i-1, i)+lastChar1, seq2.substring(j-1, j)+lastChar2));
                            lastChar1 = seq1.substring(i-1, i);
                            lastChar2 = seq2.substring(j, j+1);
                        }
                        else if (this.delete[i][j] == this.insert[i-1][j] + this.algebra.getScore(
                                "-"+lastChar1, seq2.substring(j-1, j)+lastChar2)){
                            alnCase = AlnCase.I;
                            this.scores.add(this.algebra.getScore(
                                    "-"+lastChar1, seq2.substring(j-1, j)+lastChar2));
                            lastChar1 = "-";
                            lastChar2 = seq2.substring(j, j+1);
                        }
                        else if (this.delete[i][j] == this.delete[i-1][j] + this.algebra.getScore(
                                seq1.substring(i-1, i)+lastChar1, "-"+lastChar2)){
                            this.scores.add(this.algebra.getScore(
                                    seq1.substring(i-1, i)+lastChar1, "-"+lastChar2));
                            lastChar1 = seq1.substring(i-1, i);
                            lastChar2 = "-";
                        }
                        else{
                            System.err.println("Could not find right deletion case");
                        }
                        i = i - 1;
                        break;
                }
            }
            else if (i > 0 && j == 0){
                if (i == 1){
                    this.scores.add(this.algebra.getScore(
                            seq1.substring(i-1, i)+lastChar1, "^"+lastChar2));
                }
                else {
                    this.scores.add(this.algebra.getScore(
                            seq1.substring(i - 1, i) + lastChar1, "-" + lastChar2));
                }
                lastChar1 = seq1.substring(i-1, i);
                lastChar2 = "-";
                i = i - 1;
            }
            else if (j > 0 && i == 0){
                if (j == 1){
                    this.scores.add(this.algebra.getScore(
                            "^"+lastChar1, seq2.substring(j-1, j)+lastChar2));
                }
                else {
                    this.scores.add(this.algebra.getScore(
                            "-" + lastChar1, seq2.substring(j - 1, j) + lastChar2));
                }
                lastChar1 = "-";
                lastChar2 = seq2.substring(j-1, j);
                j = j - 1;
            }
            else{
                System.err.println("backtracking failed! Please sent a mail to nancy@bioinf.uni-leipzig.de with your input data.");
                return;
            }

           // System.out.println(lastChar1 + " " + lastChar2);
        }

        this.aln1Seq.add("^");
        this.aln2Seq.add("^");
    }

    private void createAlnLines(String id1, String id2){
        //line of all formal information
        String out = "IDS: " + id1 + " " + id2 + " Score " + ((double)Math.round(alnScore*100))/100 +
                " NScore " + ((double)Math.round((alnScore*100)/scores.size()))/100 + " Word1 " +
                this.wordDatabase1.getWordByID(id1) + " Word2 " + this.wordDatabase2.getWordByID(id2);
        writer.write(out);

        //line of first aligned sequence
        out = "";
        for (int a = aln1Seq.size()-1; a >= 0; a--){
            out += "    " + aln1Seq.get(a);
        }
        writer.write(out);

        //line of second aligned sequence
        out = "";
        for (int a = aln2Seq.size()-1; a >= 0; a--){
            out +="    " + aln2Seq.get(a);
        }
        writer.write(out);

        //score line
        double summedScores = 0.;
        out ="    ";
        for (int b = scores.size()-1; b >= 0; b--){
            summedScores += scores.get(b);
            if (scores.get(b) > 0)
                out += "  " + scores.get(b);
            else
                out += " " + scores.get(b);
            //TODO: check whether alnScore is sum of scores(i) careful with top/floor calc
        }
        writer.write(out);
        writer.write("");
        if(Math.round(summedScores*100) != Math.round(alnScore*100)) {
            System.err.println("Score does not match: " + id1 + " " + id2);
            writer.close();
            System.exit(1);
        }
    }
}
