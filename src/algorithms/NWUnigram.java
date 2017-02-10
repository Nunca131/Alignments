package algorithms;

import container.AlignmentTable;
import container.WordDatabase;
import container.generalContainer;

import java.util.ArrayList;

/**
 * Created by nancy on 01.11.16.
 */
public class NWUnigram {

    private container.AlignmentTable table;
    //private String[] seqs;
    private container.SimpleScoreAlgebra algebra;
    private generalContainer.Dimension dim;

    private WordDatabase wordDatabase1;
    private WordDatabase wordDatabase2;
    private WordDatabase wordDatabase3;
    private WordDatabase wordDatabase4;

    //TODO backtracking for four sequences!!!

    public NWUnigram(container.SimpleScoreAlgebra algebra, generalContainer.Dimension dim,
                     ArrayList<WordDatabase> databaseList) {
        this.dim = dim;
        switch (this.dim) {
            case TWO:
                this.table = new container.AlignmentTable(30, 30);
                wordDatabase1 = databaseList.get(0);
                wordDatabase2 = databaseList.get(1);
                break;
            case THREE:
                this.table = new container.AlignmentTable(30, 30, 30);
                wordDatabase1 = databaseList.get(0);
                wordDatabase2 = databaseList.get(1);
                wordDatabase3 = databaseList.get(2);
                break;
            case FOUR:
                this.table = new container.AlignmentTable(30, 30, 30, 30);
                wordDatabase1 = databaseList.get(0);
                wordDatabase2 = databaseList.get(1);
                wordDatabase3 = databaseList.get(2);
                wordDatabase4 = databaseList.get(3);
                break;
        }
        //this.seqs = seqs;
        this.algebra = algebra;
    }

    public void align(String id1, String id2){
        initialize(wordDatabase1.getWordByID(id1), wordDatabase2.getWordByID(id2));
        calcScores(wordDatabase1.getWordByID(id1), wordDatabase2.getWordByID(id2));
        backtracking(wordDatabase1.getWordByID(id1), wordDatabase2.getWordByID(id2));
    }

    public void align(String id1, String id2, String id3){
        initialize(wordDatabase1.getWordByID(id1), wordDatabase2.getWordByID(id2), wordDatabase3.getWordByID(id3));
        calcScores(wordDatabase1.getWordByID(id1), wordDatabase2.getWordByID(id2), wordDatabase3.getWordByID(id3));
        backtracking(wordDatabase1.getWordByID(id1), wordDatabase2.getWordByID(id2), wordDatabase3.getWordByID(id3));
    }

    public void align(String id1, String id2, String id3, String id4){
        initialize(wordDatabase1.getWordByID(id1), wordDatabase2.getWordByID(id2), wordDatabase3.getWordByID(id3),
                wordDatabase4.getWordByID(id4));
        calcScores(wordDatabase1.getWordByID(id1), wordDatabase2.getWordByID(id2), wordDatabase3.getWordByID(id3),
                wordDatabase4.getWordByID(id4));
        backtracking(wordDatabase1.getWordByID(id1), wordDatabase2.getWordByID(id2), wordDatabase3.getWordByID(id3),
                wordDatabase4.getWordByID(id4));
    }

    private double max(ArrayList<Double> list) {
        double max = -10000.;
        if (list.size() == 1)
            System.err.println("max call on singleton");
        for (int i = 1; i < list.size(); i++) {
            if (list.get(i - 1) > list.get(i))
                max = Math.max(list.get(i - 1), max);

            else
                max = Math.max(max, list.get(i));
        }

        return max;
    }

    private double sumOfPairsScore(String a, String b, String c) {
        double sps = 0.;

        sps += algebra.getScore(a, b);
        sps += algebra.getScore(a, c);
        sps += algebra.getScore(b, c);

        return sps;
    }

    private double sumOfPairsScore(String a, String b, String c, String d) {
        double sps = 0.;

        sps += algebra.getScore(a, b);
        sps += algebra.getScore(a, c);
        sps += algebra.getScore(a, d);
        sps += algebra.getScore(b, c);
        sps += algebra.getScore(b, d);
        sps += algebra.getScore(c, d);

        return sps;
    }

    private void initialize(String word1, String word2) {
        ArrayList<Double> maxList = new ArrayList<>();
        table.setScore(0, 0, 0.);
        if (word1.length() > table.getX()){
            this.table = new AlignmentTable(word1.length(), this.table.getY());
        }
        if (word2.length() > table.getY()){
            this.table = new AlignmentTable(this.table.getX(), word2.length());
        }
        for (int i = 1; i < word1.length(); i++) {
            table.setScore(i, 0, algebra.getScore(word1.charAt(i+1)+"", "-"));
        }
        for (int j = 1; j < word2.length(); j++) {
            table.setScore(0, j, algebra.getScore("-", word2.charAt(j+1)+""));
        }
    }

    private void initialize(String word1, String word2, String word3) {
        ArrayList<Double> maxList = new ArrayList<>();
        table.setScore(0, 0, 0, 0.);
        for (int i = 1; i < table.getX(); i++) {
            table.setScore(i, 0, 0, sumOfPairsScore(word1.charAt(i+1)+"", "-", "-"));
        }
        for (int j = 1; j < table.getY(); j++) {
            table.setScore(0, j, 0, sumOfPairsScore("-",word2.charAt(j+1)+"", "-"));
        }
        for (int k = 1; k < table.getZ(); k++) {
            table.setScore(0, 0, k, sumOfPairsScore("-", "-", word3.charAt(k+1)+""));
        }
        for (int i = 1; i < table.getX(); i++) {
            for (int j = 1; j < table.getY(); j++) {
                maxList.clear();
                maxList.add(table.getEntry(i - 1, j - 1, 0) +
                        sumOfPairsScore(word1.substring(i - 1, i), word2.substring(j - 1, j), "-"));
                maxList.add(table.getEntry(i - 1, j, 0) + sumOfPairsScore(word1.substring(i - 1, i), "-", "-"));
                maxList.add(table.getEntry(i, j - 1, 0) + sumOfPairsScore("-", word2.substring(j - 1, j), "-"));
                table.setScore(i, j, 0, max(maxList));
            }
            for (int k = 1; k < table.getZ(); k++) {
                maxList.clear();
                maxList.add(table.getEntry(i - 1, 0, k - 1) +
                        sumOfPairsScore(word1.substring(i - 1, i), "-", word3.substring(k - 1, k)));
                maxList.add(table.getEntry(i - 1, 0, k) + sumOfPairsScore(word1.substring(i - 1, i), "-", "-"));
                maxList.add(table.getEntry(i, 0, k - 1) + sumOfPairsScore("-", "-", word3.substring(k - 1, k)));
                table.setScore(i, 0, k, max(maxList));
            }
        }
        for (int j = 1; j < table.getY(); j++) {
            for (int k = 1; k < table.getZ(); k++) {
                maxList.clear();
                maxList.add(table.getEntry(0, j - 1, k - 1) +
                        sumOfPairsScore("-", word2.substring(j - 1, j), word3.substring(k - 1, k)));
                maxList.add(table.getEntry(0, j - 1, k) + sumOfPairsScore("-", word2.substring(j - 1, j), "-"));
                maxList.add(table.getEntry(0, j, k - 1) + sumOfPairsScore("-", "-", word3.substring(k - 1, k)));
                table.setScore(0, j, k, max(maxList));
            }
        }
    }


    //TODO: init with sum of pairs
    private void initialize(String word1, String word2, String word3, String word4) {
        ArrayList<Double> maxList = new ArrayList<>();
        table.setScore(0, 0, 0, 0, 0.);
        for (int i = 1; i < table.getX(); i++) {
            table.setScore(i, 0, 0, 0, algebra.gapOpen * i * 3);
        }
        for (int j = 1; j < table.getY(); j++) {
            table.setScore(0, j, 0, 0, algebra.gapOpen * j * 3);
        }
        for (int k = 1; k < table.getZ(); k++) {
            table.setScore(0, 0, k, 0, algebra.gapOpen * k * 3);
        }
        for (int l = 1; l < table.getA(); l++) {
            table.setScore(0, 0, 0, l, algebra.gapOpen * l * 3);
        }
        for (int i = 1; i < table.getX(); i++) {
            for (int j = 1; j < table.getY(); j++) {
                maxList.clear();
                maxList.add(table.getEntry(i - 1, j - 1, 0, 0) +
                        sumOfPairsScore(word1.substring(i - 1, i), word2.substring(j - 1, j), "-", "-"));
                maxList.add(table.getEntry(i - 1, j, 0, 0) +
                        sumOfPairsScore(word1.substring(i - 1, i), "-", "-", "-"));
                maxList.add(table.getEntry(i, j - 1, 0, 0) +
                        sumOfPairsScore("-", word2.substring(j - 1, j), "-", "-"));
                table.setScore(i, j, 0, 0, max(maxList));
            }
            for (int k = 1; k < table.getZ(); k++) {
                maxList.clear();
                maxList.add(table.getEntry(i - 1, 0, k - 1, 0) +
                        sumOfPairsScore(word1.substring(i - 1, i), "-", word3.substring(k - 1, k), "-"));
                maxList.add(table.getEntry(i - 1, k, 0, 0) +
                        sumOfPairsScore(word1.substring(i - 1, i), "-", "-", "-"));
                maxList.add(table.getEntry(i, 0, k - 1, 0) +
                        sumOfPairsScore("-", "-", word3.substring(k - 1, k), "-"));
                table.setScore(i, 0, k, 0, max(maxList));
            }
            for (int l = 1; l < table.getZ(); l++) {
                maxList.clear();
                maxList.add(table.getEntry(i - 1, 0, 0, l - 1) +
                        sumOfPairsScore(word1.substring(i - 1, i), "-", "-", word4.substring(l - 1, l)));
                maxList.add(table.getEntry(i - 1, 0, 0, l) +
                        sumOfPairsScore(word1.substring(i - 1, i), "-", "-", "-"));
                maxList.add(table.getEntry(i, 0, 0, l - 1) +
                        sumOfPairsScore("-", "-", "-", word4.substring(l - 1, l)));
                table.setScore(i, 0, 0, l, max(maxList));
            }
        }
        for (int j = 1; j < table.getY(); j++) {
            for (int k = 1; k < table.getZ(); k++) {
                maxList.clear();
                maxList.add(table.getEntry(0, j - 1, k - 1, 0) +
                        sumOfPairsScore("-", word2.substring(j - 1, j), word3.substring(k - 1, k), "-"));
                maxList.add(table.getEntry(0, j - 1, k, 0) +
                        sumOfPairsScore("-", word1.substring(j - 1, j), "-", "-"));
                maxList.add(table.getEntry(0, j, k - 1, 0) +
                        sumOfPairsScore("-", "-", word3.substring(k - 1, k), "-"));
                table.setScore(0, j, k, 0, max(maxList));
            }
            for (int l = 1; l < table.getA(); l++) {
                maxList.clear();
                maxList.add(table.getEntry(0, j - 1, 0, l - 1) +
                        sumOfPairsScore("-", word2.substring(j - 1, j), "-", word4.substring(l - 1, l)));
                maxList.add(table.getEntry(0, j - 1, 0, l) +
                        sumOfPairsScore("-", word1.substring(j - 1, j), "-", "-"));
                maxList.add(table.getEntry(0, j, 0, l - 1) +
                        sumOfPairsScore("-", "-", "-", word4.substring(l - 1, l)));
                table.setScore(0, j, 0, l, max(maxList));
            }
        }
        for (int k = 1; k < table.getZ(); k++) {
            for (int l = 1; l < table.getA(); l++) {
                maxList.clear();
                maxList.add(table.getEntry(0, 0, k - 1, l - 1) +
                        sumOfPairsScore("-", "-", word3.substring(k - 1, k), word4.substring(l - 1, l)));
                maxList.add(table.getEntry(0, 0, k - 1, l) +
                        sumOfPairsScore("-", "-", word3.substring(k - 1, k), "-"));
                maxList.add(table.getEntry(0, 0, k, l - 1) +
                        sumOfPairsScore("-", "-", "-", word4.substring(l - 1, l)));
                table.setScore(0, 0, k, l, max(maxList));
            }
        }

        //TODO: "3-dim. Ebenen ausfuellen"
        for (int i = 1; i < table.getX(); i++) {
            for (int j = 1; j < table.getY(); j++) {
                for (int k = 1; k < table.getZ(); k++) {
                    maxList.clear();
                    maxList.add(table.getEntry(i - 1, j - 1, k - 1, 0) + sumOfPairsScore(word1.substring(i - 1, i),
                            word2.substring(j - 1, j), word3.substring(k - 1, k), "-"));
                    maxList.add(table.getEntry(i - 1, j - 1, k, 0) + sumOfPairsScore(word1.substring(i - 1, i),
                            word2.substring(j - 1, j), "-", "-"));
                    maxList.add(table.getEntry(i - 1, j, k - 1, 0) + sumOfPairsScore(word1.substring(i - 1, i),
                            "-", word3.substring(k - 1, k), "-"));
                    maxList.add(table.getEntry(i, j - 1, k - 1, 0) + sumOfPairsScore("-", word2.substring(j - 1, j),
                            word3.substring(k - 1, k), "-"));
                    maxList.add(table.getEntry(i - 1, j, k, 0) + sumOfPairsScore(word1.substring(i - 1, i), "-", "-", "-"));
                    maxList.add(table.getEntry(i, j - 1, k, 0) + sumOfPairsScore("-", word2.substring(j - 1, j), "-", "-"));
                    maxList.add(table.getEntry(i, j, k - 1, 0) + sumOfPairsScore("-", "-", word3.substring(k - 1, k), "-"));
                    table.setScore(i, j, k, max(maxList));
                }
                for (int l = 1; l < table.getA(); l++) {
                    maxList.clear();
                    maxList.add(table.getEntry(i - 1, j - 1, 0, l - 1) + sumOfPairsScore(word1.substring(i - 1, i),
                            word2.substring(j - 1, j), "-", word4.substring(l - 1, l)));
                    maxList.add(table.getEntry(i - 1, j - 1, 0, l) + sumOfPairsScore(word1.substring(i - 1, i),
                            word2.substring(j - 1, j), "-", "-"));
                    maxList.add(table.getEntry(i - 1, j, 0, l - 1) + sumOfPairsScore(word1.substring(i - 1, i),
                            "-", "-", word4.substring(l - 1, l)));
                    maxList.add(table.getEntry(i, j - 1, 0, l - 1) + sumOfPairsScore("-", word2.substring(j - 1, j),
                            "-", word4.substring(l - 1, l)));
                    maxList.add(table.getEntry(i - 1, j, 0, l) + sumOfPairsScore(word1.substring(i - 1, i), "-", "-", "-"));
                    maxList.add(table.getEntry(i, j - 1, 0, l) + sumOfPairsScore("-", word2.substring(j - 1, j), "-", "-"));
                    maxList.add(table.getEntry(i, j, 0, l - 1) + sumOfPairsScore("-", "-", "-", word4.substring(l - 1, l)));
                    table.setScore(i, j, 0, l, max(maxList));
                }
            }
            for (int k = 1; k < table.getZ(); k++) {
                for (int l = 1; l < table.getA(); l++) {
                    maxList.clear();
                    maxList.add(table.getEntry(i - 1, 0, k - 1, l - 1) + sumOfPairsScore(word1.substring(i - 1, i),
                            "-", word3.substring(k - 1, k), word4.substring(l - 1, l)));
                    maxList.add(table.getEntry(i - 1, 0, k - 1, l) + sumOfPairsScore(word1.substring(i - 1, i),
                            "-", word3.substring(k - 1, k), "-"));
                    maxList.add(table.getEntry(i - 1, 0, k, l - 1) + sumOfPairsScore(word1.substring(i - 1, i),
                            "-", "-", word4.substring(l - 1, l)));
                    maxList.add(table.getEntry(i, 0, k - 1, l - 1) + sumOfPairsScore("-", "-", word3.substring(k - 1, k),
                            word4.substring(l - 1, l)));
                    maxList.add(table.getEntry(i - 1, k, 0, l) + sumOfPairsScore(word1.substring(i - 1, i), "-", "-", "-"));
                    maxList.add(table.getEntry(i, 0, k - 1, l) + sumOfPairsScore("-", "-", word3.substring(k - 1, k), "-"));
                    maxList.add(table.getEntry(i, 0, k, l - 1) + sumOfPairsScore("-", "-", "-", word4.substring(l - 1, l)));
                    table.setScore(i, 0, k, l, max(maxList));
                }
            }
        }
        for (int j = 1; j < table.getY(); j++) {
            for (int k = 1; k < table.getZ(); k++) {
                for (int l = 1; l < table.getA(); l++) {
                    maxList.clear();
                    maxList.add(table.getEntry(0, j - 1, k - 1, l - 1) + sumOfPairsScore("-", word2.substring(j - 1, j),
                            word3.substring(k - 1, k), word4.substring(l - 1, l)));
                    maxList.add(table.getEntry(0, j - 1, k - 1, l) + sumOfPairsScore("-", word2.substring(j - 1, j),
                            word3.substring(l - 1, l), "-"));
                    maxList.add(table.getEntry(0, j - 1, k, l - 1) + sumOfPairsScore("-", word2.substring(j - 1, j),
                            "-", word4.substring(l - 1, l)));
                    maxList.add(table.getEntry(0, j, k - 1, l - 1) + sumOfPairsScore("-", "-",
                            word3.substring(k - 1, k), word4.substring(l - 1, l)));
                    maxList.add(table.getEntry(0, j - 1, k, l) + sumOfPairsScore("-", word2.substring(j - 1, j),
                            "-", "-"));
                    maxList.add(table.getEntry(0, j, k - 1, l) + sumOfPairsScore("-", "-",
                            word3.substring(k - 1, k), "-"));
                    maxList.add(table.getEntry(0, j, k, l - 1) + sumOfPairsScore("-", "-", "-",
                            word4.substring(l - 1, l)));
                    table.setScore(0, j, k, l, max(maxList));
                }
            }
        }
    }

    private void calcScores(String word1, String word2) {
        ArrayList<Double> maxList = new ArrayList<>();
        for (int i = 1; i < table.getX(); i++) {
            for (int j = 1; j < table.getY(); j++) {
                maxList.clear();
                maxList.add(table.getEntry(i - 1, j - 1) + algebra.getScore(word1.substring(i - 1, i),
                        word2.substring(j - 1, j))); //Match
                maxList.add(table.getEntry(i - 1, j) + algebra.gapOpen); //Insertion
                maxList.add(table.getEntry(i, j - 1) + algebra.gapOpen); //Deletion
                table.setScore(i, j, max(maxList));
            }
        }
    }

    private void calcScores(String word1, String word2, String word3) {
        ArrayList<Double> maxList = new ArrayList<>();
        for (int i = 1; i < table.getX(); i++) {
            for (int j = 1; j < table.getY(); j++) {
                for (int k = 1; k < table.getZ(); k++) {
                    maxList.clear();
                    maxList.add(table.getEntry(i - 1, j - 1, k - 1) + sumOfPairsScore(word1.substring(i - 1, i),
                            word2.substring(j - 1, j), word3.substring(k - 1, k)));
                    maxList.add(table.getEntry(i - 1, j - 1, k) + sumOfPairsScore(word1.substring(i - 1, i),
                            word2.substring(j - 1, j), "-"));
                    maxList.add(table.getEntry(i - 1, j, k - 1) + sumOfPairsScore(word1.substring(i - 1, i),
                            "-", word3.substring(k - 1, k)));
                    maxList.add(table.getEntry(i, j - 1, k - 1) + sumOfPairsScore("-", word2.substring(j - 1, j),
                            word3.substring(k - 1, k)));
                    maxList.add(table.getEntry(i - 1, j, k) + sumOfPairsScore(word1.substring(i - 1, i), "-", "-"));
                    maxList.add(table.getEntry(i, j - 1, k) + sumOfPairsScore("-", word2.substring(j - 1, j), "-"));
                    maxList.add(table.getEntry(i, j, k - 1) + sumOfPairsScore("-", "-", word3.substring(k - 1, k)));
                    table.setScore(i, j, k, max(maxList));
                }
            }
        }
    }

    private void calcScores(String word1, String word2, String word3, String word4) {
        ArrayList<Double> maxList = new ArrayList<>();
        for (int i = 1; i < table.getX(); i++) {
            for (int j = 1; j < table.getY(); j++) {
                for (int k = 1; k < table.getZ(); k++) {
                    for (int l = 1; l < table.getA(); l++) {
                        maxList.clear();
                        maxList.add(table.getEntry(i - 1, j - 1, k - 1, l - 1) + sumOfPairsScore(word1.substring(i - 1, i),
                                word2.substring(j - 1, j), word3.substring(k - 1, k), word4.substring(l - 1, l)));
                        maxList.add(table.getEntry(i - 1, j - 1, k - 1, l) + sumOfPairsScore(word1.substring(i - 1, i),
                                word2.substring(j - 1, j), word3.substring(k - 1, k), "-"));
                        maxList.add(table.getEntry(i - 1, j - 1, k, l - 1) + sumOfPairsScore(word1.substring(i - 1, i),
                                word2.substring(j - 1, j), "-", word4.substring(l - 1, l)));
                        maxList.add(table.getEntry(i - 1, j, k - 1, l - 1) + sumOfPairsScore(word1.substring(i - 1, i),
                                "-", word3.substring(k - 1, k), word4.substring(l - 1, l)));
                        maxList.add(table.getEntry(i, j - 1, k - 1, l - 1) + sumOfPairsScore("-",
                                word2.substring(j - 1, j), word3.substring(k - 1, k), word4.substring(l - 1, l)));
                        maxList.add(table.getEntry(i - 1, j - 1, k, l) + sumOfPairsScore(word1.substring(i - 1, i),
                                word2.substring(j - 1, j), "-", "-"));
                        maxList.add(table.getEntry(i - 1, j, k - 1, l) + sumOfPairsScore(word1.substring(i - 1, i),
                                "-", word3.substring(k - 1, k), "-"));
                        maxList.add(table.getEntry(i, j - 1, k - 1, l) + sumOfPairsScore("-",
                                word2.substring(j - 1, j), word3.substring(k - 1, k), "-"));
                        maxList.add(table.getEntry(i - 1, j, k, l - 1) + sumOfPairsScore(word1.substring(i - 1, i),
                                "-", "-", word4.substring(l - 1, l)));
                        maxList.add(table.getEntry(i, j - 1, k, l - 1) + sumOfPairsScore("-",
                                word2.substring(j - 1, j), "-", word4.substring(l - 1, l)));
                        maxList.add(table.getEntry(i, j, k - 1, l - 1) + sumOfPairsScore("-",
                                "-", word3.substring(k - 1, k), word4.substring(l - 1, l)));
                        maxList.add(table.getEntry(i - 1, j, k, l) + sumOfPairsScore(word1.substring(i - 1, i),
                                "-", "-", "-"));
                        maxList.add(table.getEntry(i, j - 1, k, l) + sumOfPairsScore("-",
                                word2.substring(j - 1, j), "-", "-"));
                        maxList.add(table.getEntry(i, j, k - 1, l) + sumOfPairsScore("-",
                                "-", word3.substring(k - 1, k), "-"));
                        maxList.add(table.getEntry(i, j, k, l - 1) + sumOfPairsScore("-",
                                "-", "-", word4.substring(l - 1, l)));
                    }
                }
            }
        }
    }


    private void tableToString() {
        for (int i = 0; i < table.getX(); i++) {
            for (int j = 0; j < table.getY(); j++) {
                System.out.print(table.getEntry(i, j) + "\t");
            }
            System.out.println();
        }
    }

    private void tableToString(String word1, String word2, String word3) {
        for (int k = 0; k < table.getZ(); k++) {
            if (k == 0)
                System.out.print("-\t-");
            else
                System.out.print(word3.substring(k - 1, k) + "\t-");
            for (int i = 1; i < table.getX(); i++) {
                System.out.print("\t" + word1.substring(i - 1, i));
            }
            System.out.println();
            for (int j = 0; j < table.getY(); j++) {
                if (j == 0)
                    System.out.print("-");
                else
                    System.out.print(word2.substring(j - 1, j));
                for (int i = 0; i < table.getX(); i++) {
                    System.out.print("\t" + table.getEntry(i, j, k));
                }
                System.out.println();
            }
            System.out.println();
        }
    }

    private void backtracking(String word1, String word2) {
        ArrayList<String> alnSeq1 = new ArrayList<>();
        ArrayList<String> alnSeq2 = new ArrayList<>();

        int i = table.getX() - 1;
        int j = table.getY() - 1;

        while (i > 0 || j > 0) {
            if (i > 0 && j > 0) {
                if (table.getEntry(i - 1, j - 1) + algebra.getScore(word1.substring(i - 1, i),
                        word2.substring(j - 1, j)) == table.getEntry(i, j)) {
                    alnSeq1.add(word1.substring(i - 1, i));
                    alnSeq2.add(word2.substring(j - 1, j));
                    i = i - 1;
                    j = j - 1;
                } else if (table.getEntry(i - 1, j) + algebra.gapOpen == table.getEntry(i, j)) {
                    alnSeq1.add(word1.substring(i - 1, i));
                    alnSeq2.add("-");
                    i = i - 1;
                } else {
                    alnSeq1.add("-");
                    alnSeq2.add(word2.substring(j - 1, j));
                    j = j - 1;
                }
            } else {
                if (j == 0) {
                    alnSeq1.add(word1.substring(i - 1, i));
                    alnSeq2.add("-");
                    i = i - 1;
                } else {
                    alnSeq1.add("-");
                    alnSeq2.add(word2.substring(j - 1, j));
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
    }

    private void backtracking(String word1, String word2, String word3) {
        ArrayList<String> alnSeq1 = new ArrayList<>();
        ArrayList<String> alnSeq2 = new ArrayList<>();
        ArrayList<String> alnSeq3 = new ArrayList<>();

        int i = table.getX() - 1;
        int j = table.getY() - 1;
        int k = table.getZ() - 1;

        while (i > 0 || j > 0 || k > 0) {
            if (i > 0 && j > 0 && k > 0) {
                if (table.getEntry(i - 1, j - 1, k - 1) +
                        sumOfPairsScore(word1.substring(i - 1, i), word2.substring(j - 1, j), word3.substring(k - 1, k))
                        == table.getEntry(i, j, k)) {
                    alnSeq1.add(word1.substring(i - 1, i));
                    alnSeq2.add(word2.substring(j - 1, j));
                    alnSeq3.add(word3.substring(k - 1, k));
                    i = i - 1;
                    j = j - 1;
                    k = k - 1;
                } else if (table.getEntry(i - 1, j - 1, k) +
                        sumOfPairsScore(word1.substring(i - 1, i), word2.substring(j - 1, j), "-")
                        == table.getEntry(i, j, k)) {
                    alnSeq1.add(word1.substring(i - 1, i));
                    alnSeq2.add(word2.substring(j - 1, j));
                    alnSeq3.add("-");
                    i = i - 1;
                    j = j - 1;
                } else if (table.getEntry(i - 1, j, k - 1) +
                        sumOfPairsScore(word1.substring(i - 1, i), "-", word3.substring(k - 1, k))
                        == table.getEntry(i, j, k)) {
                    alnSeq1.add(word1.substring(i - 1, i));
                    alnSeq2.add("-");
                    alnSeq3.add(word3.substring(k - 1, k));
                    i = i - 1;
                    k = k - 1;
                } else if (table.getEntry(i, j - 1, k - 1) +
                        sumOfPairsScore("-", word2.substring(j - 1, j), word3.substring(k - 1, k))
                        == table.getEntry(i, j, k)) {
                    alnSeq1.add("-");
                    alnSeq2.add(word2.substring(j - 1, j));
                    alnSeq3.add(word3.substring(k - 1, k));
                    j = j - 1;
                    k = k - 1;
                } else if (table.getEntry(i - 1, j, k) + sumOfPairsScore(word1.substring(i - 1, i), "-", "-")
                        == table.getEntry(i, j, k)) {
                    alnSeq1.add(word1.substring(i - 1, i));
                    alnSeq2.add("-");
                    alnSeq3.add("-");
                    i = i - 1;
                } else if (table.getEntry(i, j - 1, k) + sumOfPairsScore("-", word2.substring(j - 1, j), "-")
                        == table.getEntry(i, j, k)) {
                    alnSeq1.add("-");
                    alnSeq2.add(word2.substring(j - 1, j));
                    alnSeq3.add("-");
                    j = j - 1;
                } else {
                    alnSeq1.add("-");
                    alnSeq2.add("-");
                    alnSeq3.add(word3.substring(k - 1, k));
                    k = k - 1;
                }
            } else if (k == 0) {
                if (i > 0 && j > 0) {
                    if (table.getEntry(i - 1, j - 1, k) +
                            sumOfPairsScore(word1.substring(i - 1, i), word2.substring(j - 1, j), "-")
                            == table.getEntry(i, j, k)) {
                        alnSeq1.add(word1.substring(i - 1, i));
                        alnSeq2.add(word2.substring(j - 1, j));
                        alnSeq3.add("-");
                        i = i - 1;
                        j = j - 1;
                    }
                } else if (i > 0) {
                    if (table.getEntry(i - 1, j, k) + 2 * algebra.gapOpen == table.getEntry(i, j, k)) {
                        alnSeq1.add(word1.substring(i - 1, i));
                        alnSeq2.add("-");
                        alnSeq3.add("-");
                        i = i - 1;
                    }
                } else {
                    alnSeq1.add("-");
                    alnSeq2.add(word2.substring(j - 1, j));
                    alnSeq3.add("-");
                    j = j - 1;
                }
            } else if (j == 0) {
                if (i > 0) {
                    if (table.getEntry(i - 1, j, k - 1) +
                            sumOfPairsScore(word1.substring(i - 1, i), "-", word3.substring(k - 1, k))
                            == table.getEntry(i, j, k)) {
                        alnSeq1.add(word1.substring(i - 1, i));
                        alnSeq2.add("-");
                        alnSeq3.add(word3.substring(k - 1, k));
                        i = i - 1;
                        k = k - 1;
                    }
                } else {
                    alnSeq1.add("-");
                    alnSeq2.add("-");
                    alnSeq3.add(word3.substring(k - 1, k));
                    k = k - 1;
                }
            } else {
                alnSeq1.add("-");
                alnSeq2.add(word2.substring(j - 1, j));
                alnSeq3.add(word3.substring(k - 1, k));
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
    }

    private void backtracking(String word1, String word2, String word3, String word4) {
        ArrayList<String> alnSeq1 = new ArrayList<>();
        ArrayList<String> alnSeq2 = new ArrayList<>();
        ArrayList<String> alnSeq3 = new ArrayList<>();
        ArrayList<String> alnSeq4 = new ArrayList<>();

        int i = table.getX() - 1;
        int j = table.getY() - 1;
        int k = table.getZ() - 1;
        int l = table.getA() - 1;

        while (i > 0 || j > 0 || k > 0 || l > 0) {
            if (i > 0 && j > 0 && k > 0 && l > 0) {
                if (table.getEntry(i, j, k, l) == table.getEntry(i - 1, j - 1, k - 1, l - 1) +
                        sumOfPairsScore(word1.substring(i - 1, i), word2.substring(j - 1, j),
                                word3.substring(k - 1, k), word4.substring(l - 1, l))) {
                    alnSeq1.add(word1.substring(i - 1, i));
                    alnSeq2.add(word2.substring(j - 1, j));
                    alnSeq3.add(word3.substring(k - 1, k));
                    alnSeq4.add(word4.substring(l - 1, l));
                    i = i - 1;
                    j = j - 1;
                    k = k - 1;
                    l = l - 1;
                } else if (table.getEntry(i, j, k, l) == table.getEntry(i - 1, j - 1, k - 1, l) +
                        sumOfPairsScore(word1.substring(i - 1, i), word2.substring(j - 1, j),
                                word3.substring(k - 1, k), "-")) {
                    alnSeq1.add(word1.substring(i - 1, i));
                    alnSeq2.add(word2.substring(j - 1, j));
                    alnSeq3.add(word3.substring(k - 1, k));
                    alnSeq4.add("-");
                    i = i - 1;
                    j = j - 1;
                    k = k - 1;
                } else if (table.getEntry(i, j, k, l) == table.getEntry(i - 1, j - 1, k, l - 1) +
                        sumOfPairsScore(word1.substring(i - 1, i), word2.substring(j - 1, j),
                                "-", word4.substring(l - 1, l))) {
                    alnSeq1.add(word1.substring(i - 1, i));
                    alnSeq2.add(word2.substring(j - 1, j));
                    alnSeq3.add("-");
                    alnSeq4.add(word4.substring(l - 1, l));
                    i = i - 1;
                    j = j - 1;
                    l = l - 1;
                } else if (table.getEntry(i, j, k, l) == table.getEntry(i - 1, j, k - 1, l - 1) +
                        sumOfPairsScore(word1.substring(i - 1, i), "-",
                                word3.substring(k - 1, k), word4.substring(l - 1, l))) {
                    alnSeq1.add(word1.substring(i - 1, i));
                    alnSeq2.add("-");
                    alnSeq3.add(word3.substring(k - 1, k));
                    alnSeq4.add(word4.substring(l - 1, l));
                    i = i - 1;
                    k = k - 1;
                    l = l - 1;
                } else if (table.getEntry(i, j, k, l) == table.getEntry(i, j - 1, k - 1, l - 1) +
                        sumOfPairsScore("-", word2.substring(j - 1, j),
                                word3.substring(k - 1, k), word4.substring(l - 1, l))) {
                    alnSeq1.add("-");
                    alnSeq2.add(word2.substring(j - 1, j));
                    alnSeq3.add(word3.substring(k - 1, k));
                    alnSeq4.add(word4.substring(l - 1, l));
                    j = j - 1;
                    k = k - 1;
                    l = l - 1;
                } else if (table.getEntry(i, j, k, l) == table.getEntry(i - 1, j - 1, k, l) +
                        sumOfPairsScore(word1.substring(i - 1, i), word2.substring(j - 1, j),
                                "-", "-")) {
                    alnSeq1.add(word1.substring(i - 1, i));
                    alnSeq2.add(word2.substring(j - 1, j));
                    alnSeq3.add("-");
                    alnSeq4.add("-");
                    i = i - 1;
                    j = j - 1;
                } else if (table.getEntry(i, j, k, l) == table.getEntry(i - 1, j, k - 1, l) +
                        sumOfPairsScore(word1.substring(i - 1, i), "-",
                                word3.substring(k - 1, k), "-")) {
                    alnSeq1.add(word1.substring(i - 1, i));
                    alnSeq2.add("-");
                    alnSeq3.add(word3.substring(k - 1, k));
                    alnSeq4.add("-");
                    i = i - 1;
                    k = k - 1;
                } else if (table.getEntry(i, j, k, l) == table.getEntry(i, j - 1, k - 1, l) +
                        sumOfPairsScore("-", word2.substring(j - 1, j),
                                word3.substring(k - 1, k), "-")) {
                    alnSeq1.add("-");
                    alnSeq2.add(word2.substring(j - 1, j));
                    alnSeq3.add(word3.substring(k - 1, k));
                    alnSeq4.add("-");
                    j = j - 1;
                    k = k - 1;
                } else if (table.getEntry(i, j, k, l) == table.getEntry(i - 1, j - 1, k, l - 1) +
                        sumOfPairsScore(word1.substring(i - 1, i), "-",
                                "-", word4.substring(l - 1, l))) {
                    alnSeq1.add(word1.substring(i - 1, i));
                    alnSeq2.add("-");
                    alnSeq3.add("-");
                    alnSeq4.add(word4.substring(l - 1, l));
                    i = i - 1;
                    l = l - 1;
                } else if (table.getEntry(i, j, k, l) == table.getEntry(i - 1, j - 1, k, l - 1) +
                        sumOfPairsScore("-", word2.substring(j - 1, j),
                                "-", word4.substring(l - 1, l))) {
                    alnSeq1.add("-");
                    alnSeq2.add(word2.substring(j - 1, j));
                    alnSeq3.add("-");
                    alnSeq4.add(word4.substring(l - 1, l));
                    j = j - 1;
                    l = l - 1;
                } else if (table.getEntry(i, j, k, l) == table.getEntry(i - 1, j, k - 1, l - 1) +
                        sumOfPairsScore("-", "-",
                                word3.substring(k - 1, k), word4.substring(l - 1, l))) {
                    alnSeq1.add("-");
                    alnSeq2.add("-");
                    alnSeq3.add(word3.substring(k - 1, k));
                    alnSeq4.add(word4.substring(l - 1, l));
                    k = k - 1;
                    l = l - 1;
                } else if (table.getEntry(i, j, k, l) == table.getEntry(i - 1, j - 1, k - 1, l) +
                        sumOfPairsScore(word1.substring(i - 1, i), "-",
                                "-", "-")) {
                    alnSeq1.add(word1.substring(i - 1, i));
                    alnSeq2.add("-");
                    alnSeq3.add("-");
                    alnSeq4.add("-");
                    i = i - 1;
                } else if (table.getEntry(i, j, k, l) == table.getEntry(i - 1, j - 1, k - 1, l) +
                        sumOfPairsScore("-", word2.substring(j - 1, j),
                                "-", "-")) {
                    alnSeq1.add("-");
                    alnSeq2.add(word2.substring(j - 1, j));
                    alnSeq3.add("-");
                    alnSeq4.add("-");
                    j = j - 1;
                } else if (table.getEntry(i, j, k, l) == table.getEntry(i - 1, j, k - 1, l - 1) +
                        sumOfPairsScore("-", "-", "-", word4.substring(l - 1, l))) {
                    alnSeq1.add("-");
                    alnSeq2.add("-");
                    alnSeq3.add("-");
                    alnSeq4.add(word4.substring(l - 1, l));
                    l = l - 1;
                }
            } else if (l == 0) {
                if (i > 0 && j > 0 && k > 0) {
                    if (table.getEntry(i, j, k, l) == table.getEntry(i - 1, j - 1, k - 1, l) +
                            sumOfPairsScore(word1.substring(i - 1, i), word2.substring(j - 1, j),
                                    word3.substring(k - 1, k), "-")) {
                        alnSeq1.add(word1.substring(i - 1, i));
                        alnSeq2.add(word2.substring(j - 1, j));
                        alnSeq3.add(word3.substring(k - 1, k));
                        alnSeq4.add("-");
                        i = i - 1;
                        j = j - 1;
                        k = k - 1;
                    } else if (table.getEntry(i, j, k, l) == table.getEntry(i - 1, j - 1, k, l) +
                            sumOfPairsScore(word1.substring(i - 1, i), word2.substring(j - 1, j),
                                    "-", "-")) {
                        alnSeq1.add(word1.substring(i - 1, i));
                        alnSeq2.add(word2.substring(j - 1, j));
                        alnSeq3.add("-");
                        alnSeq4.add("-");
                        i = i - 1;
                        j = j - 1;
                    } else if (table.getEntry(i, j, k, l) == table.getEntry(i - 1, j, k - 1, l) +
                            sumOfPairsScore(word1.substring(i - 1, i), "-",
                                    word3.substring(k - 1, k), "-")) {
                        alnSeq1.add(word1.substring(i - 1, i));
                        alnSeq2.add("-");
                        alnSeq3.add(word3.substring(k - 1, k));
                        alnSeq4.add("-");
                        i = i - 1;
                        k = k - 1;
                    } else if (table.getEntry(i, j, k, l) == table.getEntry(i, j - 1, k, l) +
                            sumOfPairsScore("-", word2.substring(j - 1, j),
                                    word3.substring(k - 1, k), "-")) {
                        alnSeq1.add("-");
                        alnSeq2.add(word2.substring(j - 1, j));
                        alnSeq3.add(word3.substring(k - 1, k));
                        alnSeq4.add("-");
                        j = j - 1;
                        k = k - 1;
                    } else if (table.getEntry(i, j, k, l) == table.getEntry(i - 1, j, k, l) +
                            sumOfPairsScore(word1.substring(i - 1, i), "-",
                                    "-", "-")) {
                        alnSeq1.add(word1.substring(i - 1, i));
                        alnSeq2.add("-");
                        alnSeq3.add("-");
                        alnSeq4.add("-");
                        i = i - 1;
                    } else if (table.getEntry(i, j, k, l) == table.getEntry(i, j - 1, k, l) +
                            sumOfPairsScore("-", word2.substring(j - 1, j),
                                    "-", "-")) {
                        alnSeq1.add("-");
                        alnSeq2.add(word2.substring(j - 1, j));
                        alnSeq3.add("-");
                        alnSeq4.add("-");
                        j = j - 1;
                    } else if (table.getEntry(i, j, k, l) == table.getEntry(i, j, k - 1, l) +
                            sumOfPairsScore("-", "-",
                                    word3.substring(k - 1, k), "-")) {
                        alnSeq1.add("-");
                        alnSeq2.add("-");
                        alnSeq3.add(word3.substring(k - 1, k));
                        alnSeq4.add("-");
                        k = k - 1;
                    }
                } else if (k == 0) {
                    if (i > 0 && j > 0) {
                        if (table.getEntry(i, j, k, l) == table.getEntry(i - 1, j - 1, k - 1, l) +
                                sumOfPairsScore(word1.substring(i - 1, i), word2.substring(j - 1, j),
                                        "-", "-")) {
                            alnSeq1.add(word1.substring(i - 1, i));
                            alnSeq2.add(word2.substring(j - 1, j));
                            alnSeq3.add("-");
                            alnSeq4.add("-");
                            i = i - 1;
                            j = j - 1;
                        } else if (table.getEntry(i, j, k, l) == table.getEntry(i - 1, j, k, l) +
                                sumOfPairsScore(word1.substring(i - 1, i), "-", "-", "-")) {
                            alnSeq1.add(word1.substring(i - 1, i));
                            alnSeq2.add("-");
                            alnSeq3.add("-");
                            alnSeq4.add("-");
                            i = i - 1;
                        } else if (table.getEntry(i, j, k, l) == table.getEntry(i, j - 1, k, l) +
                                sumOfPairsScore("-", word2.substring(j - 1, j),
                                        "-", "-")) {
                            alnSeq1.add("-");
                            alnSeq2.add(word2.substring(j - 1, j));
                            alnSeq3.add("-");
                            alnSeq4.add("-");
                            j = j - 1;
                        }
                    } else if (j == 0) {
                        // i>0, j,k,l=0; if i was 0, not in while loop any more
                        if (table.getEntry(i, j, k, l) == table.getEntry(i - 1, j, k, l) +
                                sumOfPairsScore(word1.substring(i - 1, i), "-", "-", "-")) {
                            alnSeq1.add(word1.substring(i - 1, i));
                            alnSeq2.add("-");
                            alnSeq3.add("-");
                            alnSeq4.add("-");
                            i = i - 1;
                        }
                    }
                    //i,k,l = 0; j>0
                    else {
                        alnSeq1.add("-");
                        alnSeq2.add(word2.substring(j - 1, j));
                        alnSeq3.add("-");
                        alnSeq4.add("-");
                        j = j - 1;
                    }
                }
                //k > 0! l still 0
                else if (j == 0) {
                    if (i > 0) {
                        if (table.getEntry(i, j, k, l) == table.getEntry(i - 1, j, k - 1, l) +
                                sumOfPairsScore(word1.substring(i - 1, i), "-", word3.substring(k - 1, k), "-")) {
                            alnSeq1.add(word1.substring(i - 1, i));
                            alnSeq2.add("-");
                            alnSeq3.add(word3.substring(k - 1, k));
                            alnSeq4.add("-");
                            i = i - 1;
                            k = k - 1;
                        } else if (table.getEntry(i, j, k, l) == table.getEntry(i - 1, j, k, l) +
                                sumOfPairsScore(word1.substring(i - 1, i), "-", "-", "-")) {
                            alnSeq1.add(word1.substring(i - 1, i));
                            alnSeq2.add("-");
                            alnSeq3.add("-");
                            alnSeq4.add("-");
                            i = i - 1;
                        } else {
                            alnSeq1.add("-");
                            alnSeq2.add("-");
                            alnSeq3.add(word3.substring(k - 1, k));
                            alnSeq4.add("-");
                            k = k - 1;
                        }
                    }
                    //i,k,l = 0; k >0
                    else {
                        alnSeq1.add("-");
                        alnSeq2.add("-");
                        alnSeq3.add(word3.substring(k - 1, k));
                        alnSeq4.add("-");
                        k = k - 1;
                    }
                }
                //i,l=0, k,j> 0!
                else {
                    //TODO
                }
            }
        }
    }
}

