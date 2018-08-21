package tools;

import clasificadoresSupervisados.MinimaDistancia;
import java.util.ArrayList;
import objetos.Patron;

/**
 *
 * @author david
 */
public class ClassificationChecker {
    
    public static String calcEficaciaDistMin(MinimaDistancia md, ArrayList<Patron> aux) {
        int positivos = 0;
        // Midiendo la eficacia
        for (Patron conejillo : aux) {
            md.clasifica(conejillo);
            if(conejillo.getClaseOriginal().equals(conejillo.getClaseResultante()))
                positivos++;
        }
        return "Eficacia: " + (positivos * 100) / aux.size() + "%";
    }
}
