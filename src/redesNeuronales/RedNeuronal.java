package redesNeuronales;

import java.util.ArrayList;
import objetos.Patron;

/**
 *
 * @author david
 */
public interface RedNeuronal {
    public void entrenar(ArrayList<Patron> patrones, double alfa);
    public void entrenarConReglaDelta(ArrayList<Patron> patrones, double alfa);
    public int predecir(Patron patron);
    public void clasificaConjunto(ArrayList<Patron> instancias);
}
