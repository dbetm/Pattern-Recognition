/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package clasificadoresSupervisados;

import java.util.ArrayList;
import objetos.Patron;

/**
 *
 * @author david
 */
public interface clasificadorSupervisado {
    // Se entrena con un conjunto de patrones con una clase asignada y [] características
    public void entrena(ArrayList<Patron> instancias);
    // Se le pasa una referencia del patrón
    public void clasifica(Patron patron);
    
}
