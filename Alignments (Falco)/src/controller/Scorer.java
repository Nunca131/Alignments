package controller;

/**
 * The Scorer is a singleton class containing methods to compute scores for matches and miss-matches as well as gaps.
 * 
 * @author Falco, inspired by Alignments/src/container/Algebra.java written by Nancy 
 */
public class Scorer {
  
  // static finals values
	/**
	 * char representing a gap
	 */
	public static final char GAP_CHAR = '-';

  // static values an methods
	/*
	 * self explanatory
	 */
	public static float scoreGap = -1;
	public static float scoreMatch = 5;
	public static float scoreMissMatch = -1;
	
	/**
	 * sets all the static values
	 */
	public static void init(float scoreGap, float scoreMatch, float scoreMissMatch){
		Scorer.scoreGap = scoreGap;
		Scorer.scoreMatch = scoreMatch;
		Scorer.scoreMissMatch = scoreMissMatch;
	}
	
  // singleton intrinsic
	/**
	 * the singleton instance
	 */
	private static Scorer instance;
	
	/**
	 * singleton get instance method
	 * @return an instance of this class
	 */
	public static Scorer getInstance(){
		if (instance == null) {
			instance = new Scorer();
		}
		return instance;
	}

  // object definition
	/**
	 * computes the score for two characters
	 * @param c1 first character
	 * @param c2 second character
	 * @return the score
	 */
	public float getScore(char c1, char c2){
		if (c1 == c2) 
			return scoreMatch;
		if (c1 == GAP_CHAR || c2 == GAP_CHAR ) 
			return scoreGap;
		return scoreMissMatch;
	}
	
	/**
	 * computes the Sum of Pairs Score for all possible  pairs of n characters
	 * @param chars the chars to score
	 * @return the score
	 */
	public float getScoreSumOfPairs(char... chars){
		float score = 0;
		for (int i = 0; i < chars.length-1; i++) {
			for (int j = i+1; j < chars.length; j++) {
				score += getScore(chars[i], chars[j]);
//				System.out.println(""+ chars[i] + chars[j]);
			}
		}
		return score;
	}
	
}
