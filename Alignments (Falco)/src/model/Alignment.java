package model;

/**
 * Objects of this class are representing alignments containing the aligned sequences and the score.
 * 
 * @author Falco
 */
public class Alignment {
	
	/**
	 * the alignment
	 */
	private char[][] alignedSequences;
	
	
	/**
	 * generates a new Alignment with a given Array of chars representing the aligned sequences
	 * use this if the alignment is already computed
	 * @param alignedSequences the aligned sequences
	 * @param aligne set it true if the sequences have to be aligned; false if the sequences are already aligned
	 */
	public Alignment(char[][] sequences, boolean aligne) {
		if (aligne) {
			this.alignedSequences = sequences;	
		} else {
			int maxLength = 0;
			for (int i = 0; i < sequences.length; i++) {
				if (sequences[i].length > maxLength) {
					maxLength = sequences[i].length;
				}
			}
			this.alignedSequences = new char[sequences.length][maxLength];
//			do sum alignm stuff plis
		}
	}

	/**
	 * @return the alignedSequences
	 */
	public char[][] getAlignedSequences() {
		return alignedSequences;
	}

	/**
	 * @param alignedSequences the alignedSequences to set
	 */
	public void setAlignedSequences(char[][] alignedSequences) {
		this.alignedSequences = alignedSequences;
	}

}
