package redesNeuronales;

import java.util.ArrayList;
import objetos.Patron;
import tools.NNs;

/**
 *
 * @author david
 */
public class Perceptron implements RedNeuronal{
    private ArrayList<Patron> patrones; // Con estos patrones va a entrenar
    private double bias;                // Factor de convergencia
    private double alfa;                // Factor de aprendizaje
    private double[] w;                 // pesos 
    
    public Perceptron() {
        this.patrones = null;
    }
    
    @Override
    public void entrenar(ArrayList<Patron> patrones, double alfa) {
        this.alfa = alfa; // Factor de aprendizaje
        this.patrones = patrones; // Set con el que se efectua el entrenamiento
        this.bias = 0; // Factor de convergencia, teta
        this.w = NNs.generarPesosIniciales(this.patrones.get(0).getCaracteristicas().length);
        double suma;
        int e; // Para el error, clase esperada - salida
        int y; // Salida
        int t; // Clase esperada
        int correctos = 0;
        int it = patrones.size() + 1;
        while(correctos < it) {
            for (int i = 0; i < this.patrones.size(); i++) {
                suma = NNs.calcularProductoPunto(this.patrones.get(i).getCaracteristicas(), this.w);
                y = NNs.escalonUnitario(suma - this.bias);
                t = Integer.parseInt(this.patrones.get(i).getClaseOriginal());
                if(y != t) {
                    e = t - y;
                    // Vector de características que se va a iterar para actualizar los pesos
                    double v[] = this.patrones.get(i).getCaracteristicas();
                    for (int j = 0; j < v.length; j++) {
                        this.w[j] += this.alfa * e * v[j]; // w' = w + alpha(t-y)v
                    }
                    this.bias += this.alfa * e * (-1);
                    correctos = 0;
                }
                else correctos++;
                //System.out.println("correctos: " + correctos);
            }
        }
    }
    
    @Override
    public void entrenarConReglaDelta(ArrayList<Patron> patrones, double alfa) {
        this.patrones = patrones;
        this.alfa = alfa;
        this.bias = 0; // Factor de convergencia, teta
        this.w = NNs.generarPesosIniciales(this.patrones.get(0).getCaracteristicas().length);
        double suma;
        int e;
        int y; // Salida
        int t; // Clase esperada
        int numPatrones = this.patrones.size();
        double numCarac = this.patrones.get(0).getCaracteristicas().length;
        // Para el control del error cuadrático medio
        double errorAnterior;
        double error = 10000;
        int epoca = 0;
        // Comienza el ciclo iterativo que terminará cuando abs(errorAnterior-error) < 0.0001
        do {
            errorAnterior = error;
            error = 0;
            for (int i = 0; i < numPatrones; i++) {
                suma = NNs.calcularProductoPunto(this.patrones.get(i).getCaracteristicas(), this.w);
                y = NNs.escalonUnitario(suma - this.bias);
                t = Integer.parseInt(this.patrones.get(i).getClaseOriginal());
                e = t - y;
                error = error + (1 / numCarac) * (Math.pow(e, 2));
                // Vector de características que se va a iterar para actualizar los pesos
                double v[] = this.patrones.get(i).getCaracteristicas();
                for (int j = 0; j < v.length; j++) {
                    this.w[j] += this.alfa * e * v[j]; // w' = w + alpha(t-y)v
                }
                this.bias += this.alfa * e * (-1);
            }
        } while(Math.abs(errorAnterior - error) > 0.0001);
    }
    
    @Override
    public int predecir(Patron patron) {
        double suma = NNs.calcularProductoPunto(patron.getCaracteristicas(), this.w);
        int y = NNs.escalonUnitario(suma - this.bias);
        patron.setClaseResultante(String.valueOf(y));
        return y;
    }
    
    @Override
    public void clasificaConjunto(ArrayList<Patron> instancias) {
        int total= instancias.size();
        for (int i = 0; i < total; i++) {
            predecir(instancias.get(i));
            System.out.println("Clase original: " + instancias.get(i).getClaseOriginal() +
                " Clase resultante: " + instancias.get(i).getClaseResultante());
        }
    }
    
    public static void main(String args[]) {
        // Se instancia el perceptron
        Perceptron perceptron = new Perceptron();
        
        // ### Set de patrones para el OR
        System.out.println("OR");
        Patron p1 = new Patron(new double[]{0, 0}, "0");
        Patron p2 = new Patron(new double[]{0, 1}, "1");
        Patron p3 = new Patron(new double[]{1, 0}, "1");
        Patron p4 = new Patron(new double[]{1, 1}, "1");
        ArrayList<Patron> setOR = new ArrayList<>();
        setOR.add(p1);
        setOR.add(p2);
        setOR.add(p3);
        setOR.add(p4);
        // Se entrena con un factor de aprendizaje de 0.2
        perceptron.entrenarConReglaDelta(setOR, 0.5);
        // Ahora ya debe ser capaz de resolver el OR
        perceptron.clasificaConjunto(setOR);
        
        System.out.println("\nAND");
        // ### Ahora se entrena para el AND
        Patron pa = new Patron(new double[]{0, 0}, "0");
        Patron pb = new Patron(new double[]{0, 1}, "0");
        Patron pc = new Patron(new double[]{1, 0}, "0");
        Patron pd = new Patron(new double[]{1, 1}, "1");
        ArrayList<Patron> setAND = new ArrayList<>();
        setAND.add(pa);
        setAND.add(pb);
        setAND.add(pc);
        setAND.add(pd);
        perceptron.entrenarConReglaDelta(setAND, 0.1);
        // Ahora ya debe ser capaz de resolver el OR
        perceptron.clasificaConjunto(setAND);
        
        System.out.println("\nX");
        // ### Ahora se entrena para el AND
        Patron pw = new Patron(new double[]{0, 0}, "0");
        Patron px = new Patron(new double[]{1, 0}, "1");
        Patron py = new Patron(new double[]{1, 0}, "1");
        Patron pz = new Patron(new double[]{1, 1}, "0");
        ArrayList<Patron> setX = new ArrayList<>();
        setX.add(pw);
        setX.add(px);
        setX.add(py);
        setX.add(pz);
        perceptron.entrenarConReglaDelta(setX, 0.3);
        // Ahora ya debe ser capaz de resolver el OR
        perceptron.clasificaConjunto(setX);
    }
}
