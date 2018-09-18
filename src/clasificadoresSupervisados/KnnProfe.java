package clasificadoresSupervisados;

import java.util.ArrayList;
import objetos.Patron;
import objetos.VecinoKnn;
import tools.GeneradorDeInstancias;
import tools.HerramientasClasificadores;
import tools.Tokenizador;

/**
 *
 * @author david
 */
public class KnnProfe implements clasificadorSupervisado {
    private ArrayList<Patron> instancias;
    private double eficacia;
    private ArrayList<String> clases;
    private ArrayList<VecinoKnn> vecinos;
    private int K;
    public static int Contt = 0;

    public KnnProfe(int K) {
        this.K = K;
        this.eficacia = 0;
        this.clases = new ArrayList<>();
        this.vecinos = new ArrayList<>();
    }
    

    @Override
    public void entrena(ArrayList<Patron> instancias) {
        this.instancias = instancias;
        // Para saber cuántas clases están involucradas
        for (Patron instancia : this.instancias) {
            if(!this.clases.contains(instancia.getClaseOriginal())) {
                this.clases.add(instancia.getClaseOriginal());
            }
        }
    }
    
    @Override
    public void clasifica(Patron patron) {
        this.vecinos = new ArrayList<>();
        // Calcular todas las distancias
        for (Patron instancia : this.instancias) {
            String clase = instancia.getClaseOriginal();
            double distancia = 
                HerramientasClasificadores.calcularDistanciaEuclidiana(patron, instancia);
            // Para no considerar el mismo patrón
            if(distancia != 0) {
                this.vecinos.add(new VecinoKnn(distancia, clase));
            }
        }
        // Ordenar las distancias de manera ascendente
        HerramientasClasificadores.ordenar(this.vecinos);
        String resultado = verificarKVecinos();
        patron.setClaseResultante(resultado);
    }
    
    public void clasificaConjunto(ArrayList<Patron> instancias) {
        // Recorremos la colección a clasificar
        int total = instancias.size();
        // Contador de clasificaciones correctos
        int aux = 0;
        for (Patron conejillo : instancias) {
            System.out.println(Contt++);
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
    

    private String verificarKVecinos() {
        int contadores[] = new int[this.clases.size()];
        int i = 0;
        for (VecinoKnn aux : this.vecinos) {
            String clase = aux.getClasePerteneciente();
            i = this.clases.indexOf(clase);
            contadores[i]++;
            if(contadores[i] == this.K) {
                return this.clases.get(i);
            }
        }
        return "Desconocida";
    }
    
    public static void main(String[] args) {   
        Tokenizador.leerDatos();
        // Instanciamos el clasificador supervisado knn
        KnnProfe knn = new KnnProfe(3);
        // Se hace la seleccion de características
        ArrayList<Patron> aux = GeneradorDeInstancias
            .genInstanciasPorCaracteristicas(new byte[]
            {1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1}
        );
        // Se entrena el clasificador
        knn.entrena(aux);
        // Se clasifica con el mismo conjunto de entrenamiento
        knn.clasificaConjunto(aux);
        // Se muestra la eficacia resultante
        System.out.println("Eficacia: " + knn.getEficacia());
//        Tokenizador.leerDatos();
//        // Instanciamos el clasificador supervisado knn
//        KnnProfe knn = new KnnProfe(3);
//        knn.entrena(Tokenizador.instancias);
//        Patron a = new Patron(new double[]
//            {472,-3.0435406239976,-3.15730712090228,1.08846277997285,2.2886436183814,
//            1.35980512966107,-1.06482252298131,0.325574266158614,-0.0677936531906277,
//            -0.270952836226548,-0.838586564582682,-0.414575448285725,-0.503140859566824,
//            0.676501544635863,-1.69202893305906,2.00063483909015,0.666779695901966,
//            0.599717413841732,1.72532100745514,0.283344830149495,2.10233879259444,
//            0.661695924845707,0.435477208966341,1.37596574254306,-0.293803152734021,
//            0.279798031841214,-0.145361714815161,-0.252773122530705,
//            0.0357642251788156,529}, "desconocida");
//        knn.clasifica(a);
//        System.out.println(a.getClaseOriginal() + " " + a.getClaseResultante());
    }
}
