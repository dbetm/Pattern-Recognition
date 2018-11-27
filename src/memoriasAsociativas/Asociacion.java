/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package memoriasAsociativas;

/**
 *
 * @author david
 */
public class Asociacion {
    private int[] entrada; // X
    private int[] salida;  // Y

    public Asociacion(int[] entrada, int[] salida) {
        this.entrada = entrada;
        this.salida = salida;
    }

    public int[] getEntrada() {
        return entrada;
    }

    public void setEntrada(int[] entrada) {
        this.entrada = entrada;
    }

    public int[] getSalida() {
        return salida;
    }

    public void setSalida(int[] salida) {
        this.salida = salida;
    }  
}
