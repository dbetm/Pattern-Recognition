package objetos;
/**
 *
 * @author david
 */
public class PatronRepresentativo extends Patron {
    // el número de patrones que representa el objeto
    private int numPatrones;
    
    public PatronRepresentativo(int dim) {
        super(dim);
        this.numPatrones = 1;
    }
    
    public PatronRepresentativo(double[] caracteristicas, String clase) {
        super(caracteristicas, clase);
        this.numPatrones = 1;
    }

    public int getNumPatrones() {
        return numPatrones;
    }

    public void setNumPatrones(int numPatrones) {
        this.numPatrones = numPatrones;
    }
}
