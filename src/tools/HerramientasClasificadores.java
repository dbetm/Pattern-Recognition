package tools;
import objetos.Patron;
/**
 *
 * @author david
 */
public class HerramientasClasificadores {
    // Obtener la distancia mínima entre dos vectores en un espacio n-dimensional
    public static double calcularDistanciaEuclidiana(Patron p, Patron q) {
        double res = 0;
        for (int i = 0; i < p.getCaracteristicas().length; i++) {
            res += Math.pow(p.getCaracteristicas()[i] - q.getCaracteristicas()[i], 2);
        }
        return Math.sqrt(res);
    }
}
