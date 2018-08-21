package tests;

import clasificadoresSupervisados.MinimaDistancia;
import java.util.ArrayList;
import objetos.Patron;
import tools.ClassificationChecker;
import tools.HerramientasClasificadores;
import tools.Tokenizador;

/**
 *
 * @author david
 */
public class TestClasificadorDistanciaMin {
    public static void main(String[] args) {   
        Tokenizador.leerDatos();
        // Instanciamos el clasificador supervisado de distancia mínima
        MinimaDistancia md = new MinimaDistancia();
        md.entrena(Tokenizador.instancias);
        //Patron a = new Patron(new double[]{4.9, 3.0, 1.4, 0.2}, "desconocida");
        //md.clasifica(a);
        //System.out.println(res);
        md.clasificaConjunto(Tokenizador.instancias);
        //System.out.println(ClassificationChecker.calcEficaciaDistMin(md, aux));
        System.out.println("Eficacia: " + md.getEficacia());
    }
}
