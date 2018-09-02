package tools;
import objetos.Patron;
import objetos.VecinoKnn;
import java.util.ArrayList;
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
    
    public static void ordenar(ArrayList<VecinoKnn> vecinos) {
        VecinoKnn temp;
        for (int i = 1; i < vecinos.size(); i++) {
            for (int j = 0; j < vecinos.size() - i; j++) {
                if(vecinos.get(j).getDistancia() > 
                    vecinos.get(j + 1).getDistancia()) {
                    // Se intercambian
                    temp = new VecinoKnn(vecinos.get(j).getDistancia(), 
                        vecinos.get(j).getClasePerteneciente());
                    // A[j] = A[j+1];
                    vecinos.get(j).setDistancia(vecinos.get(j + 1).getDistancia());
                    vecinos.get(j).setClasePerteneciente(vecinos.get(j + 1).
                        getClasePerteneciente());
                    // A[j+1] = temp;
                    vecinos.get(j + 1).setDistancia(temp.getDistancia());
                    vecinos.get(j + 1).setClasePerteneciente(temp.
                       getClasePerteneciente());
                }
            }
        }
    }
}
