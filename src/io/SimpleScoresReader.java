package io;

import container.SimpleScoreAlgebra;
import container.generalContainer;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.FileReader;
import java.io.BufferedReader;
import java.util.ArrayList;

/**
 * Created by nancy on 09.02.17.
 */
public class SimpleScoresReader {

    public void readSimpleScores(String filename, SimpleScoreAlgebra alg) {

        try {
            FileReader reader = new FileReader(filename);
            BufferedReader in = new BufferedReader(reader);

            ArrayList<String> vowels = new ArrayList<>();
            ArrayList<String> consonants = new ArrayList<>();
            ArrayList<String> liquids = new ArrayList<>();

            ArrayList<ArrayList<String>> vowEqSet = new ArrayList<>();
            ArrayList<ArrayList<String>> conEqSet = new ArrayList<>();
            ArrayList<ArrayList<String>> liqEqSet = new ArrayList<>();

            String line = "";
            String[] lineSplit;

            while (in.ready()) {
                line = in.readLine();
                if (line.startsWith("EqSet")) {
                    //consonant, vowel, or liquid cases
                    lineSplit = line.split("\\s+");
                    ArrayList<String> eqSet = new ArrayList<>();
                    for (int i = 2; i < lineSplit.length; i++) {
                        eqSet.add(lineSplit[i]);
                        if (lineSplit[1].equals("Vowel")){
                            vowels.add(lineSplit[i]);
                        }
                        else if (lineSplit[1].equals("Conso")){
                            consonants.add(lineSplit[i]);
                        }
                        else if (lineSplit[1].equals("Liquid")){
                            liquids.add(lineSplit[i]);
                        }
                    }
                    if (lineSplit[1].equals("Vowel")){
                        vowEqSet.add(eqSet);
                    }
                    else if (lineSplit[1].equals("Conso")){
                        conEqSet.add(eqSet);
                    }
                    else if (lineSplit[1].equals("Liquid")){
                        liqEqSet.add(eqSet);
                    }
                }
                else if (line.startsWith("Set")) {
                    lineSplit = line.split("\\s+");
                    for (int i = 2; i < lineSplit.length; i++) {
                        if (lineSplit[1].equals("Vowel")) {
                            vowels.add(lineSplit[i]);
                        } else if (lineSplit[1].equals("Conso")) {
                            consonants.add(lineSplit[i]);
                        } else if (lineSplit[1].equals("Liquid")) {
                            liquids.add(lineSplit[i]);
                        }
                    }
                }
                else if (line.startsWith("Eq")) {
                    //scores for same character matches in equality sets
                    lineSplit = line.split("\\s+");
                    if (lineSplit[1].equals("Vowel")) {
                        alg.setVowScoreEq(Double.parseDouble(lineSplit[2]));
                    } else if (lineSplit[1].equals("Conso")) {
                        alg.setConScoreEq(Double.parseDouble(lineSplit[2]));
                    } else if (lineSplit[1].equals("Liquid")) {
                        alg.setLiqScoreEq(Double.parseDouble(lineSplit[2]));
                    }
                }
                else if (line.startsWith("InSet")){
                    //scores for same type matches (consonant vs consonant that are not in equality set)
                    lineSplit = line.split("\\s+");
                    if (lineSplit[1].equals("Vowel") && lineSplit[2].equals("Vowel")) {
                        alg.setVowVowScore(Double.parseDouble(lineSplit[2]));
                    } else if (lineSplit[1].equals("Vowel") && lineSplit[2].equals("Conso")) {
                        alg.setVowConScore(Double.parseDouble(lineSplit[2]));
                    } else if (lineSplit[1].equals("Vowel") && lineSplit[2].equals("Liquid")) {
                        alg.setVowLiqScore(Double.parseDouble(lineSplit[2]));
                    }
                    else if (lineSplit[1].equals("Conso") && lineSplit[2].equals("Vowel")) {
                        alg.setConVowScore(Double.parseDouble(lineSplit[2]));
                    } else if (lineSplit[1].equals("Conso") && lineSplit[2].equals("Conso")) {
                        alg.setConConScore(Double.parseDouble(lineSplit[2]));
                    } else if (lineSplit[1].equals("Conso") && lineSplit[2].equals("Liquid")) {
                        alg.setConLiqScore(Double.parseDouble(lineSplit[2]));
                    }
                    else if (lineSplit[1].equals("Liquid") && lineSplit[2].equals("Vowel")) {
                        alg.setLiqVowScore(Double.parseDouble(lineSplit[2]));
                    } else if (lineSplit[1].equals("Liquid") && lineSplit[2].equals("Conso")) {
                        alg.setLiqConScore(Double.parseDouble(lineSplit[2]));
                    } else if (lineSplit[1].equals("Liquid") && lineSplit[2].equals("Liquid")) {
                        alg.setLiqLiqScore(Double.parseDouble(lineSplit[2]));
                    }
                }
                else if (line.startsWith("GapOpen")) {
                    alg.setGapOpen(Double.parseDouble(line.split("\\s+")[1]));
                }
                else if (line.startsWith("GapExtend")){
                    alg.setGapExtend(Double.parseDouble(line.split("\\s+")[1]));
                }

            }

            alg.setConEqSet(conEqSet);
            alg.setConsonants(consonants);

            alg.setLiqEqSet(liqEqSet);
            alg.setLiquids(liquids);

            alg.setVowEqSet(vowEqSet);
            alg.setVowels(vowels);

            in.close();
        } catch (FileNotFoundException e) {
            System.err.println("Could not find the file " + filename);
        } catch (IOException e) {
            System.err.println("There was an error processing the file " + filename);
        }
    }
}
