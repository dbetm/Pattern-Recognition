package tests;

import clasificadoresSupervisados.Knn;
import java.util.ArrayList;
import objetos.Patron;
import tools.ClassificationChecker;
import tools.HerramientasClasificadores;
import tools.Tokenizador;

/**
 *
 * @author david
 */
public class TestClasificadorKnn {
    public static void main(String[] args) {   
        Tokenizador.leerDatos();
        // Instanciamos el clasificador supervisado knn
        Knn knn = new Knn();
        knn.entrena(Tokenizador.instancias);
        //Patron a = new Patron(new double[]{6.7, 3, 5.2, 2.3}, "desconocida");
        //knn.clasifica(a);
        //System.out.println(a.getClaseOriginal() + " " + a.getClaseResultante());
        
        knn.clasificaConjunto(Tokenizador.instancias);
        //System.out.println(ClassificationChecker.calcEficaciaDistMin(md, aux));
        System.out.println("Eficacia: " + knn.getEficacia());
    }
}
