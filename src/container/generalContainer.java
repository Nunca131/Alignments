package container;

/**
 * Created by nancy on 01.11.16.
 */
public class generalContainer {

    public static Double bigramDef = -3.5;
    public static Double gapOpen = -4.;
    public static Double gapExtend = -1.;
    public enum Dimension {TWO, THREE, FOUR}
    //public static Dimension dim = Dimension.TWO;
    public enum AlnMode{NWA, SWA, NWAbi, GotohBi}
    public static AlnMode alnMode = AlnMode.GotohBi;

    public enum NgramMode {unigram, bigram};
    public static NgramMode nGramMode = NgramMode.bigram;

    public static boolean trainScores = false;

    public void setBigramDef(Double bigramDef) {
        this.bigramDef = bigramDef;
    }

    public void setGapOpen(Double gapOpen) { this.gapOpen = gapOpen; }

    public void setGapExtend(Double gapExtend) {this.gapExtend = gapExtend;}


}
