import java.util.ArrayList;
import container.generalContainer;
/**
 * Created by nancy on 01.11.16.
 */
public class NeedlemanWunsch {

    private AlignmentTable table;
    private String[] seqs;
    private container.Algebra algebra;
    private generalContainer.Dimension dim;

    public NeedlemanWunsch(String[] seqs, container.Algebra algebra, generalContainer.Dimension dim){
        this.dim = dim;
        switch (this.dim){
            case TWO:
                this.table = new AlignmentTable(seqs[0].length(), seqs[1].length());
                break;
            case THREE:
                this.table = new AlignmentTable(seqs[0].length(), seqs[1].length(), seqs[2].length());
                break;
            case FOUR:
                this.table = new AlignmentTable(seqs[0].length(), seqs[1].length(), seqs[2].length(), seqs[3].length());
                break;
        }
        this.seqs = seqs;
        this.algebra = algebra;

        initialize();
        calcScores();
        tableToString();
        backtracking();
    }

    private double max(ArrayList<Double> list){
        double max = -10000.;
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

    private double sumOfPairsScore(String a, String b, String c){
        double sps = 0.;

        sps += algebra.getScore(a, b);
        sps += algebra.getScore(a, c);
        sps += algebra.getScore(b, c);

        return  sps;
    }

    private double sumOfPairsScore(String a, String b, String c, String d){
        double sps = 0.;

        sps += algebra.getScore(a, b);
        sps += algebra.getScore(a, c);
        sps += algebra.getScore(a, d);
        sps += algebra.getScore(b, c);
        sps += algebra.getScore(b, d);
        sps += algebra.getScore(c, d);

        return sps;
    }

    private void initialize(){
        ArrayList<Double> maxList = new ArrayList<Double>();

        switch (this.dim) {
            case TWO:
                table.setScore(0, 0, 0.);
                for (int i = 1; i < table.getX(); i++) {
                    table.setScore(i, 0, generalContainer.gapOpen * i);
                }
                for (int j = 1; j < table.getY(); j++) {
                    table.setScore(0, j, generalContainer.gapOpen * j);
                }
                break;
            case THREE:
                table.setScore(0, 0, 0, 0.);
                for (int i = 1; i < table.getX(); i++) {
                    table.setScore(i, 0, 0, generalContainer.gapOpen * i * 2);
                }
                for (int j = 1; j < table.getY(); j++) {
                    table.setScore(0, j, 0, generalContainer.gapOpen * j * 2);
                }
                for (int k = 1; k < table.getZ(); k++) {
                    table.setScore(0, 0, k, generalContainer.gapOpen * k * 2);
                }
                for (int i = 1; i < table.getX(); i++){
                    for (int j = 1; j < table.getY(); j++){
                        maxList.clear();
                        maxList.add(table.getEntry(i-1, j-1, 0) +
                                        sumOfPairsScore(seqs[0].substring(i-1,i), seqs[1].substring(j-1,j),"-"));
                        maxList.add(table.getEntry(i-1,j,0) + sumOfPairsScore(seqs[0].substring(i-1,i),"-","-"));
                        maxList.add(table.getEntry(i,j-1,0) + sumOfPairsScore("-",seqs[1].substring(j-1,j),"-"));
                        table.setScore(i, j, 0, max(maxList));
                    }
                    for (int k = 1; k < table.getZ(); k++){
                        maxList.clear();
                        maxList.add(table.getEntry(i-1, 0, k-1) +
                                sumOfPairsScore(seqs[0].substring(i-1,i),"-", seqs[2].substring(k-1,k)));
                        maxList.add(table.getEntry(i-1,0,k) + sumOfPairsScore(seqs[0].substring(i-1,i),"-","-"));
                        maxList.add(table.getEntry(i,0,k-1) + sumOfPairsScore("-","-",seqs[2].substring(k-1,k)));
                        table.setScore(i, 0, k, max(maxList));
                    }
                }
                for (int j = 1; j < table.getY(); j++) {
                    for (int k = 1; k < table.getZ(); k++){
                        maxList.clear();
                        maxList.add(table.getEntry(0, j-1, k-1) +
                                sumOfPairsScore("-", seqs[1].substring(j-1,j), seqs[2].substring(k-1,k)));
                        maxList.add(table.getEntry(0,j-1,k) + sumOfPairsScore("-",seqs[1].substring(j-1,j), "-"));
                        maxList.add(table.getEntry(0,j,k-1) + sumOfPairsScore("-","-", seqs[2].substring(k-1,k)));
                        table.setScore(0, j, k, max(maxList));
                    }
                }
                break;
            case FOUR:
                table.setScore(0, 0, 0, 0, 0.);
                for (int i = 1; i < table.getX(); i++) {
                    table.setScore(i, 0, 0, 0, generalContainer.gapOpen * i * 3);
                }
                for (int j = 1; j < table.getY(); j++){
                    table.setScore(0, j, 0, 0, generalContainer.gapOpen * j * 3);
                }
                for (int k = 1; k < table.getZ(); k++){
                    table.setScore(0, 0, k, 0, generalContainer.gapOpen * k * 3);
                }
                for (int l = 1; l < table.getA(); l++){
                    table.setScore(0, 0, 0, l, generalContainer.gapOpen * l * 3);
                }

                for (int i = 1; i < table.getX(); i++){
                    for (int j = 1; j < table.getY(); j++){
                        maxList.clear();
                        maxList.add(table.getEntry(i - 1, j - 1, 0, 0) +
                                sumOfPairsScore(seqs[0].substring(i-1, i), seqs[1].substring(j-1,j), "-", "-"));
                        maxList.add(table.getEntry(i -1, j, 0, 0) +
                                sumOfPairsScore(seqs[0].substring(i-1,i), "-", "-", "-"));
                        maxList.add(table.getEntry(i, j-1, 0, 0) +
                                sumOfPairsScore("-", seqs[1].substring(j-1,j), "-", "-"));
                        table.setScore(i, j, 0, 0, max(maxList));
                    }
                    for (int k = 1; k < table.getZ(); k++){
                        maxList.clear();
                        maxList.add(table.getEntry(i - 1, 0, k - 1, 0) +
                                sumOfPairsScore(seqs[0].substring(i-1, i), "-", seqs[2].substring(k-1,k), "-"));
                        maxList.add(table.getEntry(i -1, k, 0, 0) +
                                sumOfPairsScore(seqs[0].substring(i-1,i), "-", "-", "-"));
                        maxList.add(table.getEntry(i, 0, k-1, 0) +
                                sumOfPairsScore("-", "-", seqs[2].substring(k-1,k), "-"));
                        table.setScore(i, 0, k, 0, max(maxList));
                    }
                    for (int l = 1; l < table.getZ(); l++){
                        maxList.clear();
                        maxList.add(table.getEntry(i - 1, 0, 0, l - 1) +
                                sumOfPairsScore(seqs[0].substring(i-1, i), "-", "-", seqs[3].substring(l-1,l)));
                        maxList.add(table.getEntry(i -1, 0, 0, l) +
                                sumOfPairsScore(seqs[0].substring(i-1,i), "-", "-", "-"));
                        maxList.add(table.getEntry(i, 0, 0, l-1) +
                                sumOfPairsScore("-", "-", "-", seqs[3].substring(l-1,l)));
                        table.setScore(i, 0, 0, l, max(maxList));
                    }
                }

                for (int j = 1; j < table.getY(); j++){
                    for (int k = 1; k < table.getZ(); k++){
                        maxList.clear();
                        maxList.add(table.getEntry(0, j - 1, k - 1, 0) +
                                sumOfPairsScore("-", seqs[1].substring(j-1,j), seqs[2].substring(k-1, k), "-"));
                        maxList.add(table.getEntry(0, j-1, k, 0) +
                                sumOfPairsScore("-", seqs[0].substring(j-1,j), "-", "-"));
                        maxList.add(table.getEntry(0, j, k-1, 0) +
                                sumOfPairsScore("-", "-", seqs[2].substring(k-1,k), "-"));
                        table.setScore(0, j, k, 0, max(maxList));
                    }
                    for (int l = 1; l < table.getA(); l++){
                        maxList.clear();
                        maxList.add(table.getEntry(0, j - 1, 0, l - 1) +
                                sumOfPairsScore("-", seqs[1].substring(j-1,j), "-", seqs[3].substring(l-1, l)));
                        maxList.add(table.getEntry(0, j-1, 0, l) +
                                sumOfPairsScore("-", seqs[0].substring(j-1,j), "-", "-"));
                        maxList.add(table.getEntry(0, j, 0, l-1) +
                                sumOfPairsScore("-", "-", "-", seqs[3].substring(l-1,l)));
                        table.setScore(0, j, 0, l, max(maxList));
                    }
                }

                for (int k = 1; k < table.getZ(); k++){
                    for (int l = 1; l < table.getA(); l++){
                        maxList.clear();
                        maxList.add(table.getEntry(0, 0, k-1, l-1) +
                                sumOfPairsScore("-", "-", seqs[2].substring(k-1,k), seqs[3].substring(l-1,l)));
                        maxList.add(table.getEntry(0, 0, k-1, l) +
                                sumOfPairsScore("-", "-", seqs[2].substring(k-1,k), "-"));
                        maxList.add(table.getEntry(0, 0, k, l-1) +
                                sumOfPairsScore("-", "-", "-", seqs[3].substring(l-1,l)));
                        table.setScore(0, 0, k, l, max(maxList));
                    }
                }

                //TODO: "3-dim. Ebenen ausfuellen"
                for (int i = 1; i < table.getX(); i++){
                    for (int j = 1; j < table.getY(); j++){
                        for (int k = 1; k < table.getZ(); k++){
                            maxList.clear();
                            maxList.add(table.getEntry(i-1,j-1,k-1,0) + sumOfPairsScore(seqs[0].substring(i-1,i),
                                    seqs[1].substring(j-1,j), seqs[2].substring(k-1,k), "-"));
                            maxList.add(table.getEntry(i-1,j-1,k,0) + sumOfPairsScore(seqs[0].substring(i-1,i),
                                    seqs[1].substring(j-1,j), "-", "-"));
                            maxList.add(table.getEntry(i-1,j,k-1,0) + sumOfPairsScore(seqs[0].substring(i-1,i),
                                    "-", seqs[2].substring(k-1,k), "-"));
                            maxList.add(table.getEntry(i,j-1,k-1,0) + sumOfPairsScore("-", seqs[1].substring(j-1,j),
                                    seqs[2].substring(k-1,k), "-"));
                            maxList.add(table.getEntry(i-1,j,k,0) + sumOfPairsScore(seqs[0].substring(i-1,i), "-", "-", "-"));
                            maxList.add(table.getEntry(i,j-1,k,0) + sumOfPairsScore("-", seqs[1].substring(j-1,j), "-", "-"));
                            maxList.add(table.getEntry(i,j,k-1,0) + sumOfPairsScore("-", "-", seqs[2].substring(k-1,k), "-"));
                            table.setScore(i ,j ,k, max(maxList));
                        }
                        for (int l = 1; l < table.getA(); l++){
                            maxList.clear();
                            maxList.add(table.getEntry(i-1,j-1,0,l-1) + sumOfPairsScore(seqs[0].substring(i-1,i),
                                    seqs[1].substring(j-1,j), "-", seqs[3].substring(l-1,l)));
                            maxList.add(table.getEntry(i-1,j-1,0,l) + sumOfPairsScore(seqs[0].substring(i-1,i),
                                    seqs[1].substring(j-1,j), "-", "-"));
                            maxList.add(table.getEntry(i-1,j,0,l-1) + sumOfPairsScore(seqs[0].substring(i-1,i),
                                    "-", "-", seqs[3].substring(l-1,l)));
                            maxList.add(table.getEntry(i,j-1,0,l-1) + sumOfPairsScore("-", seqs[1].substring(j-1,j),
                                    "-", seqs[3].substring(l-1,l)));
                            maxList.add(table.getEntry(i-1,j,0,l) + sumOfPairsScore(seqs[0].substring(i-1,i), "-", "-", "-"));
                            maxList.add(table.getEntry(i,j-1,0,l) + sumOfPairsScore("-", seqs[1].substring(j-1,j), "-", "-"));
                            maxList.add(table.getEntry(i,j,0,l-1) + sumOfPairsScore("-", "-", "-", seqs[3].substring(l-1,l)));
                            table.setScore(i, j, 0, l, max(maxList));
                        }
                    }
                    for (int k = 1; k < table.getZ(); k++){
                        for (int l = 1; l < table.getA(); l++){
                            maxList.clear();
                            maxList.add(table.getEntry(i-1,0, k-1,l-1) + sumOfPairsScore(seqs[0].substring(i-1,i),
                                    "-", seqs[2].substring(k-1,k), seqs[3].substring(l-1,l)));
                            maxList.add(table.getEntry(i-1,0,k-1,l) + sumOfPairsScore(seqs[0].substring(i-1,i),
                                    "-", seqs[2].substring(k-1,k), "-"));
                            maxList.add(table.getEntry(i-1,0,k,l-1) + sumOfPairsScore(seqs[0].substring(i-1,i),
                                    "-", "-", seqs[3].substring(l-1,l)));
                            maxList.add(table.getEntry(i,0,k-1,l-1) + sumOfPairsScore("-", "-", seqs[2].substring(k-1,k),
                                    seqs[3].substring(l-1,l)));
                            maxList.add(table.getEntry(i-1,k,0,l) + sumOfPairsScore(seqs[0].substring(i-1,i), "-", "-", "-"));
                            maxList.add(table.getEntry(i,0,k-1,l) + sumOfPairsScore("-", "-", seqs[2].substring(k-1,k), "-"));
                            maxList.add(table.getEntry(i,0,k,l-1) + sumOfPairsScore("-", "-", "-", seqs[3].substring(l-1,l)));
                            table.setScore(i, 0, k, l, max(maxList));
                        }
                    }
                }
                for (int j = 1; j < table.getY(); j++){
                    for (int k = 1; k < table.getZ(); k++){
                        for (int l = 1; l < table.getA(); l++){
                            maxList.clear();
                            maxList.add(table.getEntry(0, j-1, k-1, l-1) + sumOfPairsScore("-", seqs[1].substring(j-1,j),
                                    seqs[2].substring(k-1,k), seqs[3].substring(l-1, l)));
                            maxList.add(table.getEntry(0, j-1, k-1, l) + sumOfPairsScore("-", seqs[1].substring(j-1,j),
                                    seqs[2].substring(l-1,l), "-"));
                            maxList.add(table.getEntry(0, j-1, k, l-1) + sumOfPairsScore("-", seqs[1].substring(j-1,j),
                                    "-", seqs[3].substring(l-1,l)));
                            maxList.add(table.getEntry(0, j, k-1, l-1) + sumOfPairsScore("-", "-",
                                    seqs[2].substring(k-1,k), seqs[3].substring(l-1,l)));
                            maxList.add(table.getEntry(0, j-1, k, l) + sumOfPairsScore("-", seqs[1].substring(j-1,j),
                                    "-", "-"));
                            maxList.add(table.getEntry(0, j, k-1, l) + sumOfPairsScore("-", "-",
                                    seqs[2].substring(k-1,k), "-"));
                            maxList.add(table.getEntry(0, j, k, l-1) + sumOfPairsScore("-", "-", "-",
                                    seqs[3].substring(l-1,l)));
                            table.setScore(0, j, k, l, max(maxList));
                        }
                    }
                }
                break;
        }
    }

    private void calcScores(){
        ArrayList<Double> maxList = new ArrayList<>();
        switch (this.dim){
            case TWO:
                for (int i = 1; i < table.getX(); i++){
                    for (int j = 1; j < table.getY(); j++){
                        maxList.clear();
                        maxList.add(table.getEntry(i-1, j-1) + algebra.getScore(seqs[0].substring(i-1, i),
                                seqs[1].substring(j-1,j))); //Match
                        maxList.add(table.getEntry(i-1, j) + generalContainer.gapOpen); //Insertion
                        maxList.add(table.getEntry(i, j-1) + generalContainer.gapOpen); //Deletion
                        table.setScore(i, j, max(maxList));
                    }
                }
                break;
            case THREE:
                for (int i = 1; i < table.getX(); i++){
                    for (int j = 1; j < table.getY(); j++){
                        for (int k = 1; k < table.getZ(); k++){
                            maxList.clear();
                            maxList.add(table.getEntry(i-1,j-1,k-1) + sumOfPairsScore(seqs[0].substring(i-1,i),
                                    seqs[1].substring(j-1,j), seqs[2].substring(k-1,k)));
                            maxList.add(table.getEntry(i-1,j-1,k) + sumOfPairsScore(seqs[0].substring(i-1,i),
                                    seqs[1].substring(j-1,j), "-"));
                            maxList.add(table.getEntry(i-1,j,k-1) + sumOfPairsScore(seqs[0].substring(i-1,i),
                                    "-", seqs[2].substring(k-1,k)));
                            maxList.add(table.getEntry(i,j-1,k-1) + sumOfPairsScore("-", seqs[1].substring(j-1,j),
                                    seqs[2].substring(k-1,k)));
                            maxList.add(table.getEntry(i-1,j,k) + sumOfPairsScore(seqs[0].substring(i-1,i), "-", "-"));
                            maxList.add(table.getEntry(i,j-1,k) + sumOfPairsScore("-", seqs[1].substring(j-1,j), "-"));
                            maxList.add(table.getEntry(i,j,k-1) + sumOfPairsScore("-", "-", seqs[2].substring(k-1,k)));
                            table.setScore(i ,j ,k, max(maxList));
                        }
                    }
                }
                break;
            case FOUR:
                for (int i = 1; i < table.getX(); i++){
                    for (int j = 1; j < table.getY(); j++){
                        for (int k = 1; k < table.getZ(); k++){
                            for (int l = 1; l < table.getA(); l++){
                                maxList.clear();
                                maxList.add(table.getEntry(i-1,j-1,k-1,l-1) + sumOfPairsScore(seqs[0].substring(i-1,i),
                                        seqs[1].substring(j-1,j), seqs[2].substring(k-1,k), seqs[3].substring(l-1,l)));
                                maxList.add(table.getEntry(i-1,j-1,k-1,l) + sumOfPairsScore(seqs[0].substring(i-1,i),
                                        seqs[1].substring(j-1,j), seqs[2].substring(k-1,k), "-"));
                                maxList.add(table.getEntry(i-1,j-1,k,l-1) + sumOfPairsScore(seqs[0].substring(i-1,i),
                                        seqs[1].substring(j-1,j), "-", seqs[3].substring(l-1,l)));
                                maxList.add(table.getEntry(i-1,j,k-1,l-1) + sumOfPairsScore(seqs[0].substring(i-1,i),
                                        "-", seqs[2].substring(k-1,k), seqs[3].substring(l-1,l)));
                                maxList.add(table.getEntry(i,j-1,k-1,l-1) + sumOfPairsScore("-",
                                        seqs[1].substring(j-1,j), seqs[2].substring(k-1,k), seqs[3].substring(l-1,l)));
                                maxList.add(table.getEntry(i-1,j-1,k,l) + sumOfPairsScore(seqs[0].substring(i-1,i),
                                        seqs[1].substring(j-1,j), "-", "-"));
                                maxList.add(table.getEntry(i-1,j,k-1,l) + sumOfPairsScore(seqs[0].substring(i-1,i),
                                        "-", seqs[2].substring(k-1,k), "-"));
                                maxList.add(table.getEntry(i,j-1,k-1,l) + sumOfPairsScore("-",
                                        seqs[1].substring(j-1,j), seqs[2].substring(k-1,k), "-"));
                                maxList.add(table.getEntry(i-1,j,k,l-1) + sumOfPairsScore(seqs[0].substring(i-1,i),
                                        "-", "-", seqs[3].substring(l-1,l)));
                                maxList.add(table.getEntry(i,j-1,k,l-1) + sumOfPairsScore("-",
                                        seqs[1].substring(j-1,j), "-", seqs[3].substring(l-1,l)));
                                maxList.add(table.getEntry(i,j,k-1,l-1) + sumOfPairsScore("-",
                                        "-", seqs[2].substring(k-1,k), seqs[3].substring(l-1,l)));
                                maxList.add(table.getEntry(i-1,j,k,l) + sumOfPairsScore(seqs[0].substring(i-1,i),
                                        "-", "-", "-"));
                                maxList.add(table.getEntry(i,j-1,k,l) + sumOfPairsScore("-",
                                        seqs[1].substring(j-1,j), "-", "-"));
                                maxList.add(table.getEntry(i,j,k-1,l) + sumOfPairsScore("-",
                                        "-", seqs[2].substring(k-1,k),"-"));
                                maxList.add(table.getEntry(i,j,k,l-1) + sumOfPairsScore("-",
                                        "-", "-", seqs[3].substring(l-1,l)));
                            }
                        }
                    }
                }
                break;
        }
    }

    private void tableToString(){
        if(this.dim.equals(generalContainer.Dimension.TWO)) {
            for (int i = 0; i < table.getX(); i++) {
                for (int j = 0; j < table.getY(); j++) {
                    System.out.print(table.getEntry(i, j) + "\t");
                }
                System.out.println();
            }
        }
        else if (this.dim.equals(generalContainer.Dimension.THREE)){
            for (int k = 0; k < table.getZ(); k++){
                if(k == 0)
                    System.out.print("-\t-");
                else
                    System.out.print(seqs[2].substring(k-1,k) + "\t-");
                for (int i = 1; i < table.getX(); i++){
                    System.out.print("\t" + seqs[0].substring(i-1,i));
                }
                System.out.println();
                for (int j = 0; j  < table.getY(); j++){
                    if (j == 0)
                        System.out.print("-");
                    else
                        System.out.print(seqs[1].substring(j-1,j));
                    for (int i = 0; i < table.getX(); i++){
                        System.out.print("\t" + table.getEntry(i,j,k));
                    }
                    System.out.println();
                }
                System.out.println();
            }
        }
    }

    private void backtracking() {
        ArrayList <String> alnSeq1 = new ArrayList<String>() ;
        ArrayList<String> alnSeq2 = new ArrayList<String>();
        ArrayList <String> alnSeq3 = new ArrayList<String>() ;
        ArrayList <String> alnSeq4 = new ArrayList<String>();
        int i = table.getX() - 1;
        int j = table.getY() - 1;
        int k = table.getZ() - 1;
        int l = table.getA() - 1;
        switch (this.dim) {
            case TWO:
                while (i > 0 || j > 0) {
                    if (i > 0 && j > 0) {
                        if (table.getEntry(i - 1, j - 1) + algebra.getScore(seqs[0].substring(i - 1, i),
                                seqs[1].substring(j - 1, j)) == table.getEntry(i, j)) {
                            alnSeq1.add(seqs[0].substring(i - 1, i));
                            alnSeq2.add(seqs[1].substring(j - 1, j));
                            i = i - 1;
                            j = j - 1;
                        } else if (table.getEntry(i - 1, j) + generalContainer.gapOpen == table.getEntry(i, j)) {
                            alnSeq1.add(seqs[0].substring(i - 1, i));
                            alnSeq2.add("-");
                            i = i - 1;
                        } else {
                            alnSeq1.add("-");
                            alnSeq2.add(seqs[1].substring(j - 1, j));
                            j = j - 1;
                        }
                    } else {
                        if (j == 0) {
                            alnSeq1.add(seqs[0].substring(i - 1, i));
                            alnSeq2.add("-");
                            i = i - 1;
                        } else {
                            alnSeq1.add("-");
                            alnSeq2.add(seqs[1].substring(j - 1, j));
                            j = j - 1;
                        }
                    }
                }

                for (int a = alnSeq1.size() - 1; a >= 0; a--) {
                    System.out.print("  " + alnSeq1.get(a));
                }
                System.out.println();
                for (int a = alnSeq2.size() - 1; a >= 0; a--) {
                    System.out.print("  " + alnSeq2.get(a));
                }
                break;

            case THREE:
                while (i > 0 || j > 0 || k > 0) {
                    if (i > 0 && j > 0 && k > 0) {
                        if (table.getEntry(i - 1, j - 1, k - 1) +
                                sumOfPairsScore(seqs[0].substring(i - 1, i), seqs[1].substring(j - 1, j), seqs[2].substring(k - 1, k))
                                == table.getEntry(i, j, k)) {
                            alnSeq1.add(seqs[0].substring(i - 1, i));
                            alnSeq2.add(seqs[1].substring(j - 1, j));
                            alnSeq3.add(seqs[2].substring(k - 1, k));
                            i = i - 1;
                            j = j - 1;
                            k = k - 1;
                        } else if (table.getEntry(i - 1, j - 1, k) +
                                sumOfPairsScore(seqs[0].substring(i - 1, i), seqs[1].substring(j - 1, j), "-")
                                == table.getEntry(i, j, k)) {
                            alnSeq1.add(seqs[0].substring(i - 1, i));
                            alnSeq2.add(seqs[1].substring(j - 1, j));
                            alnSeq3.add("-");
                            i = i - 1;
                            j = j - 1;
                        } else if (table.getEntry(i - 1, j, k - 1) +
                                sumOfPairsScore(seqs[0].substring(i - 1, i), "-", seqs[2].substring(k - 1, k))
                                == table.getEntry(i, j, k)) {
                            alnSeq1.add(seqs[0].substring(i - 1, i));
                            alnSeq2.add("-");
                            alnSeq3.add(seqs[2].substring(k - 1, k));
                            i = i - 1;
                            k = k - 1;
                        } else if (table.getEntry(i, j - 1, k - 1) +
                                sumOfPairsScore("-", seqs[1].substring(j - 1, j), seqs[2].substring(k - 1, k))
                                == table.getEntry(i, j, k)) {
                            alnSeq1.add("-");
                            alnSeq2.add(seqs[1].substring(j - 1, j));
                            alnSeq3.add(seqs[2].substring(k - 1, k));
                            j = j - 1;
                            k = k - 1;
                        } else if (table.getEntry(i - 1, j, k) + sumOfPairsScore(seqs[0].substring(i - 1, i), "-", "-")
                                == table.getEntry(i, j, k)) {
                            alnSeq1.add(seqs[0].substring(i - 1, i));
                            alnSeq2.add("-");
                            alnSeq3.add("-");
                            i = i - 1;
                        } else if (table.getEntry(i, j - 1, k) + sumOfPairsScore("-", seqs[1].substring(j - 1, j), "-")
                                == table.getEntry(i, j, k)) {
                            alnSeq1.add("-");
                            alnSeq2.add(seqs[1].substring(j - 1, j));
                            alnSeq3.add("-");
                            j = j - 1;
                        } else {
                            alnSeq1.add("-");
                            alnSeq2.add("-");
                            alnSeq3.add(seqs[2].substring(k - 1, k));
                            k = k - 1;
                        }
                    } else if (k == 0) {
                        if (i > 0 && j > 0) {
                            if (table.getEntry(i - 1, j - 1, k) +
                                    sumOfPairsScore(seqs[0].substring(i - 1, i), seqs[1].substring(j - 1, j), "-")
                                    == table.getEntry(i, j, k)) {
                                alnSeq1.add(seqs[0].substring(i - 1, i));
                                alnSeq2.add(seqs[1].substring(j - 1, j));
                                alnSeq3.add("-");
                                i = i - 1;
                                j = j - 1;
                            }
                        } else if (i > 0) {
                            if (table.getEntry(i - 1, j, k) + 2 * generalContainer.gapOpen == table.getEntry(i, j, k)) {
                                alnSeq1.add(seqs[0].substring(i - 1, i));
                                alnSeq2.add("-");
                                alnSeq3.add("-");
                                i = i - 1;
                            }
                        } else {
                            alnSeq1.add("-");
                            alnSeq2.add(seqs[1].substring(j - 1, j));
                            alnSeq3.add("-");
                            j = j - 1;
                        }
                    } else if (j == 0) {
                        if (i > 0) {
                            if (table.getEntry(i - 1, j, k - 1) +
                                    sumOfPairsScore(seqs[0].substring(i - 1, i), "-", seqs[2].substring(k - 1, k))
                                    == table.getEntry(i, j, k)) {
                                alnSeq1.add(seqs[0].substring(i - 1, i));
                                alnSeq2.add("-");
                                alnSeq3.add(seqs[2].substring(k - 1, k));
                                i = i - 1;
                                k = k - 1;
                            }
                        } else {
                            alnSeq1.add("-");
                            alnSeq2.add("-");
                            alnSeq3.add(seqs[2].substring(k - 1, k));
                            k = k - 1;
                        }
                    } else {
                        alnSeq1.add("-");
                        alnSeq2.add(seqs[1].substring(j - 1, j));
                        alnSeq3.add(seqs[2].substring(k - 1, k));
                        j = j - 1;
                        k = k - 1;
                    }
                }

                for (int a = alnSeq1.size() - 1; a >= 0; a--) {
                    System.out.print("  " + alnSeq1.get(a));
                }
                System.out.println();
                for (int a = alnSeq2.size() - 1; a >= 0; a--) {
                    System.out.print("  " + alnSeq2.get(a));
                }
                System.out.println();
                for (int a = alnSeq3.size() - 1; a >= 0; a--) {
                    System.out.print("  " + alnSeq3.get(a));
                }
                break;

            case FOUR:
                while (i > 0 || j > 0 || k > 0 || l > 0) {
                    if (i > 0 && j > 0 && k > 0 && l > 0) {
                        if (table.getEntry(i, j, k, l) == table.getEntry(i - 1, j - 1, k - 1, l - 1) +
                            sumOfPairsScore(seqs[0].substring(i - 1, i), seqs[1].substring(j - 1, j),
                                    seqs[2].substring(k - 1, k), seqs[3].substring(l - 1, l))) {
                            alnSeq1.add(seqs[0].substring(i - 1, i));
                            alnSeq2.add(seqs[1].substring(j - 1, j));
                            alnSeq3.add(seqs[2].substring(k - 1, k));
                            alnSeq4.add(seqs[3].substring(l - 1, l));
                            i = i - 1;
                            j = j - 1;
                            k = k - 1;
                            l = l - 1;
                        } else if (table.getEntry(i, j, k, l) == table.getEntry(i - 1, j - 1, k - 1, l) +
                            sumOfPairsScore(seqs[0].substring(i - 1, i), seqs[1].substring(j - 1, j),
                                    seqs[2].substring(k - 1, k), "-")) {
                        alnSeq1.add(seqs[0].substring(i - 1, i));
                        alnSeq2.add(seqs[1].substring(j - 1, j));
                        alnSeq3.add(seqs[2].substring(k - 1, k));
                        alnSeq4.add("-");
                        i = i - 1;
                        j = j - 1;
                        k = k - 1;
                    } else if (table.getEntry(i, j, k, l) == table.getEntry(i - 1, j - 1, k, l - 1) +
                            sumOfPairsScore(seqs[0].substring(i - 1, i), seqs[1].substring(j - 1, j),
                                    "-", seqs[3].substring(l - 1, l))) {
                        alnSeq1.add(seqs[0].substring(i - 1, i));
                        alnSeq2.add(seqs[1].substring(j - 1, j));
                        alnSeq3.add("-");
                        alnSeq4.add(seqs[3].substring(l - 1, l));
                        i = i - 1;
                        j = j - 1;
                        l = l - 1;
                    } else if (table.getEntry(i, j, k, l) == table.getEntry(i - 1, j, k - 1, l - 1) +
                            sumOfPairsScore(seqs[0].substring(i - 1, i), "-",
                                    seqs[2].substring(k - 1, k), seqs[3].substring(l - 1, l))) {
                        alnSeq1.add(seqs[0].substring(i - 1, i));
                        alnSeq2.add("-");
                        alnSeq3.add(seqs[2].substring(k - 1, k));
                        alnSeq4.add(seqs[3].substring(l - 1, l));
                        i = i - 1;
                        k = k - 1;
                        l = l - 1;
                    } else if (table.getEntry(i, j, k, l) == table.getEntry(i, j - 1, k - 1, l - 1) +
                            sumOfPairsScore("-", seqs[1].substring(j - 1, j),
                                    seqs[2].substring(k - 1, k), seqs[3].substring(l - 1, l))) {
                        alnSeq1.add("-");
                        alnSeq2.add(seqs[1].substring(j - 1, j));
                        alnSeq3.add(seqs[2].substring(k - 1, k));
                        alnSeq4.add(seqs[3].substring(l - 1, l));
                        j = j - 1;
                        k = k - 1;
                        l = l - 1;
                    } else if (table.getEntry(i, j, k, l) == table.getEntry(i - 1, j - 1, k, l) +
                            sumOfPairsScore(seqs[0].substring(i - 1, i), seqs[1].substring(j - 1, j),
                                    "-", "-")) {
                        alnSeq1.add(seqs[0].substring(i - 1, i));
                        alnSeq2.add(seqs[1].substring(j - 1, j));
                        alnSeq3.add("-");
                        alnSeq4.add("-");
                        i = i - 1;
                        j = j - 1;
                    } else if (table.getEntry(i, j, k, l) == table.getEntry(i - 1, j, k - 1, l) +
                            sumOfPairsScore(seqs[0].substring(i - 1, i), "-",
                                    seqs[2].substring(k - 1, k), "-")) {
                        alnSeq1.add(seqs[0].substring(i - 1, i));
                        alnSeq2.add("-");
                        alnSeq3.add(seqs[2].substring(k - 1, k));
                        alnSeq4.add("-");
                        i = i - 1;
                        k = k - 1;
                    } else if (table.getEntry(i, j, k, l) == table.getEntry(i, j - 1, k - 1, l) +
                            sumOfPairsScore("-", seqs[1].substring(j - 1, j),
                                    seqs[2].substring(k - 1, k), "-")) {
                        alnSeq1.add("-");
                        alnSeq2.add(seqs[1].substring(j - 1, j));
                        alnSeq3.add(seqs[2].substring(k - 1, k));
                        alnSeq4.add("-");
                        j = j - 1;
                        k = k - 1;
                    } else if (table.getEntry(i, j, k, l) == table.getEntry(i - 1, j - 1, k, l - 1) +
                            sumOfPairsScore(seqs[0].substring(i - 1, i), "-",
                                    "-", seqs[3].substring(l - 1, l))) {
                        alnSeq1.add(seqs[0].substring(i - 1, i));
                        alnSeq2.add("-");
                        alnSeq3.add("-");
                        alnSeq4.add(seqs[3].substring(l - 1, l));
                        i = i - 1;
                        l = l - 1;
                    } else if (table.getEntry(i, j, k, l) == table.getEntry(i - 1, j - 1, k, l - 1) +
                            sumOfPairsScore("-", seqs[1].substring(j - 1, j),
                                    "-", seqs[3].substring(l - 1, l))) {
                        alnSeq1.add("-");
                        alnSeq2.add(seqs[1].substring(j - 1, j));
                        alnSeq3.add("-");
                        alnSeq4.add(seqs[3].substring(l - 1, l));
                        j = j - 1;
                        l = l - 1;
                    } else if (table.getEntry(i, j, k, l) == table.getEntry(i - 1, j, k - 1, l - 1) +
                            sumOfPairsScore("-", "-",
                                    seqs[2].substring(k - 1, k), seqs[3].substring(l - 1, l))) {
                        alnSeq1.add("-");
                        alnSeq2.add("-");
                        alnSeq3.add(seqs[2].substring(k - 1, k));
                        alnSeq4.add(seqs[3].substring(l - 1, l));
                        k = k - 1;
                        l = l - 1;
                    } else if (table.getEntry(i, j, k, l) == table.getEntry(i - 1, j - 1, k - 1, l) +
                            sumOfPairsScore(seqs[0].substring(i - 1, i), "-",
                                    "-", "-")) {
                        alnSeq1.add(seqs[0].substring(i - 1, i));
                        alnSeq2.add("-");
                        alnSeq3.add("-");
                        alnSeq4.add("-");
                        i = i - 1;
                    } else if (table.getEntry(i, j, k, l) == table.getEntry(i - 1, j - 1, k - 1, l) +
                            sumOfPairsScore("-", seqs[1].substring(j - 1, j),
                                    "-", "-")) {
                        alnSeq1.add("-");
                        alnSeq2.add(seqs[1].substring(j - 1, j));
                        alnSeq3.add("-");
                        alnSeq4.add("-");
                        j = j - 1;
                    } else if (table.getEntry(i, j, k, l) == table.getEntry(i - 1, j, k - 1, l - 1) +
                            sumOfPairsScore("-", "-", "-", seqs[3].substring(l - 1, l))) {
                        alnSeq1.add("-");
                        alnSeq2.add("-");
                        alnSeq3.add("-");
                        alnSeq4.add(seqs[3].substring(l - 1, l));
                        l = l - 1;
                    }
                }
                else if (l == 0) {
                        if (i > 0 && j > 0 && k > 0) {
                            if (table.getEntry(i, j, k, l) == table.getEntry(i - 1, j - 1, k - 1, l) +
                                    sumOfPairsScore(seqs[0].substring(i - 1, i), seqs[1].substring(j - 1, j),
                                            seqs[2].substring(k - 1, k), "-")) {
                                alnSeq1.add(seqs[0].substring(i - 1, i));
                                alnSeq2.add(seqs[1].substring(j - 1, j));
                                alnSeq3.add(seqs[2].substring(k - 1, k));
                                alnSeq4.add("-");
                                i = i - 1;
                                j = j - 1;
                                k = k - 1;
                            } else if (table.getEntry(i, j, k, l) == table.getEntry(i - 1, j - 1, k, l) +
                                    sumOfPairsScore(seqs[0].substring(i - 1, i), seqs[1].substring(j - 1, j),
                                            "-", "-")) {
                                alnSeq1.add(seqs[0].substring(i - 1, i));
                                alnSeq2.add(seqs[1].substring(j - 1, j));
                                alnSeq3.add("-");
                                alnSeq4.add("-");
                                i = i - 1;
                                j = j - 1;
                            } else if (table.getEntry(i, j, k, l) == table.getEntry(i - 1, j, k - 1, l) +
                                    sumOfPairsScore(seqs[0].substring(i - 1, i), "-",
                                            seqs[2].substring(k - 1, k), "-")) {
                                alnSeq1.add(seqs[0].substring(i - 1, i));
                                alnSeq2.add("-");
                                alnSeq3.add(seqs[2].substring(k - 1, k));
                                alnSeq4.add("-");
                                i = i - 1;
                                k = k - 1;
                            } else if (table.getEntry(i, j, k, l) == table.getEntry(i, j - 1, k, l) +
                                    sumOfPairsScore("-", seqs[1].substring(j - 1, j),
                                            seqs[2].substring(k - 1, k), "-")) {
                                alnSeq1.add("-");
                                alnSeq2.add(seqs[1].substring(j - 1, j));
                                alnSeq3.add(seqs[2].substring(k - 1, k));
                                alnSeq4.add("-");
                                j = j - 1;
                                k = k - 1;
                            } else if (table.getEntry(i, j, k, l) == table.getEntry(i - 1, j, k, l) +
                                    sumOfPairsScore(seqs[0].substring(i - 1, i), "-",
                                            "-", "-")) {
                                alnSeq1.add(seqs[0].substring(i - 1, i));
                                alnSeq2.add("-");
                                alnSeq3.add("-");
                                alnSeq4.add("-");
                                i = i - 1;
                            } else if (table.getEntry(i, j, k, l) == table.getEntry(i, j - 1, k, l) +
                                    sumOfPairsScore("-", seqs[1].substring(j - 1, j),
                                            "-", "-")) {
                                alnSeq1.add("-");
                                alnSeq2.add(seqs[1].substring(j - 1, j));
                                alnSeq3.add("-");
                                alnSeq4.add("-");
                                j = j - 1;
                            } else if (table.getEntry(i, j, k, l) == table.getEntry(i, j, k - 1, l) +
                                    sumOfPairsScore("-", "-",
                                            seqs[2].substring(k - 1, k), "-")) {
                                alnSeq1.add("-");
                                alnSeq2.add("-");
                                alnSeq3.add(seqs[2].substring(k - 1, k));
                                alnSeq4.add("-");
                                k = k - 1;
                            }
                        } else if (k == 0) {
                            if (i > 0 && j > 0) {
                                if (table.getEntry(i, j, k, l) == table.getEntry(i - 1, j - 1, k - 1, l) +
                                        sumOfPairsScore(seqs[0].substring(i - 1, i), seqs[1].substring(j - 1, j),
                                                "-", "-")) {
                                    alnSeq1.add(seqs[0].substring(i - 1, i));
                                    alnSeq2.add(seqs[1].substring(j - 1, j));
                                    alnSeq3.add("-");
                                    alnSeq4.add("-");
                                    i = i - 1;
                                    j = j - 1;
                                } else if (table.getEntry(i, j, k, l) == table.getEntry(i - 1, j, k, l) +
                                        sumOfPairsScore(seqs[0].substring(i - 1, i), "-", "-", "-")) {
                                    alnSeq1.add(seqs[0].substring(i - 1, i));
                                    alnSeq2.add("-");
                                    alnSeq3.add("-");
                                    alnSeq4.add("-");
                                    i = i - 1;
                                } else if (table.getEntry(i, j, k, l) == table.getEntry(i, j - 1, k, l) +
                                        sumOfPairsScore("-", seqs[1].substring(j - 1, j),
                                                "-", "-")) {
                                    alnSeq1.add("-");
                                    alnSeq2.add(seqs[1].substring(j - 1, j));
                                    alnSeq3.add("-");
                                    alnSeq4.add("-");
                                    j = j - 1;
                                }
                            } else if (j == 0) {
                                // i>0, j,k,l=0; if i was 0, not in while loop any more
                                if (table.getEntry(i, j, k, l) == table.getEntry(i - 1, j, k, l) +
                                        sumOfPairsScore(seqs[0].substring(i - 1, i), "-", "-", "-")) {
                                    alnSeq1.add(seqs[0].substring(i - 1, i));
                                    alnSeq2.add("-");
                                    alnSeq3.add("-");
                                    alnSeq4.add("-");
                                    i = i - 1;
                                }
                            }
                            //i,k,l = 0; j>0
                            else {
                                alnSeq1.add("-");
                                alnSeq2.add(seqs[1].substring(j - 1, j));
                                alnSeq3.add("-");
                                alnSeq4.add("-");
                                j = j - 1;
                            }
                        }
                        //k > 0! l still 0
                        else if (j == 0) {
                            if(i > 0){
                                if (table.getEntry(i, j, k, l) == table.getEntry(i-1, j, k-1, l) +
                                        sumOfPairsScore(seqs[0].substring(i-1,i), "-", seqs[2].substring(k-1,k), "-")){
                                    alnSeq1.add(seqs[0].substring(i-1,i));
                                    alnSeq2.add("-");
                                    alnSeq3.add(seqs[2].substring(k-1,k));
                                    alnSeq4.add("-");
                                    i = i-1;
                                    k = k-1;
                                }
                                else if (table.getEntry(i, j, k, l) == table.getEntry(i-1, j,k,l) +
                                        sumOfPairsScore(seqs[0].substring(i-1,i), "-", "-", "-")){
                                    alnSeq1.add(seqs[0].substring(i-1,i));
                                    alnSeq2.add("-");
                                    alnSeq3.add("-");
                                    alnSeq4.add("-");
                                    i = i-1;
                                }
                                else {
                                    alnSeq1.add("-");
                                    alnSeq2.add("-");
                                    alnSeq3.add(seqs[2].substring(k-1,k));
                                    alnSeq4.add("-");
                                    k = k-1;
                                }
                            }
                            //i,k,l = 0; k >0
                            else {
                                alnSeq1.add("-");
                                alnSeq2.add("-");
                                alnSeq3.add(seqs[2].substring(k-1,k));
                                alnSeq4.add("-");
                                k = k-1;
                            }
                        }
                        //i,l=0, k,j> 0!
                        else {
                            //TODO
                        }
                    }
                }
                break;

        }
    }

}
