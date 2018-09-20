package objetos;
/**
 *
 * @author david
 */
public class Patron {
    /** Los elementos que tiene un patrón en el contexto de clasificación 
    supervisada */
    private double[] caracteristicas;
    // Para etiquetar el objeto de acuerdo a la claseOriginal a la que pertenece
    private String claseOriginal;
    private String claseResultante;
    
    // Inicialización por defecto
    public Patron(int dim) {
        this.caracteristicas = new double[dim];
        this.claseOriginal = "desconocida";
        this.claseResultante = "desconocida";
    }
    
    public Patron(double[] caracteristicas, String clase) {
        this.caracteristicas = caracteristicas;
        this.claseOriginal = clase;
        this.claseResultante = "desconocida";
    }

    public double[] getCaracteristicas() {
        return caracteristicas;
    }

    public String getClaseOriginal() {
        return claseOriginal;
    }

    public void setClaseOriginal(String claseOriginal) {
        this.claseOriginal = claseOriginal;
    }

    public String getClaseResultante() {
        return claseResultante;
    }

    public void setClaseResultante(String claseResultante) {
        this.claseResultante = claseResultante;
    }

    public void setCaracteristicas(double[] caracteristicas) {
        this.caracteristicas = caracteristicas;
    }
    
}
