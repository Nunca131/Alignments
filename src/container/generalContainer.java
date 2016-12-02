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
    enum AlnMode{NWA, SWA, NWAbi, GotohBi}
    static AlnMode mode = AlnMode.GotohBi;

    public void setBigramDef(Double bigramDef) {
        this.bigramDef = bigramDef;
    }

    public void setGapOpen(Double gapOpen) { this.gapOpen = gapOpen; }

    public void setGapExtend(Double gapExtend) {this.gapExtend = gapExtend;}


}
