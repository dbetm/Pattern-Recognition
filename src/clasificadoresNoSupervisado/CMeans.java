package clasificadoresNoSupervisado;

import java.util.ArrayList;
import java.util.Random;
import objetos.Patron;
import tools.HerramientasClasificadores;
import tools.Tokenizador;

/**
 *
 * @author David Betancourt Montellano
 */
public class CMeans {
    // ### ATRIBUTOS ###
    // Conjunto de instancias
    private ArrayList<Patron> instancias;
    // Número de clusters => grupos (número de clases) que deseamos genere.
    private int c;
    // Para almacenar el conjunto de centroides generados en cada iteración.
        // Cada centroide corresponde a un grupo de patrones
    private ArrayList<Patron[]> centroides;
    // Arreglo de contadores, para saber de cada iteración, cuántos patrones
    // tiene cada centroide, ejemplo, centroide1(clase A) tiene 8 patrones
    int[] contadores;
    // Como un criterio para detener las iteraciones, si no se cambiar su valor
    // entonces el criterio es por verificación de centroides (convergen).
    int iteraciones = -1;
    
    // ### CONSTRUCTORES ###
    // Se le pasa el conjunto de instancias, el número de cluster a generar 
    // e iteraciones.
    public CMeans(ArrayList<Patron> instancias, int c, int iteraciones) {
        this.instancias = instancias;
        this.c = c;
        this.iteraciones = iteraciones;
        this.centroides = new ArrayList<>();
    }
    
    // Para el criterio cuando se detienen las iteraciones por convergencia de
    // los centroides
    public CMeans(ArrayList<Patron> instancias, int c) {
        this.instancias = instancias;
        this.c = c;
        this.centroides = new ArrayList<>();
    }
    
    // ### MÉTODOS ###
    public void clasifica() {
        // Se generan, de forma aleatoria, los centroides iniciales, se declara r
        Random r;
        // Conjunto de patrones para los centroides, deben ser c patrones 
        // "representativos". 
        Patron[] centroides = new Patron[this.c];
        // Se eligen al azar los primeros c centroides
        for(int i = 0; i < this.c; i++) {
            /* Se obtiene de forma aleatoria, con distinta semilla, la posición
            del centroide seleccionado, el centroide es un patrón de las 
            instancias */
            r = new Random(i);
            int pos = r.nextInt(this.instancias.size());
            // Se guardan los centroides, clonando según la posición obtenida de
            // forma aleatoria y asignando una clase 'Centroide #'.
            centroides[i] = new Patron(
                this.instancias.get(pos).getCaracteristicas().clone(), 
                "Centroide "+i);
        }
        
        // Se agregan a la colección de centroides los centroides inciciales
        this.centroides.add(centroides);
        
        // Etiquetar por primera ocasión (clasificar por primera ocasión)
        etiquetar(centroides);
        
        // Ahora inicia el proceso iterativo, para modificar y/o ajustar 
        // los centroides
        
        // Se declara un contador por si acaso el criterio son las iteraciones
        int contador = 0;
        boolean bandera; // para determinar cuando se detiene el ciclo
        do {
            // Es necesario recalcular los centroides
            // Y es necesario acumular
            Patron[] centroidesNuevos = new Patron[c];
            // Nuevo arreglo de contadores
            this.contadores = new int[c];
            // Se incializan nuevo grupo de centroides
            inicializarNuevosCentroides(centroidesNuevos);
          
            // Para acumular es necesario recorrer todas las instancias
            for (Patron instancia : this.instancias) {
                // Recuperamos la clase de la instancia
                String nombreCluster = instancia.getClaseOriginal();
                forCentroides: for (int i = 0; i < centroidesNuevos.length; i++) {
                    if(centroidesNuevos[i].getClaseOriginal().equals(nombreCluster)) {
                        // se suman sus vectores de características, y la suma
                        // en el centroide del nuevo grupo de centroides
                        centroidesNuevos[i].setCaracteristicas(
                            sumaVectores(centroidesNuevos[i].getCaracteristicas(), 
                               instancia.getCaracteristicas()));
                        this.contadores[i]++;
                        break forCentroides;
                    }
                }
            }
            // Se agrega el grupo de centroides a la colección
            this.centroides.add(centroidesNuevos);
            // Se divide para promediar
            dividirUltimosCentroides();
            
            // Se vuelven a etiquetar las instancias
                // se obtiene el último grupo de centroides generados
            etiquetar(this.centroides.get(this.centroides.size() - 1));
            
            // según el criterio para detener las iteraciones, se obtiene una bandera
            if(iteraciones == -1) bandera = !verificarCentroides();
            else {
                bandera = contador < iteraciones;
                System.out.println("Contador: " + contador++);
            }
        } while(bandera);
        System.out.println("");
    }
    
    private void etiquetar(Patron[] centroides) {
        // para la distancia más pequeña, del patrón respecto del centroide
        double menor, distancia; 
        // Se recorren las instancias y se etiqueta cada una de ellas con
        // base a distancias
        for (Patron patron : this.instancias) {
            // Hipótesis, la distancia más pequeña la guarda con el primer centroide
            menor = HerramientasClasificadores
                .calcularDistanciaEuclidiana(patron, centroides[0]);
            patron.setClaseResultante(centroides[0].getClaseOriginal());
            /* Ahora se comprueba si las distancias del patrón respecto 
            a los centroides no son menores a la de la hipótesis, si es así
            entonces se actualiza la distancia menor, y se re-etiqueta el patrón
            con la clase del centroide con el que se tiene la menor distancia*/
            for (int i = 1; i < this.c; i++) {
                // Se calculan las distancias
                distancia = HerramientasClasificadores
                    .calcularDistanciaEuclidiana(patron, centroides[i]);
                // Si la distancia calculada es menor a la "menor" actual,
                // entonces se actualiza su valor
                if(distancia < menor) 
                    patron.setClaseResultante(centroides[i].getClaseOriginal());
            }
        }
    }
    
    
    private void inicializarNuevosCentroides(Patron[] centroidesNuevos) {
        // Se recorre el arreglo de centroides nuevos
        for (int i = 0; i < centroidesNuevos.length; i++) {
            // se genera el nuevo centroide, el constructor que requiere el
            // arreglo de doubles (en este caso se define el tamaño del dicho arreglo, 
            // y con la clase del anterior centroide
            centroidesNuevos[i] = new Patron(new double[this.instancias.get(0)
                .getCaracteristicas().length], this.centroides.get(
                    this.centroides.size() - 1)[i].getClaseOriginal());
        }
    }
    
    private double[] sumaVectores(double[] A, double[] B) {
        double aux[] = new double[A.length];
        // Suma las características de ambos
        for (int i = 0; i < aux.length; i++) {
            aux[i] = A[i] + B[i];
        }
        return aux;
    }
    
    private void dividirUltimosCentroides() {
        // Obtenemos el último grupo de centroides
        Patron[] aux = this.centroides.get(this.centroides.size() - 1);
        // Recorremos el último grupo de centroides y luego el grupo de 
        // características de cada uno para así promediar
        for (int i = 0; i < aux.length; i++) {
            double[] vector = aux[i].getCaracteristicas();
            for (int j = 0; j < vector.length; j++) {
                vector[j] /= contadores[i];
            }
        }
    }
    
    private boolean verificarCentroides() {
        System.out.println("Voy a comprobar");
        // Verificar si los centroides nuevos son iguales a los anteriores
        int numGruposCentroides = this.centroides.size();
        // Obtenemos el último grupo
        Patron[] ultimo = this.centroides.get(numGruposCentroides - 1);
        // Y ahora el penúltimo
        Patron[] penultimo = this.centroides.get(numGruposCentroides - 2);
        // se recorren ambos grupos para compararlos
        for (int i = 0; i < this.c; i++) {
            // si no son iguales
            if(!ultimo[i].equals(penultimo[i])) return false;
        }
        System.out.println("¡Convergen los centroides!");
        return true;
    }
    
    public static void main(String []args) {
        Tokenizador.leerDatos();
        CMeans cm = new CMeans(Tokenizador.instancias, 3, 500);
        cm.clasifica();
    }
    
}
