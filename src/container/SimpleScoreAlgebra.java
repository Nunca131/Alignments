package container;

import java.util.ArrayList;

/**
 * Created by nancy on 10.02.17.
 */
public class SimpleScoreAlgebra {

    //contains all vowels
    private ArrayList<String> vowels = new ArrayList<>();
    //contains all consonants
    private ArrayList<String> consonants = new ArrayList<>();
    //contains all liquids
    private ArrayList<String> liquids = new ArrayList<>();

    //contains all vowel equality sets
    private ArrayList<ArrayList<String>> vowEqSets = new ArrayList<ArrayList<String>>();
    //contains all consonant equality sets
    private ArrayList<ArrayList<String>> conEqSets = new ArrayList<ArrayList<String>>();
    //contains all liquid equality sets
    private ArrayList<ArrayList<String>> liqEqSets = new ArrayList<ArrayList<String>>();

    private double conScoreEq;
    private double vowScoreEq;
    private double liqScoreEq;

    private double vowVowScore;
    private double vowConScore;
    private double vowLiqScore;
    private double conVowScore;
    private double conConScore;
    private double conLiqScore;
    private double liqVowScore;
    private double liqConScore;
    private double liqLiqScore;

    public double gapOpen;
    public double gapExtend;

    public void setVowels(ArrayList<String> vowels) {
        this.vowels = vowels;
    }

    public void setConsonants(ArrayList<String> consonants) {
        this.consonants = consonants;
    }

    public void setLiquids(ArrayList<String> liquids) {
        this.liquids = liquids;
    }

    public void setVowEqSet(ArrayList<ArrayList<String>> vowEqSets) {
        this.vowEqSets = vowEqSets;
    }

    public void setConEqSet(ArrayList<ArrayList<String>> conEqSets) {
        this.conEqSets = conEqSets;
    }

    public void setLiqEqSet(ArrayList<ArrayList<String>> liqEqSets) {
        this.liqEqSets = liqEqSets;
    }

    public void setConScoreEq(double conScoreEq) {
        this.conScoreEq = conScoreEq;
    }

    public void setVowScoreEq(double vowScoreEq) {
        this.vowScoreEq = vowScoreEq;
    }

    public void setLiqScoreEq(double liqScoreEq) {
        this.liqScoreEq = liqScoreEq;
    }

    public void setVowVowScore(double vowVowScore) {
        this.vowVowScore = vowVowScore;
    }

    public void setVowConScore(double vowConScore) {
        this.vowConScore = vowConScore;
    }

    public void setVowLiqScore(double vowLiqScore) {
        this.vowLiqScore = vowLiqScore;
    }

    public void setConVowScore(double conVowScore) {
        this.conVowScore = conVowScore;
    }

    public void setConConScore(double conConScore) {
        this.conConScore = conConScore;
    }

    public void setConLiqScore(double conLiqScore) {
        this.conLiqScore = conLiqScore;
    }

    public void setLiqVowScore(double liqVowScore) {
        this.liqVowScore = liqVowScore;
    }

    public void setLiqConScore(double liqConScore) {
        this.liqConScore = liqConScore;
    }

    public void setLiqLiqScore(double liqLiqScore) {
        this.liqLiqScore = liqLiqScore;
    }

    public void setGapOpen(double gapOpen) {
        this.gapOpen = gapOpen;
    }

    public void setGapExtend(double gapExtend) {
        this.gapExtend = gapExtend;
    }

    public double getScore(String unigram1, String unigram2) {
        if (vowels.contains(unigram1)) {
            if (vowels.contains(unigram2)) {
                //check whether in eq set else return vowVowScore
                for (ArrayList<String> vowEqSet : vowEqSets) {
                    if (vowEqSet.contains(unigram1) && vowEqSet.contains(unigram2))
                        return vowScoreEq;
                }
                return vowVowScore;
            } else if (consonants.contains(unigram2))
                return vowConScore;
            else
                return vowLiqScore;
        } else if (consonants.contains(unigram1)) {
            if (consonants.contains(unigram2)) {
                for (ArrayList<String> conEqSet : conEqSets) {
                    if (conEqSet.contains(unigram1) && conEqSet.contains(unigram2))
                        return conScoreEq;
                }
                return conConScore;
            } else if (vowels.contains(unigram2))
                return conVowScore;
            else
                return conLiqScore;
        }
        if (liquids.contains(unigram1)) {
            if (liquids.contains(unigram2)) {
                for (ArrayList<String> liqEqSet : liqEqSets) {
                    if (liqEqSet.contains(unigram1) && liqEqSet.contains(unigram2))
                        return liqScoreEq;
                }
                return liqLiqScore;
            } else if (vowels.contains(unigram2))
                return liqVowScore;
            else
                return liqConScore;
        }
        else
            return gapOpen;
    }
}
