package objetos;

/**
 *
 * @author david
 */
public class VecinoKnn {
    private double distancia;
    private String clasePerteneciente;

    public VecinoKnn(double distancia, String clasePerteneciente) {
        this.distancia = distancia;
        this.clasePerteneciente = clasePerteneciente;
    }

    public double getDistancia() {
        return distancia;
    }

    public void setDistancia(double distancia) {
        this.distancia = distancia;
    }

    public String getClasePerteneciente() {
        return clasePerteneciente;
    }

    public void setClasePerteneciente(String clasePerteneciente) {
        this.clasePerteneciente = clasePerteneciente;
    }

}
