import container.generalContainer;

/**
 * Created by nancy on 01.11.16.
 */
public class AlignmentTable {
    private double[][] table2;
    private double[][][] table3;
    private double[][][][] table4;
    container.generalContainer.Dimension dim;

    AlignmentTable(int seq1length, int seq2length) {
        table2 = new double[seq1length + 1][seq2length + 1];
        dim = generalContainer.Dimension.TWO;
    }

    AlignmentTable(int seq1length, int seq2length, int seq3length) {
        table3 = new double[seq1length + 1][seq2length + 1][seq3length + 1];
        this.dim = generalContainer.Dimension.THREE;
    }

    AlignmentTable(int seq1length, int seq2length, int seq3length, int seq4length) {
        table4 = new double[seq1length + 1][seq2length + 1][seq3length + 1][seq4length + 1];
        this.dim = generalContainer.Dimension.FOUR;
    }

    public void setScore(int i, int j, double score){
        table2[i][j] = score;
    }

    public void setScore(int i, int j, int k, double score){
        table3[i][j][k] = score;
    }

    public void setScore(int i, int j, int k, int l, double score){
        table4[i][j][k][l] = score;
    }

    public int getX(){
        switch (this.dim){
            case TWO:
                return table2.length;
            case THREE:
                return table3.length;
            case FOUR:
                return table4.length;
        }
        return 0;
    }

    public int getY(){
        switch (this.dim){
            case TWO:
                return table2[0].length;
            case THREE:
                return table3[0].length;
            case FOUR:
                return table4[0].length;
        }
        return 0;
    }

    public int getZ(){
        switch (this.dim){
            case THREE:
                return table3[0][0].length;
            case FOUR:
                return table4[0][0].length;
        }
        return 0;
    }

    public int getA(){
        if (this.dim.equals(generalContainer.Dimension.FOUR))
            return table4[0][0][0].length;
        else
            return 0;
    }

    public double getEntry(int i, int j){
        return table2[i][j];
    }

    public double getEntry(int i, int j, int k){
        return table3[i][j][k];
    }

    public double getEntry(int i, int j, int k, int l){
        return table4[i][j][k][l];
    }
}
