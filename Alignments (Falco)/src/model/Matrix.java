package model;

/**
 * Objects of this class are representing represent lengths.length-dimensional Matrices stored in 1-dimensional matrices. lengths.length-dimensional indices are mapped to 1-dimensional indices.
 * 
 * @author Falco
 */
public class Matrix {

  // object definition
	/**
	 * the 1-dimensional (flat) matrix
	 */
	private float[] flatMatrix;
	
	/**
	 * the length of each dimension
	 */
	private int[] lengths;
	
	/**
	 * the DimensionsCount = SequencesCount
	 */
	private int dimensions;
	
	/**
	 * generates a new 1-dimensional (flat) matrix with length length^dimensionalCount
	 * @param legth	the length of each dimension
	 * @param dimensionCount the count of the dimensions
	 */
	public Matrix(int... lengths) {
		this.dimensions = lengths.length;
		this.lengths = lengths;
		int length = lengths[0];
		for (int i = 1; i < lengths.length; i++) {
			length *= lengths[i];
		}
		flatMatrix = new float[length];
	}
	
	/**
	 * generates a new 1-dimensional (flat) matrix with length length^dimensionalCount
	 * @param legth	the length of each dimension
	 * @param dimensionCount the count of the dimensions
	 */
	public Matrix(char[]... sequences) {
		this.dimensions = sequences.length;
		int[] lengths = new int[sequences.length];
		for (int i = 0; i < lengths.length; i++) {
			lengths[i] = sequences[i].length+1;
		}
		this.lengths = lengths;
		int length = lengths[0];
		for (int i = 1; i < lengths.length; i++) {
			length *= lengths[i];
		}
		flatMatrix = new float[length];
	}
	
	 /**
	  * maps the DimensionCount-dimensional indices to 1-dimensional indices
	  * @param indices
	  * @return
	  */
	private int getFlatIndex(int... indices){
		if (lengths.length == indices.length) {
			int flatIndex = 0;
			int multiplyer = 1;
			
			for (int i = 0; i < indices.length; i++) {
				int index = indices[i]+1;
				if (index <= lengths[i]) {
					flatIndex += multiplyer * (index-1);
					multiplyer *= lengths[i];
				} else {
					throw new java.lang.Error("Matrix.get: ArrayIndexOutOfBoundsException: " + (index-1));
				}
			}
			return flatIndex;
		} else {
			throw new java.lang.Error("Matrix.get: called with wrong dimensions count");
		}
	}
	
	/**
	 * to String method
	 */
	@Override
		public String toString() {
		String s = "";
			if (this.dimensions == 2) {
				for (int i = 0; i < lengths[0]; i++) {
					for (int j = 0; j < lengths[1]; j++) {
						s += get(i,j) + " | ";
					}
					s += "\n";
				}
			}
			return s;
		}
	
	/**
	 * get the value of the position indices; indices start by 0
	 * @param indices the position
	 * @return the value of the position indices
	 */
	public float get(int... indices){
		return flatMatrix[getFlatIndex(indices)];
	}
	 /**
	  * set the value on the position indices; indices start by 0
	  * @param value the value to set
	  * @param indices the position
	  */
	public void set(float value, int... indices){
		flatMatrix[getFlatIndex(indices)] = value;
	}
	
	/**
	 * returns the lengths of the index' dimension / index' sequence
	 * @param index the index
	 * @return the lengths
	 */
	public int getLength(int index){
		return this.lengths[index];
	}
	
}
