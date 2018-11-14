package tools;

import objetos.Patron;

/**
 *
 * @author david
 */
public class ClaseBayes {
    private String nombre;
    private Patron media;
    private Patron varianza;
    private int numInstancias;
    private double aPriori;

    public ClaseBayes(String nombre) {
        this.nombre = nombre;
        this.numInstancias = 0;
        this.media = null;
        this.aPriori = 0.0;
        this.varianza = null;
    }
    
    public void acumularMedia(double[] patron) {
        if(this.media == null) {
            this.media = new Patron(patron.length);
            this.media.setClaseOriginal(this.nombre);
        }
        // Aumentamos el contador de instancias
        this.numInstancias++;
        // Directamente acumula
        for (int i = 0; i < patron.length; i++) {
            this.media.getCaracteristicas()[i] += patron[i];
        }
    }
    
    public void calcularMedia() {
        for (int i = 0; i < this.media.getCaracteristicas().length; i++) {
            this.media.getCaracteristicas()[i] /= this.numInstancias;
        }
    }
    
    public void calcularVarianza(double[] patron) {
        if(this.varianza == null) {
            this.varianza = new Patron(patron.length);
            this.varianza.setClaseOriginal(this.nombre);
        }
        // Directamente acumula
        for (int i = 0; i < patron.length; i++) {
            // resta
            double base = patron[i] - this.media.getCaracteristicas()[i];
            double v = Math.pow(base, 2);
            this.varianza.getCaracteristicas()[i] += v/(this.numInstancias - 1);
        }
    }
    
    public void calcularAPriori(double totalInstancias) {
        this.aPriori = this.numInstancias / totalInstancias;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Patron getMedia() {
        return media;
    }

    public void setMedia(Patron media) {
        this.media = media;
    }

    public Patron getVarianza() {
        return varianza;
    }

    public void setVarianza(Patron varianza) {
        this.varianza = varianza;
    }

    public int getNumInstancias() {
        return numInstancias;
    }

    public void setNumInstancias(int numInstancias) {
        this.numInstancias = numInstancias;
    }

    public double getaPriori() {
        return aPriori;
    }

    public void setaPriori(double aPriori) {
        this.aPriori = aPriori;
    }
}
