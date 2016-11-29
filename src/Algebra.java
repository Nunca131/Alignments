import java.util.HashMap;

/**
 * Created by nancy on 01.11.16.
 */
public class Algebra {
    private HashMap<String, HashMap<String, Double> > scoreMapping;
    Double bigramDef;

    Algebra(){
        this.scoreMapping = new HashMap<String, HashMap<String, Double> >();
        this.bigramDef = generalContainer.bigramDef;
    }

    public void fill(String ngram1, String ngram2, Double logOddScore){
        HashMap<String, Double> scoreMap = scoreMapping.get(ngram1);
        if(scoreMap != null)
            scoreMapping.get(ngram1).put(ngram2, logOddScore);
        else{
            scoreMap = new HashMap<String, Double>();
            scoreMap.put(ngram2, logOddScore);
            scoreMapping.put(ngram1, scoreMap);
        }
    }

    public double getScore(String ngram1, String ngram2){
        if(generalContainer.mode.equals(generalContainer.AlnMode.NWA)) {
            if (scoreMapping.containsKey(ngram1)) {
                if (scoreMapping.get(ngram1).containsKey(ngram2))
                    return scoreMapping.get(ngram1).get(ngram2);
                else if (ngram1.equals("-") || ngram2.equals("-"))
                    return generalContainer.gapOpen;
                else
                    return this.bigramDef;
            } else {
                if (ngram1.equals("-") && ngram2.equals("-"))
                    return 0.;
                else if (ngram1.equals("-") || ngram2.equals("-"))
                    return generalContainer.gapOpen;
                else
                    return this.bigramDef;
            }
        }
        else if (generalContainer.mode.equals(generalContainer.AlnMode.NWAbi)){
            if(scoreMapping.containsKey(ngram1)) {
                if (scoreMapping.get(ngram1).containsKey(ngram2))
                    return scoreMapping.get(ngram1).get(ngram2);
            }

            else if(ngram1.equals("--") || ngram2.equals("--"))
                return generalContainer.gapExtend;
            else if (ngram1.substring(ngram1.length()-1).equals("-") || ngram2.substring(ngram2.length()-1).equals("-"))
                    return generalContainer.gapOpen;

            return bigramDef;
        }
        System.out.println("score mapping went wrong: " + ngram1 + " " + ngram2);
        return -1.;
    }

    public double getScore(String ngram1, String ngram2, String ngram3){
        return getScore(ngram1, ngram2) + getScore(ngram1, ngram3) + getScore(ngram2, ngram3);
    }

    public double getScore(String ngram1, String ngram2, String ngram3, String ngram4){
        return getScore(ngram1, ngram2) + getScore(ngram1, ngram3) + getScore(ngram1, ngram4) +
                getScore(ngram2, ngram3) + getScore(ngram2, ngram4) + getScore(ngram3, ngram4);
    }
}
