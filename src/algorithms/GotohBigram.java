package algorithms;

import java.util.ArrayList;
import container.*;

/**
 * Created by nancy on 23.11.16.
 */
public class GotohBigram {

    private Double[][] match;
    private Double[][] insert;
    private Double[][] delete;
    private Algebra algebra;
    int dim1;
    int dim2;

    ArrayList<Double> maxList;


    private enum AlnCase{M, I, D}

    /**
     *
     * @param dim1
     * @param dim2
     * @param alg
     */
    public GotohBigram(int dim1, int dim2, Algebra alg){

        this.dim1 = dim1;
        this.dim2 = dim2;

        this.algebra = alg;
        init();

        this.maxList = new ArrayList<Double>();

    }

    public void align(String seq1, String seq2){
        seq1 = "^" + seq1 + "$";
        seq2 = "^" + seq2 + "$";

        if (seq1.length() > this.dim1 || seq2.length() > this.dim2){
            this.dim1 = seq1.length();
            this.dim2 = seq2.length();
            init();
        }

        calcScores(seq1, seq2);
        /*matrixToString(match, seq1.length(), seq2.length());
        System.out.println();
        matrixToString(insert, seq1.length(), seq2.length());
        System.out.println();
        matrixToString(delete, seq1.length(), seq2.length());*/
        backtracking(seq1, seq2);
    }

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
     * space allocated the lengths of both sequences is necessary. Note: that the first sequence
     * is handled vertically and the second horizontally.
     * @param matrix
     * @param length1
     * @param length2
     */
    //TODO: exchange rows and columns and put all bigrams
    private void matrixToString(Double[][] matrix, int length1, int length2){
        for (int i = 0; i < length1; i++){
            for (int j = 0; j < length2; j++) {
                System.out.print(matrix[i][j]+"\t");
            }
            System.out.println();
        }
    }

    /**
     * Given filled alignment matrices, backtracking can be performed in order to get the perfect alignment of both
     * sequences seq1 and seq2 and the respective alignment scores
     * @param seq1
     * @param seq2
     */
    private void backtracking(String seq1, String seq2){
        ArrayList<String> aln1Seq = new ArrayList<String>();
        ArrayList<String> aln2Seq = new ArrayList<String>();
        ArrayList<Double> scores = new ArrayList<Double>();
        AlnCase alnCase = AlnCase.M;

        double alnScore = -1000.;

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
            lastChar1 = "&";
            lastChar2 = seq2.substring(j);
        }

        //currentMatrix gets the current "case" M/D/I
        while(i > 0 || j > 0){
            aln1Seq.add(lastChar1);
            aln2Seq.add(lastChar2);

            if(i > 0 && j > 0){
                switch (alnCase){
                    case M:
                        if (match[i][j] == delete[i-1][j-1] + algebra.getScore(
                                seq1.substring(i-1, i)+lastChar1, "-"+lastChar2)){
                            alnCase = AlnCase.D;
                            scores.add(algebra.getScore(
                                    seq1.substring(i-1, i)+lastChar1, "-"+lastChar2));
                            lastChar1 = seq1.substring(i-1, i);
                            lastChar2 = "-";
                        }
                        else if (match[i][j] == insert[i-1][j-1] + algebra.getScore(
                                "-"+lastChar1, seq2.substring(j-1, j)+lastChar2)){
                            alnCase = AlnCase.I;
                            scores.add(algebra.getScore(
                                    "-"+lastChar1, seq2.substring(j-1, j)+lastChar2));
                            lastChar1 = "-";
                            lastChar2 = seq2.substring(j-1, j);
                        }
                        //else if? in that way errors could be caught here...
                        else {
                            scores.add(algebra.getScore(
                                    seq1.substring(i-1, i)+lastChar1, seq2.substring(j-1, j)+lastChar2));
                            lastChar1 = seq1.substring(i-1, i);
                            lastChar2 = seq2.substring(j-1, j);
                        }
                        i = i-1;
                        j = j-1;
                        break;
                    case I:
                        if (insert[i][j] == match[i][j-1] + algebra.getScore(
                                seq1.substring(i-1, i)+lastChar1, seq2.substring(j-1, j) + lastChar2)){
                            alnCase = AlnCase.M;
                            scores.add(algebra.getScore(
                                    seq1.substring(i-1, i)+lastChar1, seq2.substring(j-1, j)+lastChar2));
                            lastChar1 = seq1.substring(i, i+1);
                            lastChar2 = seq2.substring(j-1, j);
                        }
                        else if (insert[i][j] == delete[i-1][j] + algebra.getScore(
                                seq1.substring(i-1, i)+lastChar1, "-" + lastChar2)){
                            alnCase = AlnCase.D;
                            scores.add(algebra.getScore(
                                    seq1.substring(i-1, i)+lastChar1, "-"+lastChar2));
                            lastChar1 = seq1.substring(i, i+1);
                            lastChar2 = "-";
                        }
                        else {
                            scores.add(algebra.getScore(
                                    "-"+lastChar1, seq2.substring(j-1, j)+lastChar2));
                            lastChar1 = "-";
                            lastChar2 = seq2.substring(j-1, j);
                        }
                        j = j - 1;
                        break;
                    case D:
                        if (delete[i][j] == match[i-1][j] + algebra.getScore(
                                seq1.substring(i-1, i)+lastChar1, seq2.substring(j-1, j)+lastChar2)) {
                            alnCase = AlnCase.M;
                            scores.add(algebra.getScore(
                                    seq1.substring(i-1, i)+lastChar1, seq2.substring(j-1, j)+lastChar2));
                            lastChar1 = seq1.substring(i-1, i);
                            lastChar2 = seq2.substring(j, j+1);
                        }
                        else if (delete[i][j] == insert[i-1][j] + algebra.getScore(
                                "-"+lastChar1, seq2.substring(j-1, j)+lastChar2)){
                            alnCase = AlnCase.I;
                            scores.add(algebra.getScore(
                                    "-"+lastChar1, seq2.substring(j-1, j)+lastChar2));
                            lastChar1 = "-";
                            lastChar2 = seq2.substring(j, j+1);
                        }
                        else {
                            scores.add(algebra.getScore(
                                    seq1.substring(i-1, i)+lastChar1, "-"+lastChar2));
                            lastChar1 = seq1.substring(i-1, i);
                            lastChar2 = "-";
                        }
                        i = i - 1;
                        break;
                }
            }
           // System.out.println(lastChar1 + " " + lastChar2);
        }

        if (alnCase.equals(AlnCase.M)){
            aln1Seq.add("^");
            aln2Seq.add("^");
        }
        else
            System.err.println("Backtracking did not end in Match");

        //TODO get IDS and output stream here (or move it somewhere else)
        System.out.println("IDS: " + " Score " + alnScore + " NScore " +
                ((double)Math.round((alnScore*100)/scores.size()))/100 + " Word1 " + seq1 + " Word2 " + seq2);

        for (int a = aln1Seq.size()-1; a >= 0; a--){
            System.out.print("    " + aln1Seq.get(a));
        }
        System.out.println();
        for (int a = aln2Seq.size()-1; a >= 0; a--){
            System.out.print("    " + aln2Seq.get(a));
        }
        System.out.println();
        System.out.print("    ");
        for (int b = scores.size()-1; b >= 0; b--){
            if (scores.get(b) > 0)
                System.out.print("  " + scores.get(b));
            else
                System.out.print(" " + scores.get(b));

            //TODO: check whether alnScore is sum of scores(i) careful with top/floor calc
        }
    }
}
