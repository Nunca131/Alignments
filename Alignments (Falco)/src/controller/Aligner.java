package controller;

import java.util.ArrayList;

import model.IndexVector;
import model.Matrix;

/**
 * The Aligner is a singleton class containing methods to compute Alignments
 * 
 * @author Falco, inspired by Alignments/src/algorithms/GotohBigram.java written by Nancy
 */
public class Aligner {
  
  // singleton intrinsic
	/**
	 * the singleton instance
	 */
	protected static Aligner instance;
	
	/**
	 * singleton get instance method
	 * @return an instance of this class
	 */
	public static Aligner getInstance(){
		if (instance == null) {
			instance = new Aligner();
		}
		return instance;
	}
	
  // object definition
	public void align(char[]... sequences){
		backtrack(computeMatrix(sequences), sequences);
	}
	
	
	/**
	 * 
	 * @return
	 */
	protected char[][] backtrack(Matrix scoreMatrix, char[]... sequences){
		IndexVector piPattern = new IndexVector(new int[sequences.length]);		// the PI-Pattern vector used in case distinctions (PI-Pattern)
		IndexVector indices = new IndexVector(new int[sequences.length]);		// to save the actual matrix indices
		char[] chars = new char[sequences.length];								// temporary char array used in calculation
		String[] alignedSequences = new String[sequences.length]; 				// the aligned Sequences (output)
		
		for (int i = 0; i < alignedSequences.length; i++) {						// set them to ""
			alignedSequences[i] = "";
		}
		
		for (int i = 0; i < sequences.length; i++) {							// start on the bottom right of the matrix (max indices)
			indices.set(sequences[i].length, i);
		}
		
		// calculation
		boolean c = false;
		while (true) {															// iteration over (negative!) PI-Pattern starts here. (Pi-Pattern e {0,-1}^n)
			System.out.println(piPattern);
			if (!piPattern.isNullVector() && !indices.getChangedVector(piPattern.toArray()).hasNegetiveEntry()) {		// ignore case where PI-Pattern = (0,...,0) (total gap is not allowed)
				// case distinctions																					// or where one or more indices get negative
				for (int i = 0; i < chars.length; i++) {
					if (piPattern.get(i) == 0) { 
						chars[i] = Scorer.GAP_CHAR;
					} else {
						chars[i] = sequences[i][indices.get(i)-1];	
					}
				}
				
				for (int i = 0; i < indices.length(); i++) {
					System.out.print(indices.get(i) + ",");
				}
				System.out.print(chars);
				System.out.println(" : " + scoreMatrix.get(indices.getChangedArray(piPattern.toArray())) + " =? " + scoreMatrix.get(indices.toArray()) + "-" + Scorer.getInstance().getScoreSumOfPairs(chars));
				
				if (scoreMatrix.get(indices.getChangedArray(piPattern.toArray())) == scoreMatrix.get(indices.toArray()) - Scorer.getInstance().getScoreSumOfPairs(chars)) {
					System.out.println("yes");
					for (int i = 0; i < alignedSequences.length; i++) {
						alignedSequences[i] = chars[i] + alignedSequences[i];
					}
					indices = indices.getChangedVector(piPattern.toArray());
					if (indices.isNullVector()) {
						break;
					}
					piPattern.setToNullVector();
					continue;
				}
			}
			
			// iteration logic (PI-Pattern)
			piPattern.addTo(-1, piPattern.length()-1);								// iteration logic
			for (int i = piPattern.length()-1; i >= 0; i--) {
				if (piPattern.get(i) == -2) {										// check iteration completeness
					if (i == 0) {			
						c = true;
						break;
					}
					piPattern.set(0, i);
					piPattern.addTo(-1, i-1);
				}
			}
			if (c) { break; }	
		}
		System.out.println(alignedSequences[0]);
		System.out.println(alignedSequences[1]);
		System.out.println(alignedSequences[2]);
		System.out.println(scoreMatrix.get(0,1,2));
		return null;
	}
	
	/**
	 * aligns n sequences
	 * ueses the Matrix class which may cause more effort (O(n) with n = sequences.length per Matrix lookup)
	 * @param sequences the sequences to align given in char arrays
	 */
	public Matrix computeMatrix(char[]... sequences){
		IndexVector iPattern = new IndexVector(new int[sequences.length]);		// the index Vector used for iteration (I-Pattern)
		Matrix scoreMatrix = new Matrix(sequences);								// the score Matrix
		
		//initialization (insertions and deletions for 1st row/column)
		for (int i = 0; i < iPattern.length(); i++) {
			for (iPattern.set(1, i); iPattern.get(i) < scoreMatrix.getLength(i); iPattern.addTo(1, i)) {
				scoreMatrix.set(scoreMatrix.get(iPattern.getChangedArray(-1, i)) + Scorer.scoreGap, iPattern.toArray());
			}
			iPattern.set(0, i);	
		}
		
		// calculation
		for (int i = 0; i < iPattern.length(); i++) {						// start with I-Pattern (1,...,1)
			iPattern.set(1, i);
		}
		
		boolean b = false;
		while (true) {														// iteration over I-Pattern starts here
			
			// calculation
			IndexVector piPattern = new IndexVector(new int[sequences.length]);			// the PI-Pattern vector used in case distinctions (PI-Pattern)
			ArrayList<Float> scores = new ArrayList<Float>();							// stores the scores, used to find max score
			boolean c = false;
			while (true) {																// iteration over PI-Pattern starts here
				
				if (!piPattern.isNullVector()) {										// ignore case where PI-Pattern = (0,...,0) (total gap is not allowed)
					// case distinctions
					int[] predecessorIPattern = iPattern.getChangedArray(piPattern.toArray());	// (miss)match/gaps in different sequences
					scores.add(scoreMatrix.get(predecessorIPattern) + Scorer.getInstance().getScoreSumOfPairs(getSequencesCharArarybyIndexes(iPattern.toArray(), piPattern.toArray(), sequences)));
					
					// debugging
//					System.out.print(I-Pattern + "" + PI-Pattern);
//					System.out.println(matrix.get(newIndices) + " + " + Scorer.getInstance().getScoreSumOfPairs(getSequencesCharArarybyIndexes(indices.toArray(), piPattern.toArray(), sequences)));
					
					float maxScore = -10000;											// calculate max Score
					for (Float score : scores) {
						if (score > maxScore) {
							maxScore = score;
						}
					}
					scoreMatrix.set(maxScore, iPattern.toArray());						// set max score
				}
				
				// iteration logic (PI-Pattern)
				piPattern.addTo(-1, piPattern.length()-1);								// iteration logic
				for (int i = piPattern.length()-1; i >= 0; i--) {
					if (piPattern.get(i) == -2) {										// check iteration completeness
						if (i == 0) {			
							c = true;
							break;
						}
						piPattern.set(0, i);
						piPattern.addTo(-1, i-1);
					}
				}
				if (c) { break; }														// iteration complete?
			}																			// iteration over PI-Pattern ends here
			
			// iteration logic (I-Pattern)
			iPattern.addTo(1, iPattern.length()-1);										// iteration logic
			for (int i = iPattern.length()-1; i >= 0; i--) {
				if (iPattern.get(i) == scoreMatrix.getLength(i)) {
					if (i == 0) {			
						b = true;														// check iteration completeness
						break;
					}
					iPattern.set(1, i);
					iPattern.addTo(1, i-1);
				}
			}
			if (b) { break; }															// iteration complete?
		}																				// iteration over I-Pattern (I-Pattern) ends here
			
		// print
		System.out.println(scoreMatrix.toString());
		return scoreMatrix;
	}
	
	/**
	 * returns the actual vertical vector of the Alignment, the actual character of each sequence or Scorer.GAP_CHAR if pi at this column and for this sequence is 0 (piPattern[i] == 0)
	 * @param iPattern
	 * @param piPattern
	 * @param sequences
	 * @return the actual vertical vector of the Alignment
	 */
	protected char[] getSequencesCharArarybyIndexes(int[] iPattern, int[] piPattern, char[][] sequences){
		char[] chars = new char[iPattern.length];
		for (int i = 0; i < iPattern.length; i++) {
			if (piPattern[i] == 0) {
				chars[i] = Scorer.GAP_CHAR;
			} else {
				chars[i] = sequences[i][iPattern[i] + piPattern[i]];
			}
		}
		return chars;
	}
}
