import controller.Aligner;

public class Main_Alignments {

	public static void main(String[] args) {
		//System.out.println(Scorer.getInstance().getScore('b', '-'));
		Aligner.getInstance().align("abcabcabc".toCharArray(),"caaca".toCharArray(), "caaacca".toCharArray());
	}
}
