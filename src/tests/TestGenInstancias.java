package tests;

import clasificadoresSupervisados.MinimaDistancia;
import java.util.ArrayList;
import objetos.Patron;
import tools.ClassificationChecker;
import tools.GeneradorDeInstancias;
import tools.HerramientasClasificadores;
import tools.Tokenizador;

/**
 *
 * @author david
 */
public class TestGenInstancias {
    public static void main(String[] args) {   
        Tokenizador.leerDatos();
        ArrayList<Patron> aux = GeneradorDeInstancias
            .genInstanciasPorCaracteristicas(new byte[]{0,0,1,1});
        
        System.out.println("");
    }
}
