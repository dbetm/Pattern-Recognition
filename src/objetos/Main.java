package objetos;

import java.util.ArrayList;
import tools.Tokenizador;

/**
 *
 * @author david
 */
public class Main {
    public static void main(String[] args) {
        // Los plumones tienen peso, ancho y alto
        Patron plumon = new Patron(3);
        plumon.getCaracteristicas()[0] = 75;
        plumon.getCaracteristicas()[1] = 2;
        plumon.getCaracteristicas()[2] = 12;
        plumon.setClaseOriginal("Plumon");
        Patron plumon2 = new Patron(new double[]{75, 2.5, 12.5}, "Plumon");
        // Los gatos tienen peso, no_bigotes, no_patas, long_cola, long_oreja1, long_oreja2
        Patron gato = new Patron(6);
        gato.getCaracteristicas()[0] = 4;
        gato.getCaracteristicas()[1] = 14;
        gato.getCaracteristicas()[2] = 4;
        gato.getCaracteristicas()[3] = 12;
        gato.getCaracteristicas()[4] = 6;
        gato.getCaracteristicas()[5] = 7;
        gato.setClaseOriginal("Gato");
        
        Patron gato2 = new Patron(new double[]{3, 22, 4, 16, 6, 6}, "Gato");
        
        Tokenizador.leerDatos();
        ArrayList<Patron> aux = Tokenizador.instancias;
        System.out.println("");
    }
}
