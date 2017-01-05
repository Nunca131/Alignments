import java.util.ArrayList;

/**
 * Created by nancy on 24.11.16.
 */
public class NWBigram {

        private Double[][] match;
        private String[] seqs;
        private container.Algebra algebra;

        public NWBigram(String[] seqs, container.Algebra alg){
            this.seqs = new String[seqs.length];
            for (int i = 0; i < this.seqs.length; i++){
                this.seqs[i] = "^" + seqs[i] + "$";
                System.out.println(this.seqs[i]);
            }
            this.match = new Double[this.seqs[0].length()][this.seqs[1].length()];
            this.algebra = alg;

            init();
            calcScores();
            matrixToString(match);
            System.out.println();

            backtracking();
        }

        private double max(ArrayList<Double> list){
            double max = -100000.;
            if(list.size() == 1)
                System.err.println("max call on singleton");
            for(int i = 1; i < list.size(); i++){
                if (list.get(i-1) > list.get(i))
                    max = Math.max(list.get(i-1), max);

                else
                    max = Math.max(max, list.get(i));
            }

            return max;
        }

        private void init(){
            match[0][0] = 0.;
            match[1][0] = container.generalContainer.gapOpen;
            match[0][1] = container.generalContainer.gapOpen;
            for(int i = 2; i < seqs[0].length(); i++){
                match[i][0] = match[i-1][0] + container.generalContainer.gapExtend;
            }

            for (int j = 2; j < seqs[1].length(); j++){
                match[0][j] = match[0][j-1] + container.generalContainer.gapExtend;
            }
        }

        private void calcScores(){
            ArrayList<Double> maxList = new ArrayList<Double>();
            maxList.clear();

            for (int i = 1; i < seqs[0].length(); i++){
                for (int j = 1; j < seqs[1].length(); j++){
                    maxList.add(match[i-1][j-1] +
                            algebra.getScore(seqs[0].substring(i-1,i+1), seqs[1].substring(j-1,j+1)));
                    maxList.add(match[i][j-1] +
                            algebra.getScore(seqs[0].substring(i,i+1)+"-", seqs[1].substring(j-1,j+1)));
                    maxList.add(match[i-1][j] +
                            algebra.getScore(seqs[0].substring(i-1,i+1), seqs[1].substring(j,j+1)+"-"));
                    match[i][j] = max(maxList);
                    maxList.clear();
                }
            }
        }

        private void matrixToString(Double[][] matrix){
            for (int i = 0; i < matrix.length; i++){
                for (int j = 0; j < matrix[0].length; j++) {
                    System.out.print(matrix[i][j]+"\t");
                }
                System.out.println();
            }
        }

        private void backtracking(){
            System.out.println("backt");
            ArrayList<String> aln1Seq = new ArrayList<String>();
            ArrayList<String> aln2Seq = new ArrayList<String>();

            String lastChar1 = "";
            String lastChar2 = "";

            int i = seqs[0].length()-1;
            int j = seqs[1].length()-1;

            lastChar1 = seqs[0].substring(i);
            lastChar2 = seqs[1].substring(j);
            System.out.println(match[i][j]);

            System.out.println(i + " " + j);

            //currentMatrix gets the current "case" M/D/I
            while(i > 0 || j > 0){
                if(i > 0 && j > 0){
                    if(match[i][j] == match[i-1][j-1] + algebra.getScore(
                            seqs[0].substring(i-1,i)+lastChar1,
                            seqs[1].substring(j-1,j)+lastChar2)){
                        System.out.println(seqs[0].substring(i-1,i)+lastChar1 + " " +
                                seqs[1].substring(j-1,j)+lastChar2);
                        lastChar1 = seqs[0].substring(i-1,i);
                        lastChar2 = seqs[1].substring(j-1,j);
                        i = i - 1;
                        j = j - 1;
                    }

                    else if (match[i][j] == match[i][j-1] + algebra.getScore(
                            "-"+lastChar1,
                            seqs[1].substring(j-1,j) + lastChar2)){
                        System.out.println(i+" la "+j);
                        lastChar1 = "-";
                        lastChar2 = seqs[1].substring(j-1,j);
                        j = j - 1;
                    }
                    else if (match[i][j] == match[i-1][j] + algebra.getScore(
                            seqs[0].substring(i-1,i)+lastChar1,
                            "-"+lastChar2)){
                        System.out.println(i+" "+j);
                        lastChar1 = seqs[0].substring(i-1,i);
                        lastChar2 = "-";
                        i = i - 1;
                    }
                }
                else if (i == 0){
                    System.out.println(i+" "+j);
                    lastChar1 = "-";
                    lastChar2 = seqs[1].substring(j-1,j);
                    j = j - 1;
                }
                else {
                    System.out.println(i+" "+j);
                    lastChar1 = seqs[0].substring(i-1,i);
                    lastChar2 = "-";
                    i = i - 1;
                }
                aln1Seq.add(lastChar1);
                aln2Seq.add(lastChar2);
                System.out.println(lastChar1 + " " + lastChar2);
            }
            for (int a = aln1Seq.size()-1; a >= 0; a--){
                System.out.print(aln1Seq.get(a));
            }
            System.out.println();
            for (int a = aln2Seq.size()-1; a >= 0; a--){
                System.out.print(aln2Seq.get(a));
            }
        }
    }