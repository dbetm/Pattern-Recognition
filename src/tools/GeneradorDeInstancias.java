package tools;

import java.util.ArrayList;
import objetos.Patron;

/**
 *
 * @author david
 */
/*
Para que se generén colecciones/instancias de entrenamiento/conjunto de
entrenamiento o bien conjunto para clasificación, 
de acuerdo a las características seleccionadas
*/
public class GeneradorDeInstancias {
    //Adjunta toda la colección y filtra por características
    public static ArrayList<Patron> genInstanciasPorCaracteristicas(byte[] map) {
        ArrayList<Patron> aux = new ArrayList<>();
        int unos = 0;
        // Para saber la longitud del vector de las características de cada patrón
        for (int i = 0; i < map.length; i++) if(map[i] == 1) unos++;
        
        // Recorremos las instancias
        for(Patron pOriginal: Tokenizador.instancias) {
            // Asegurar la creación de un patrón
            Patron pNuevo;
            String clase = pOriginal.getClaseOriginal();
            double vectorN[] = new double[unos];
            int j = 0;
            for (int i = 0; i < map.length; i++) {
                if(map[i] == 1) {
                    vectorN[j] = pOriginal.getCaracteristicas()[i];
                    j++;
                }
            }
            pNuevo = new Patron(vectorN, clase);
            aux.add(pNuevo);
        }
        return aux;
    }

    
    public static void main(String[] args) {   
        Tokenizador.leerDatos();
        //ArrayList<Patron> aux = GeneradorDeInstancias
        //   .genInstanciasPorCaracteristicas(new byte[]{0,0,0,0,0,0,0,0,0,1,1,1,1});
        
        ArrayList<Patron> aux = GeneradorDeInstancias
            .genInstanciasPorCaracteristicas(new byte[]{1,1,1,1});
        
        System.out.println("");
    }
    
}
