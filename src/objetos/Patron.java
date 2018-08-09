package objetos;

/**
 *
 * @author david
 */
public class Patron {
    /** Los elementos que tiene un patrón en el contexto de clasificación 
    supervisada */
    private double[] caracteristicas;
    // Para etiquetar el objeto de acuerdo a la clase a la que pertenece
    private String clase;
    
    // Inicialización por defecto
    public Patron(int dim) {
        this.caracteristicas = new double[dim];
        this.clase = "desconocida";
    }
    
    public Patron(double[] caracteristicas, String clase) {
        this.caracteristicas = caracteristicas;
        this.clase = clase;
    }

    public double[] getCaracteristicas() {
        return caracteristicas;
    }

    public String getClase() {
        return clase;
    }

    public void setClase(String clase) {
        this.clase = clase;
    }
    
}
