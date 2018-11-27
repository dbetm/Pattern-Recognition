package clasificadoresSupervisados;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;
import objetos.Patron;
import tools.ClaseBayes;
import tools.GeneradorDeInstancias;
import tools.Tokenizador;

/**
 *
 * @author david
 */
public class NaiveBayes implements clasificadorSupervisado {
    private ArrayList<Patron> instancias;
    private Map<String, ClaseBayes> clases;
    private double eficacia;
    
    public NaiveBayes() {
        this.clases = new HashMap<String, ClaseBayes>();
        this.eficacia = 0;
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
            //System.out.println("evidencia: " + evidencia);
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
    }
    
    private double calcularDistribucionNormal(double c, double media, double varianza) {
        double res = 1 / Math.sqrt(2 * Math.PI * varianza);
        res *= 1 / Math.exp(Math.pow(c - media, 2) / (2 * varianza));
        return res;
    }
    
    public void clasificaConjunto(ArrayList<Patron> instancias) {
        // Recorremos la colección a clasificación
        int total = instancias.size();
        // Contador de clasificaciones correctos
        int aux = 0;
        for (Patron conejillo : instancias) {
            clasifica(conejillo);
            if(conejillo.getClaseResultante().equals(conejillo.getClaseOriginal())) {
                aux++;
            }
        }
        this.eficacia = (aux * 100) / total;
    }

    public double getEficacia() {
        return eficacia;
    }
    
    
    
    // Pruebas unitarias
    
    public static void main(String []args) {
        Tokenizador.leerDatos();
        NaiveBayes nb = new NaiveBayes();
        ArrayList<Patron> aux = GeneradorDeInstancias
            .genInstanciasPorCaracteristicas(new byte[]
            //{1, 1, 1, 0, 0, 1, 1, 0, 1}
            //{1, 1, 1, 1}
            {1, 1, 0, 1, 0, 0, 0, 0, 0}
        );
        nb.entrena(aux);
        nb.clasificaConjunto(aux);
        System.out.println("Eficacia: " + nb.getEficacia());
        /*
        Map<Double, byte[]> eficacias = new HashMap<>();
        Tokenizador.leerDatos();
        for (int i = 0; i < 500; i++) {
            NaiveBayes nb = new NaiveBayes();
            // Se hace la seleccion de características
            byte[] selec = generarSeleccion(9);
            ArrayList<Patron> aux = GeneradorDeInstancias
                .genInstanciasPorCaracteristicas(selec);
            // Entrenamos el clasificador
            nb.entrena(aux);
            // Se clasifica el mismo conjunto de entrenamiento
            nb.clasificaConjunto(aux);
            // Imprimimos la eficacia
            if(nb.getEficacia() > 0.0) {
                //mostrarComb(selec);
                //System.out.print(" Eficacia: " + nb.getEficacia() + "\n");
                if(!eficacias.containsKey(nb.getEficacia())) {
                    eficacias.put(nb.getEficacia(), selec);
                }
            }
        }
        
        for (Entry<Double, byte[]> entry : eficacias.entrySet()) {
            Double key = entry.getKey();
            byte[] value = entry.getValue();
            mostrarComb(value);
            System.out.print(" Eficacia: " + key + "\n");
        }
        */
    }
    
    
    public static byte[] generarSeleccion(int n) {
        byte s[] = new byte[n]; 
        Random r = new Random();
        for (int i = 0; i < n; i++) {
            s[i] = (byte) r.nextInt(2);
            //System.out.print(s[i] + " ");
        }
        return s;
    }
    
    public static void mostrarComb(byte[] aux) {
       for (int i = 0; i < aux.length; i++) {
            System.out.print(aux[i] + " ");
        } 
    }
}
