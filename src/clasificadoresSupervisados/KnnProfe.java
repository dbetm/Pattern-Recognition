package clasificadoresSupervisados;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;
import objetos.Patron;
import objetos.VecinoKnn;
import tools.GeneradorDeInstancias;
import tools.HerramientasClasificadores;
import tools.Tokenizador;

/**
 *
 * @author david
 */
public class KnnProfe implements clasificadorSupervisado {
    private ArrayList<Patron> instancias;
    private double eficacia;
    private ArrayList<String> clases;
    private ArrayList<VecinoKnn> vecinos;
    private int K;

    public KnnProfe(int K) {
        this.K = K;
        this.eficacia = 0;
        this.clases = new ArrayList<>();
        this.instancias = new ArrayList<>();
        this.vecinos = new ArrayList<>();
    }
    

    @Override
    public void entrena(ArrayList<Patron> instancias) {
        this.instancias = instancias;
        // Para saber cuántas clases están involucradas
        for (Patron instancia : this.instancias) {
            if(!this.clases.contains(instancia.getClaseOriginal())) {
                this.clases.add(instancia.getClaseOriginal());
            }
        }
        System.out.println("");
    }
    
    @Override
    public void clasifica(Patron patron) {
        // Calcular todas las distancias
        for (Patron instancia : this.instancias) {
            String clase = instancia.getClaseOriginal();
            double distancia = 
                    HerramientasClasificadores.calcularDistanciaEuclidiana(patron, instancia);
            if(distancia != 0) {
                this.vecinos.add(new VecinoKnn(distancia, clase));
            }
        }
        // Ordenar las distancias
        HerramientasClasificadores.ordenar(vecinos);
        String resultado = verificarKVecinos();
    }
    
    public void clasificaConjunto(ArrayList<Patron> instancias) {
        // Recorremos la colección a clasificación
        int total = instancias.size();
        // Contador de clasificaciones correctos
        int aux = 0;
        for (Patron conejillo : instancias) {
            clasifica(conejillo);
            if(conejillo.getClaseResultante().equals(conejillo.getClaseOriginal()))
                aux++;    
        }
        this.eficacia = (aux * 100) / total;
    }

    public double getEficacia() {
        return eficacia;
    }
    

    private String verificarKVecinos() {
        int contadores[] = new int[this.clases.size()];
        int i = 0;
        int max = 0;
        for (VecinoKnn aux : this.vecinos) {
            String clase = aux.getClasePerteneciente();
            i = this.clases.indexOf(clase);
            contadores[i]++;
            if(contadores[i] == K) {
                break;
            }
        }
        if(i == K) {
            return this.clases.get(i);
        }
        else {
            // Para este caso se retorna la clase con mayor número de vecinos más
            // cercano
            for (int ii = 0; ii < contadores.length; ii++) {
                if(contadores[ii] > contadores[max]) max = ii;
            }
        }
        return this.clases.get(max);
    }
    
    public static void main(String[] args) {   
        Tokenizador.leerDatos();
        // Instanciamos el clasificador supervisado knn
        KnnProfe knn = new KnnProfe(3);
        knn.entrena(Tokenizador.instancias);
        //Patron a = new Patron(new double[]{6.7, 3, 5.2, 2.3}, "desconocida");
        //knn.clasifica(a);
        //System.out.println(a.getClaseOriginal() + " " + a.getClaseResultante());
        
        knn.clasificaConjunto(Tokenizador.instancias);
        //System.out.println(ClassificationChecker.calcEficaciaDistMin(md, aux));
        System.out.println("Eficacia: " + knn.getEficacia());
    }
}
