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
import tools.HerramientasClasificadores;

/**
 *
 * @author david
 */
public class Knn implements clasificadorSupervisado {
    private ArrayList<Patron> instancias;
    private double eficacia;
    public static final int K = 3;

    @Override
    public void entrena(ArrayList<Patron> instancias) {
        this.instancias = instancias;
        this.eficacia = 0;
    }
    
    @Override
    public void clasifica(Patron patron) {
        // Se debe obtener la distancia entre el patrón y todas las 
        // instancias, se guardan en un arreglo de vecinos
        ArrayList<VecinoKnn> distancias = new ArrayList<>();
        VecinoKnn vecino;
        Double dist;
        Map<String, Integer> tablaHash = new TreeMap<String, Integer>();
        boolean fueClasificada = false;
        
        for (Patron instancia : instancias) {
            dist = new Double(HerramientasClasificadores
                .calcularDistanciaEuclidiana(instancia, patron));
            vecino = new VecinoKnn(dist, instancia.getClaseOriginal());
            distancias.add(vecino);
        }
        // Se ordenan por distancia, de manera ascendente
        Collections.sort(distancias, new Comparator<VecinoKnn>() {
            @Override
            public int compare(VecinoKnn v1, VecinoKnn v2) {
                if (v1.getDistancia() < v2.getDistancia()) return -1;
                else if (v1.getDistancia() > v2.getDistancia()) return 1;
                return 0;
            }
        });
        // Cuando el número de incidencias de cierta clase sea igual a k, entonces
        // al patrón se le seteará la clase resultante
        for (VecinoKnn distancia : distancias) {
            String claseClave = distancia.getClasePerteneciente();
            Integer val;
            if(tablaHash.containsKey(claseClave)) {
                val = tablaHash.get(claseClave) + 1;
                tablaHash.replace(claseClave, val);
            }
            else {
                tablaHash.put(claseClave, 1);
            }
            if(tablaHash.get(claseClave) == K) {
                // Le agregamos la clase que primero cumplió con las 
                // k incidencias de vecino más cercano
                patron.setClaseResultante(claseClave);
                fueClasificada = true;
                break;
            }
        }
        // Si no se clasificó, agregamos la que más se repite
        if(!fueClasificada) {
           Integer max = 0;
           String aproachClass = "desconocida";
           Integer value;
           String key;
           
            for (Entry<String, Integer> entry : tablaHash.entrySet()) {
                value = entry.getValue();
                if(value > max)  {
                    max = value;
                    aproachClass = entry.getKey();
                }
            }
            patron.setClaseResultante(aproachClass);
        } 
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
}
