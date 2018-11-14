package clasificadoresSupervisados;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import objetos.Patron;
import tools.ClaseBayes;
import tools.Tokenizador;

/**
 *
 * @author david
 */
public class NaiveBayes implements clasificadorSupervisado {
    private ArrayList<Patron> instancias;
    private Map<String, ClaseBayes> clases;
    
    public NaiveBayes() {
        this.clases = new HashMap<String, ClaseBayes>();
    }

    @Override
    public void entrena(ArrayList<Patron> instancias) {
        this.instancias = instancias;
        // Calcular la proababilidad a priori por cada clase
        this.clases.put(this.instancias.get(0).getClaseOriginal(), 
            new ClaseBayes(this.instancias.get(0).getClaseOriginal()));
        // Recorrer las instancias
        for (Patron patron : instancias) {
            // Verificar si existe una clase bayes con el nombre de la 
            // clase del patrón.
            if(!this.clases.containsKey(patron.getClaseOriginal())) {
                // si lo tiene lo acumula
                this.clases.put(patron.getClaseOriginal(), 
                    new ClaseBayes(patron.getClaseOriginal()));
            }
            this.clases.get(patron.getClaseOriginal())
                .acumularMedia(patron.getCaracteristicas());
        }
        // Se calculan todas las medias
        for (Entry<String, ClaseBayes> aux : this.clases.entrySet()) {
            aux.getValue().calcularMedia();
            // aprovechando el ciclo, se calcula aPriori
            aux.getValue().calcularAPriori(this.instancias.size());
        }
        // Y ahora las varianzas
        for (Patron patron : instancias) {
            String clase = patron.getClaseOriginal();
            this.clases.get(clase).calcularVarianza(patron.getCaracteristicas());
        }
    }

    @Override
    public void clasifica(Patron patron) {
        // Recibir el patrón a clasificar
        // Calcular una matriz de distribuciones normales parametrizada y se 
        // aprovecha el ciclo para ir acumulando la evidencia
        double[][] distribucion = 
            new double[this.clases.size()][patron.getCaracteristicas().length];
        int x = 0;
        // Evidencias
            /* Suma de la multiplicación, por cada clase, la probabilidad aProri, por la 
            distribución normal de cada característica */
        double evidencia = 0;
        for (Entry<String, ClaseBayes> entry : this.clases.entrySet()) {
            Patron media = entry.getValue().getMedia();
            Patron varianza = entry.getValue().getVarianza();
            double producto = entry.getValue().getaPriori();
            for (int c = 0; c < patron.getCaracteristicas().length; c++) {
                distribucion[x][c] = calcularDistribucionNormal(patron.getCaracteristicas()[c], 
                    media.getCaracteristicas()[c], varianza.getCaracteristicas()[c]);
                producto *= distribucion[x][c];
            }
            x++;
            // se suma a la evidencia
            evidencia += producto;
            System.out.println("evidencia: " + evidencia);
        }
            
        // Se calculan las probabilidades a posteriori
        String claseResultante = "desconocida";
        double maxPosteriori = -1;
        x = 0;
        for (Entry<String, ClaseBayes> entry : clases.entrySet()) {
            String key = entry.getKey();
            ClaseBayes value = entry.getValue();
            double producto = value.getaPriori();
            for (int c = 0; c < patron.getCaracteristicas().length; c++) {
                producto *= distribucion[x][c];
            }
            producto /= evidencia;
            // verificamos si la probabilidad a posteriori es mayor a la actual
            if(maxPosteriori < producto) {
                maxPosteriori = producto;
                claseResultante = value.getNombre();
            }
            x++;
        }
        // El argumento máx de las a posteriori define la clase a la que pertenece el patrón
        patron.setClaseResultante(claseResultante);
        System.out.println("");
    }
    
    private double calcularDistribucionNormal(double c, double media, double varianza) {
        double res = 1 / Math.sqrt(2 * Math.PI * varianza);
        res *= 1 / Math.exp(Math.pow(c - media, 2) / (2 * varianza));
        return res;
    }
    
    // Pruebas unitarias
    
    public static void main(String []args) {
        Tokenizador.leerDatos();
        NaiveBayes nb = new NaiveBayes();
        nb.entrena(Tokenizador.instancias);
        Patron patron = new Patron(new double[]{6, 130, 8}, "desconocida");
        nb.clasifica(patron);
        System.out.println("La clase es: " + patron.getClaseResultante());
    }
    
}
