/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tests;

import clasificadoresSupervisados.MinimaDistancia;
import java.util.ArrayList;
import objetos.Patron;
import tools.HerramientasClasificadores;
import tools.Tokenizador;

/**
 *
 * @author david
 */
public class TestClasificadorDistanciaMin {
    public static void main(String[] args) {   
        Tokenizador.leerDatos();
        ArrayList<Patron> aux = Tokenizador.instancias;
        double res = HerramientasClasificadores.calcularDistanciaEuclidiana(
            Tokenizador.instancias.get(0), Tokenizador.instancias.get(1));
        // Instanciamos el clasificador supervisado de distancia mínima
        MinimaDistancia md = new MinimaDistancia();
        md.entrena(Tokenizador.instancias);
        Patron a = new Patron(new double[]{4.9, 3.0, 1.4, 0.2}, "desconocida");
        md.clasifica(a);
        System.out.println(res);
    }
    
    
}
