package redesNeuronales;

import java.util.ArrayList;
import objetos.Patron;
import tools.NNs;

/**
 *
 * @author david
 */
public class PerceptronXOR implements RedNeuronal {
    private Perceptron perceptron1;
    private Perceptron perceptron2;
    private ArrayList<Patron> patrones; // Con estos patrones va a entrenar
    private double bias;                // Factor de convergencia
    private double alfa;                // Factor de aprendizaje
    private double[] w;                 // pesos 
    
    public PerceptronXOR() {
        this.patrones = null;
        this.perceptron1 = new Perceptron(); // la entrenamos con OR
        this.perceptron2 = new Perceptron(); // la entrenamos con AND
    }
    
    @Override
    public void entrenar(ArrayList<Patron> patrones, double alfa) {
        this.alfa = alfa;
        this.patrones = patrones;
        generarCapaOculta(1);
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
                // generamos el vector output, con la salidas de los perceptrones
                int y1 = this.perceptron1.predecir(this.patrones.get(i)); // or
                int y2 = this.perceptron2.predecir(this.patrones.get(i)); // and
                double[] output = new double[]{y1, y2};
                suma = NNs.calcularProductoPunto(output, this.w);
                y = NNs.escalonUnitario(suma - this.bias);
                t = Integer.parseInt(this.patrones.get(i).getClaseOriginal());
                if(y != t) {
                    e = t - y;
                    // Vector de características que se va a iterar para actualizar los pesos
                    double v[] = output;
                    for (int j = 0; j < v.length; j++) {
                        this.w[j] += this.alfa * e * v[j]; // w' = w + alpha(t-y)v
                    }
                    this.bias += this.alfa * e * (-1);
                    correctos = 0;
                }
                else correctos++;
            }
        }   
    }
    
    @Override
    public void entrenarConReglaDelta(ArrayList<Patron> patrones, double alfa) {
        this.alfa = alfa;
        this.patrones = patrones;
        generarCapaOculta(2);
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
                // generamos el vector output, con la salidas de los perceptrones
                int y1 = this.perceptron1.predecir(this.patrones.get(i)); // or
                int y2 = this.perceptron2.predecir(this.patrones.get(i)); // and
                double[] output = new double[]{y1, y2};
                suma = NNs.calcularProductoPunto(output, this.w);
                y = NNs.escalonUnitario(suma - this.bias);
                t = Integer.parseInt(this.patrones.get(i).getClaseOriginal());
                e = t - y;
                error += 0.5 * (Math.pow(e, 2.0));
                // Vector de características que se va a iterar para actualizar los pesos
                // Que en realidad es la salida de la capa oculta
                double v[] = output.clone();
                for (int j = 0; j < v.length; j++) {
                    this.w[j] += this.alfa * e * v[j]; // w' = w + alpha(t-y)v
                }
                this.bias += this.alfa * e * (-1);
            }
            //System.out.println("Epoca: " + epoca++);
            //System.out.println("Error anterior: " + errorAnterior + 
            //    " Error actual: " + error);
        } while(Math.abs(errorAnterior - error) > 0.0001);
    }
    
    private void generarCapaOculta(int opt) {
        // AND
        ArrayList<Patron> setAND = new ArrayList<>();
        setAND.add(new Patron(new double[]{0, 0}, "0"));
        setAND.add(new Patron(new double[]{0, 1}, "0"));
        setAND.add(new Patron(new double[]{1, 0}, "0"));
        setAND.add(new Patron(new double[]{1, 1}, "1"));
        
        // OR 
        ArrayList<Patron> setOR = new ArrayList<>();
        setOR.add(new Patron(new double[]{0, 0}, "0"));
        setOR.add(new Patron(new double[]{0, 1}, "1"));
        setOR.add(new Patron(new double[]{1, 0}, "1"));
        setOR.add(new Patron(new double[]{1, 1}, "1"));
        
        // Se llaman los métodos de entrenamiento, dependiendo de opt
        if(opt == 1) {
            this.perceptron1.entrenar(setOR, this.alfa);
            this.perceptron2.entrenar(setAND, this.alfa);
        }
        else {
            this.perceptron1.entrenarConReglaDelta(setOR, this.alfa);
            this.perceptron2.entrenarConReglaDelta(setAND, this.alfa); 
        }
    }
    
    @Override
    public int predecir(Patron patron) {
        int y1 = this.perceptron1.predecir(patron);
        int y2 = this.perceptron2.predecir(patron);
        
        double suma = NNs.calcularProductoPunto(new double[]{y1, y2}, this.w);
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
        PerceptronXOR pxor = new PerceptronXOR();
        // ### Set de patrones para el XOR
        System.out.println("XOR");
        Patron p1 = new Patron(new double[]{0, 0}, "0");
        Patron p2 = new Patron(new double[]{0, 1}, "1");
        Patron p3 = new Patron(new double[]{1, 0}, "1");
        Patron p4 = new Patron(new double[]{1, 1}, "0");
        ArrayList<Patron> setXOR = new ArrayList<>();
        setXOR.add(p1);
        setXOR.add(p2);
        setXOR.add(p3);
        setXOR.add(p4);
        pxor.entrenarConReglaDelta(setXOR, 0.8);
        // Ahora ya debe ser capaz de resolver el XOR
        pxor.clasificaConjunto(setXOR);
        /*
        // Se entrena con un factor de aprendizaje de 0.2
        for (int i = 0; i < 10; i++) {
            pxor.entrenarConReglaDelta(setXOR, 0.2);
            // Ahora ya debe ser capaz de resolver el OR
            pxor.clasificaConjunto(setXOR);
            System.out.println("\n");
        }
        */
    }
}
