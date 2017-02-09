package containers;
import java.util.TreeMap;


public class NgramCount {

	private int pseudocount;
	private TreeMap<String, Integer> ngrams;
	private int counter;
	
	public NgramCount(){
		
		this.pseudocount = 1;
		this.ngrams = new TreeMap<String, Integer>();
		this.counter = 0;
	}
	
	
	public TreeMap<String, Integer> getCounts(){
		return this.ngrams;
	}
	
	public int getCounter(){
		return this.counter;
	}
	
	public void addCount(String ngram){
		
		if(this.ngrams.containsKey(ngram)){
			this.ngrams.put(ngram, this.ngrams.get(ngram)+1);
			this.counter++;
		}
		else{
			this.ngrams.put(ngram, 1+this.pseudocount);
			this.counter++;
		}
		
	}
	
}
