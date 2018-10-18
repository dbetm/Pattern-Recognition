package clasificadoresNoSupervisado;

import java.util.ArrayList;
import objetos.Patron;
import objetos.PatronRepresentativo;
import tools.Grafica;
import tools.HerramientasClasificadores;
import tools.Punto;
import tools.Tokenizador;

/**
 *
 * @author david
 */
public class MinMax {
    // ### ATRIBUTOS ###
    // Conjunto de instancias originales
    private ArrayList<Patron> instancias;
    // Conjunto de instancias a trabajar
    private ArrayList<Patron> patrones;
    // Los representativos, inicialmente hay 2
    private ArrayList<Patron> representativos;
    // Media entre los 2 primeros representativos, que son los más alejados entre sí
    private double media;
    // Umbral, es para sensibilizar la clasificación
    private double umbral;
    // Argumento máx. de las distancias mínimas
    private double maxMin;
    
    public MinMax(ArrayList<Patron> instancias, double umbral) {
        this.instancias = instancias;
        this.umbral = umbral;
        this.representativos = new ArrayList<>();
        this.patrones = (ArrayList<Patron>) this.instancias.clone();
        this.media = 0;
        this.maxMin = 0;
    }
    
    public void clasifica() {
        obtenerPrimerosRepresentantes();
        this.media = HerramientasClasificadores
            .calcularDistanciaEuclidiana(this.representativos.get(0),
                this.representativos.get(1));
        // Se va iterar mientras no se cumpla:
        //  maxMin < umbral * media aritmética
        do {
            int indexPatronMaxMin = obtenerIndexPatronMaxMin();
            System.out.println("");
            if(this.maxMin < this.umbral*this.media) break;
            // Se agrega a los representantes el patrón MaxMin
            this.representativos.add(this.patrones.get(indexPatronMaxMin));
            // Se agrega su clase
            int ultimo = this.representativos.size() - 1;
            this.representativos.get(ultimo).setClaseOriginal("class" + ultimo);
            // Se elimina de los patrones
            this.patrones.remove(indexPatronMaxMin);
        }while(true);
        // Se hace el etiquetado
        etiquetar();
    }
    
    // En este método se calcula maxMin, y retorna el id del patrón que corresponde
    private int obtenerIndexPatronMaxMin() {
        int dimRepresentativos = this.representativos.size();
        int index = 0;
        this.maxMin = 0;
        for (int i = 0; i < this.patrones.size(); i++) {
            // Hipótesis, es con respecto al primer representante
            double minDistancia = HerramientasClasificadores
                .calcularDistanciaEuclidiana(this.patrones.get(i), 
                    this.representativos.get(0));
            // Se comprueba la anterior hipótesis, si no se actualiza
            for (int j = 1; j < dimRepresentativos; j++) {
                double aux = HerramientasClasificadores
                .calcularDistanciaEuclidiana(this.patrones.get(i), 
                    this.representativos.get(j));
                if(aux < minDistancia) minDistancia = aux;
            }
            if(minDistancia > this.maxMin) {
                this.maxMin = minDistancia;
                // se guarda/actualiza el índice del patrón
                index = i;
            }
        }
        return index;
    }
    
    private void obtenerPrimerosRepresentantes() {
        // Para calcular los dos primeros representantes
        // 1) Se obtiene el representativo de todos
        Patron patronRepresentantivo = obtenerRepresentativo();
        // 2) Se busca el primer representante => el más alejado del representativo
        int indexPrimer = buscarMasAlejado(patronRepresentantivo);
        this.representativos.add(this.instancias.get(indexPrimer));
        this.representativos.get(0).setClaseOriginal("class0");
        // 3) Ahora se busca el segundo representante => el más alejado del primero
        int indexSegundo = buscarMasAlejado(this.instancias.get(indexPrimer));
        this.representativos.add(this.instancias.get(indexSegundo));
        this.representativos.get(1).setClaseOriginal("class1");
    }
    
    // Busca el patrón más alejado de A, considerando todas las instancias
    private int buscarMasAlejado(Patron A) {
        // La hipótesis es que es el primero, índice 0
        int index = 0;
        double mayor = HerramientasClasificadores
            .calcularDistanciaEuclidiana(A, this.instancias.get(0));
        for (int i = 1; i < this.instancias.size(); i++) {
            double aux = HerramientasClasificadores
                .calcularDistanciaEuclidiana(A, this.instancias.get(i));
            if(aux > mayor) {
                mayor = aux;
                index = i;
            }
        }
        return index;
    }
    
    private Patron obtenerRepresentativo() {
        int n = this.instancias.size();
        Patron representativo = 
            new Patron(this.instancias.get(0).getCaracteristicas(), "desconocida");
        // Acumulamos todas las característica en el representativo
        for (int i = 1; i < n; i++) {
            representativo.setCaracteristicas(sumaVectores(
                this.instancias.get(i).getCaracteristicas(),
                    representativo.getCaracteristicas()));
        }
        // Dividimos cada característica del representativo entre el total de
        // instancias
        for (int i = 0; i < representativo.getCaracteristicas().length; i++) {
            representativo.getCaracteristicas()[i] /= n;
        }
        
        return representativo;
    }
    
    private double[] sumaVectores(double[] A, double[] B) {
        double aux[] = new double[A.length];
        // Suma las características de ambos
        for (int i = 0; i < aux.length; i++) {
            aux[i] = A[i] + B[i];
        }
        return aux;
    }
    
    private void etiquetar() {
        double distancia;
        int index;
        for (int i = 0; i < this.instancias.size(); i++) {
            // Hipótesis, la instancia está más cerca al primer representantes
            index = 0;
            distancia = HerramientasClasificadores
                .calcularDistanciaEuclidiana(this.instancias.get(i), 
                    this.representativos.get(index));
            // Se confirma o actualiza
            for (int j = 1; j < this.representativos.size(); j++) {
                double aux = HerramientasClasificadores
                    .calcularDistanciaEuclidiana(this.instancias.get(i), 
                        this.representativos.get(j));
                if(aux < distancia) {
                    index = j;
                    distancia = aux;
                }
            }
            // Se le setea la clase de su representante más cercano
            this.instancias.get(i).setClaseOriginal(
                this.representativos.get(index).getClaseOriginal());
        }
    }

    public ArrayList<Patron> getRepresentativos() {
        return representativos;
    }
    
    
    
    public static void main(String []args) {
        // Instanciamos 4 patrones
//        ArrayList<Patron> instancias = new ArrayList<>();
//        instancias.add(new Patron(new double[]{1, 2}, "desconocida"));
//        instancias.add(new Patron(new double[]{4, 5}, "desconocida"));
//        instancias.add(new Patron(new double[]{7, 8}, "desconocida"));
//        instancias.add(new Patron(new double[]{10, 11}, "desconocida"));
        
        Tokenizador.leerDatos();
        
        MinMax mm = new MinMax(Tokenizador.instancias, 0.5);
        mm.clasifica();
        
        Grafica grafica = new Grafica("Clasificación", "char_1", "char2");
        
        ArrayList<Patron> representantes = mm.getRepresentativos();
        for (int i = 0; i < representantes.size(); i++) {
            grafica.agregarSerie(representantes.get(i).getClaseOriginal());
        }
        
        Punto punto;
        for (Patron patron : Tokenizador.instancias) {
            // Se genera un punto con las dos características del patrón
            punto = new Punto(patron.getCaracteristicas()[0], 
                patron.getCaracteristicas()[1]);
            grafica.agregarPunto(patron.getClaseOriginal(), punto);
        }
        
        // Se crea la gráfica con los puntos
        grafica.crearGraficaPuntos();
    }
}
