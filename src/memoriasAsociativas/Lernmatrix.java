package memoriasAsociativas;

import java.util.ArrayList;

/**
 /*
 * La Lernmatrix es una memoria heteroasociativa que puede funcionar como un 
 * clasificador de patrones binarios si se escogen adecuadamente los patrones 
 * de salida; es un sistema de entrada y salida que al operar acepta como 
 * entrada un patrón binario.
 *
 * @author david
 */
public class Lernmatrix {
    private static final int epsilon = 1;
    private int p; // cardinalidad
    private int n; // dimensión de los patrones
    private int[][] m; // es la memoria asociativa
    
    public Lernmatrix(int p, int n) {
        this.p = p;
        this.n = n;
        m = new int[p][n]; // Al instanciarse se llena de ceros
    }
    
    public void aprender(ArrayList<Asociacion> asociaciones) {
        for (int x = 0; x < asociaciones.size(); x++) {
            int i = x % p;
            System.out.println("");
            for (int j = 0; j < this.n; j++) {
                int valor = 0;
                if(asociaciones.get(x).getEntrada()[j] == 1 && 
                    asociaciones.get(x).getSalida()[i] == 1) {
                    valor = epsilon;
                }
                else if(asociaciones.get(x).getEntrada()[j] == 0 && 
                    asociaciones.get(x).getSalida()[i] == 1) {
                    valor = -epsilon;
                }
                m[i][j] += valor;
            }
        }
        mostrarMatrix();
    }
    
    // Se recupera la clase perteniente de la entrada
    public int[] recuperar(int[] entrada) {
        int clase[] = new int[this.p];
        int indexMax = 0;
        int max = 0;
        
        for (int i = 0; i < this.p; i++) {
            int res = 0;
            for (int j = 0; j < this.n; j++) {
                res += m[i][j]*entrada[j];
            }
            // Restaría condicionar cuando res == max, que sería para 
            // cuando hay saturación.
            if(res > max) {
                max = res;
                indexMax = i;
            }
        }
        // En clase en indexMax ponemos un 1
        clase[indexMax] = 1;
        return clase;
    }
    
    private void mostrarMatrix() {
        for (int i = 0; i < this.p; i++) {
            for (int j = 0; j < this.n; j++) {
                System.out.print(m[i][j] + " ");
            }
            System.out.println("");
        }
    }
    
    // Pruebas unitarias
    public static void main(String []args) {
        ArrayList<Asociacion> asociaciones = new ArrayList<>();
        asociaciones.add(new Asociacion(new int[]{1,0,1,0,1}, new int[]{1,0,0}));
        asociaciones.add(new Asociacion(new int[]{1,1,0,0,1}, new int[]{0,1,0}));
        asociaciones.add(new Asociacion(new int[]{1,0,1,1,0}, new int[]{0,0,1}));
        //asociaciones.add(new Asociacion(new int[]{0,1,0,1,1}, new int[]{1,0,0}));
        
        Lernmatrix lm = new Lernmatrix(3, 5);
        lm.aprender(asociaciones);
        // Un ejemplo de la recuperación
        int clase[] = lm.recuperar(new int[]{1,0,1,0,1});
        System.out.println("Clase resultante: ");
        for (int i = 0; i < clase.length; i++) {
            System.out.println(clase[i]);
        }
    } 
}
