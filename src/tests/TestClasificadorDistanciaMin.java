/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tests;

import clasificadoresSupervisados.MinimaDistancia;
import java.util.ArrayList;
import objetos.Patron;
import tools.Tokenizador;

/**
 *
 * @author david
 */
public class TestClasificadorDistanciaMin {
    public static void main(String[] args) {   
        Tokenizador.leerDatos();
        ArrayList<Patron> aux = Tokenizador.instancias;
        // Instanciamos el clasificador
        MinimaDistancia md = new MinimaDistancia();
        // Se manda llamar el método 'entrena' y se le pasan las instancias.
        md.entrena(Tokenizador.instancias);
        System.out.println();
    }
    
    
}
