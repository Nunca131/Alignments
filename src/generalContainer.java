/**
 * Created by nancy on 01.11.16.
 */
public class generalContainer {

    static Double bigramDef = -3.5;
    static Double gapOpen = -4.;
    static Double gapExtend = -1.;
    enum Dimension {TWO, THREE, FOUR}
    static Dimension dim = Dimension.TWO;
    enum AlnMode{NWA, SWA, NWAbi}
    static AlnMode mode = AlnMode.NWAbi;

    public void setBigramDef(Double bigramDef) {
        this.bigramDef = bigramDef;
    }

    public void setGapOpen(Double gapOpen) { this.gapOpen = gapOpen; }

    public void setGapExtend(Double gapExtend) {this.gapExtend = gapExtend;}

    public void setDim(int dim){
        if (dim == 2) {
            this.dim = Dimension.TWO;
        }
        else if(dim == 3) {
            this.dim = Dimension.THREE;
        }
        else {
            this.dim = Dimension.FOUR;
        }
    }
}
