
package tools;

import java.util.Random;

/**
 *
 * @author david
 */
public class NNs {
    
    public static int escalonUnitario(double input) {
        return (input >= 0) ? 1 : 0;
    }
    
    public static double[] generarPesosIniciales(int dim) {
        double[] pesos = new double[dim];
        Random r = new Random();
        
        for (int i = 0; i < dim; i++) {
            pesos[i] = r.nextDouble();
        }
        
        return pesos;
    }
    
    public static double calcularProductoPunto(double[] x, double[] pesos) {
        double ans = 0;
        for (int i = 0; i < x.length; i++) {
            ans += x[i] * pesos[i];
        }
        return ans;
    }
    
    public static void main(String args[]) {
        System.out.println(escalonUnitario(0.1));
        System.out.println(escalonUnitario(-0.1));
    }
    
}
