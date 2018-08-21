package clasificadoresSupervisados;

import java.util.ArrayList;
import objetos.Patron;

/**
 *
 * @author david
 */
/*
   Conjunto/Instancias de entrenamiento para cada clasificador, conjunto de patrones
   con los que se entrena, y se considera el número de características
*/
public interface clasificadorSupervisado {
    // Se entrena con un conjunto de patrones con una clase asignada y [] características
    public void entrena(ArrayList<Patron> instancias);
    // Se le pasa una referencia del patrón
    public void clasifica(Patron patron);
    
}
