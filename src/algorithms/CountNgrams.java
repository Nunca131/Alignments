package dataProcessing;
import containers.NgramCount;
import defpackage.Mode;

import java.util.TreeMap;

/**
 * counts bigrams and bigram pairs
 * 
 * @author Nunca
 *
 */
public class CountNgrams {

	private TreeMap<String, NgramCount> bigrams;
	private TreeMap<String, NgramCount> bigramPairs;
	private TreeMap<String, NgramCount> unigrams;
	private TreeMap<String, NgramCount> unigramPairs;

	/**
	 * constructs new counter
	 */
	public CountNgrams() {
		this.bigrams = new TreeMap<String, NgramCount>();		
		this.bigramPairs = new TreeMap< String, NgramCount>();
		this.unigrams = new TreeMap<String, NgramCount>();
		this.unigramPairs = new TreeMap<String, NgramCount>();
	}

	/**
	 * returns the map containing languages and counts
	 * @return map
	 */
	public TreeMap<String, NgramCount> getBigramPairCounts(){
		return this.bigramPairs;
	}

	public TreeMap<String, NgramCount> getUnigramPairCounts(){
		return this.unigramPairs;
	}

	/**
	 * returns the map containing languages and bigrams
	 */
	public TreeMap<String, NgramCount> getBigramCounts(){
		return this.bigrams;
	}

	public TreeMap<String, NgramCount> getUnigramCounts(){
		return this.unigrams;
	}


	/**
	 * stores bigram pair counts
	 * @param aln1 first aligned sequence
	 * @param aln2 second aligned sequence
	 * @param lang1 language of first aligned sequence
	 * @param lang2 language of second aligned sequence
	 */
	public void countNgramPairs(String aln1, String aln2, String lang1, String lang2) {

		String pairKey = lang1 + " " + lang2;

		String[] aln1Vec = aln1.split("\\s+");
		String[] aln2Vec = aln2.split("\\s+");

		String bigrampair = "";
		String unigrampair = "";

		//both aln vectors have same length
		for(int i = 0; i < aln1Vec.length-1; i++){

			if(!this.bigrams.containsKey(aln1Vec[i] + " " + aln1Vec[i+1]))
				addBigram(lang1, aln1Vec[i] + " " + aln1Vec[i+1]);
			if(!this.unigrams.containsKey(aln1Vec[i]))
				addUnigram(lang1, aln1Vec[i]);
			if(!this.bigrams.containsKey(aln2Vec[i] + " " + aln2Vec[i+1]))
				addBigram(lang2, aln2Vec[i] + " " + aln2Vec[i+1]);
			if(!this.unigrams.containsKey(aln2Vec[i]))
				addUnigram(lang2, aln2Vec[i]);

			bigrampair = aln1Vec[i] + " " + aln1Vec[i+1] + " " + aln2Vec[i] + " " + aln2Vec[i+1];
			addBigrampair(pairKey, bigrampair);

			if(!aln1Vec[i].equals("-") && !aln2Vec[i].equals("-")){
				unigrampair = aln1Vec[i] + " " + aln2Vec[i];
				addUnigrampair(pairKey, unigrampair);
			}

		}

		if(!this.unigrams.containsKey(aln1Vec[aln1Vec.length-1]))
			addUnigram(lang1, aln1Vec[aln1Vec.length-1]);
		if(!this.unigrams.containsKey(aln2Vec[aln2Vec.length-1]))
			addUnigram(lang2, aln2Vec[aln2Vec.length-1]);

		unigrampair = aln1Vec[aln1Vec.length-1] + " " + aln2Vec[aln2Vec.length-1];
		addUnigrampair(pairKey, unigrampair);
	}

	/**
	 * stores bigram counts
	 * @param language
	 * @param word
	 * @param mode
	 */
	public void countBigrams(String language, String word, Mode.mode mode){

		if(mode.toString().equals("normal")){
			word = "^ " + word + " &"; 
		}

		String[] unigrams = word.split("\\s+");
		String bigram = "";

		for(int i = 0; i < unigrams.length-1; i++){

			bigram = unigrams[i] + " " + unigrams[i+1];
			addBigram(language, bigram);
		}

	}

	/**
	 * adds bigram pair to the database under the respective language pair
	 * @param pairKey
	 * @param bigrampair
	 */
	private void addBigrampair(String pairKey, String bigrampair) {

		if(this.bigramPairs.containsKey(pairKey)){
			this.bigramPairs.get(pairKey).addCount(bigrampair);
		}

		else{
			NgramCount count = new NgramCount();
			count.addCount(bigrampair);
			this.bigramPairs.put(pairKey, count);
		}

	}

	private void addUnigrampair(String pairKey, String unigrampair) {
		if(this.unigramPairs.containsKey(pairKey)){
			this.unigramPairs.get(pairKey).addCount(unigrampair);
		}

		else{
			NgramCount count = new NgramCount();
			count.addCount(unigrampair);
			this.unigramPairs.put(pairKey, count);
		}
	}

	/**
	 * adds bigram to the database under the respective language
	 * @param lang
	 * @param bigram1
	 */
	private void addBigram(String lang, String bigram1) {

		if(this.bigrams.containsKey(lang)){
			this.bigrams.get(lang).addCount(bigram1);
		}

		else{
			NgramCount count = new NgramCount();
			count.addCount(bigram1);
			this.bigrams.put(lang, count);
		}

	}

	private void addUnigram(String lang, String unigram){

		if(this.unigrams.containsKey(lang))
			this.unigrams.get(lang).addCount(unigram);

		else{
			NgramCount count = new NgramCount();
			count.addCount(unigram);
			this.unigrams.put(lang, count);
		}
	}

}
