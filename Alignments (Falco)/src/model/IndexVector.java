package model;

public class IndexVector {
	
  // object definition
	/**
	 * the Vector data
	 */
	private int[] data;
	
	
	/**
	 * generates a new Vector
	 * @param data the Vector data
	 */
	public IndexVector(int[] data) {
		this.data = data;
	}
	
	/**
	 * returns the length of the Vector data
	 * @return lengths of the Vector data
	 */
	public int length(){
		return data.length;
	}
	
	/**
	 * gets the value at the position data[index]
	 * @param index the index
	 * @return the value at data[index]
	 */
	public int get(int index){
		return data[index];
	}
	
	/**
	 * sets the value at data[index] to value
	 * @param value the value to set
	 * @param index the index
	 */
	public void set(int value, int index){
		data[index] = value;
	}
	
	/**
	 * adds the valueToAdd tho the value at data[index]
	 * @param valueToAdd the value to add
	 * @param index the index
	 */
	public void addTo(int valueToAdd, int index){
		data[index] += valueToAdd;
	}
	
	/**
	 * adds the valueToAdd to the value at data[index] and returns a new changed Vector. The old Vector stays unchanged.
	 * @param valueToAdd the value to add
	 * @param index the index
	 */
	public IndexVector getChangedVector(int valueToAdd, int index){
		int[] data = this.data.clone();
		data[index] += valueToAdd;
		return new IndexVector(data);
	}
	
	/**
	 * adds the valueToAdd to the value at data[index] and returns a new changed data Array. The old Vector stays unchanged.
	 * @param valueToAdd the value to add
	 * @param index the index
	 */
	public int[] getChangedArray(int valueToAdd, int index){
		int[] data = this.data.clone();
		data[index] += valueToAdd;
		return data;
	}
	
	/**
	 * adds the valuesToAdd to the values at data[index] and returns a new changed Vector. The old Vector stays unchanged.
	 * @param valuesToAdd the values to add
	 */
	public IndexVector getChangedVector(int[] valuesToAdd){
		int[] data = this.data.clone();
		for (int i = 0; i < data.length; i++) {
			data[i] += valuesToAdd[i];
		}
		return new IndexVector(data);
	}
	
	/**
	 * adds the valuesToAdd to the values at data[index] and returns a new changed data Array. The old Vector stays unchanged.
	 * @param valuesToAdd the values to add
	 */
	public int[] getChangedArray(int[] valuesToAdd){
		int[] data = this.data.clone();
		for (int i = 0; i < data.length; i++) {
			data[i] += valuesToAdd[i];
		}
		return data;
	}
	
	/**
	 * sets every entry of this vector to 0
	 */
	public void setToNullVector() {
		for (int i = 0; i < data.length; i++) {
			data[i] = 0;
		}
		
	}
	
	/**
	 * returns true if this vector only contains zeros, false otherwise
	 * @return 
	 */
	public boolean isNullVector(){
		for (int i = 0; i < data.length; i++) {
			if (data[i] != 0) {
				return false;
			}
		}
		return true;
	}
	
	/**
	 * returns true if one entry is negative, false otherwise
	 * @return
	 */
	public boolean hasNegetiveEntry(){
		for (int i = 0; i < data.length; i++) {
			if (data[i] < 0) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * returns the data
	 * @return the data
	 */
	public int[] toArray(){
		return data;
	}
	
	/**
	 * to String method
	 */
	@Override
		public String toString() {
			String s = "";
			for (int i = 0; i < data.length; i++) {
				s += data[i] + " | ";
			}
			s += "\n";
			return s;
		}

}
