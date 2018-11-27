package memoriasAsociativas;
// Este es un clasificador supervisado

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;
import objetos.Patron;
import tools.GeneradorDeInstancias;
import tools.Tokenizador;

/**
 *
 * @author david
 */
public class CAP {
    private ArrayList<Patron> patrones;
    private Map<String, int[]> clases;
    private double vectorMedio[];
    private double C[][];
    private double eficacia;
    
    public CAP(ArrayList<Patron> patrones) {
        this.patrones = patrones;
        this.clases = new LinkedHashMap<String, int[]>();
        compilarClases();
        this.vectorMedio = new double[this.patrones.get(0).getCaracteristicas().length];
        calcularVectorMedio();
        trasladarVectoresEntrada();
        this.C = 
            new double[this.clases.size()][this.patrones.get(0).getCaracteristicas().length];
    }
    
    private void compilarClases() {
        for (int i = 0; i < this.patrones.size(); i++) {
            if(!this.clases.containsKey(this.patrones.get(i).getClaseOriginal())) {
                this.clases.put(this.patrones.get(i).getClaseOriginal(), null);
            }
        }
        // Ahora que tenemos todas las clases, definimos los vectores de salida
        int num = this.clases.size();
        int cont = 0;
        for (Map.Entry<String, int[]> entry : this.clases.entrySet()) {
            entry.setValue(new int[num]);
            entry.getValue()[cont] = 1;
            cont++;
        }
    }
    
    private void calcularVectorMedio() {
        // Se hace la suma
        for (int i = 0; i < this.patrones.size(); i++) {
            for (int j = 0; j < this.vectorMedio.length; j++) {
                this.vectorMedio[j] += this.patrones.get(i).getCaracteristicas()[j];
            }
        }
        // Se divide entre el número de vectores de entrada
        int p = this.patrones.size();
        for (int i = 0; i < this.vectorMedio.length; i++) {
            this.vectorMedio[i] /= p;
        }
    }
    
    private void trasladarVectoresEntrada() {
        for (int i = 0; i < this.patrones.size(); i++) {
            for (int j = 0; j < this.vectorMedio.length; j++) {
                this.patrones.get(i).getCaracteristicas()[j] -= this.vectorMedio[j]; 
            }
        }
    }
    
    private double[] trasladarVector(double vector[]) {
        for (int i = 0; i < this.vectorMedio.length; i++) {
            vector[i] -= this.vectorMedio[i];
        }
        return vector;
    }
    
    public void aprender() {
        for (int i = 0; i < this.patrones.size(); i++) {
            int y[] = this.clases.get(this.patrones.get(i).getClaseOriginal());
            for (int j = 0; j < y.length; j++) {
                if(y[j] == 0) continue;
                // Se ejecuta lo siguiente solo cuando es 1 
                // el j-ésimo componente, y = [0 0 0 1 0]
                double caracteristicas[] =
                    this.patrones.get(i).getCaracteristicas();
                for (int k = 0; k < caracteristicas.length; k++) {
                    C[j][k] += caracteristicas[k];
                }
                break;
            }
        }
    }
    
    public void recuperar(Patron patron) {
        double caracteristicas[] = patron.getCaracteristicas();
        caracteristicas = trasladarVector(caracteristicas);
        double ans[] = new double[this.clases.size()];
        double max = 0;
        int i = 0;
        boolean flag = true; // cambia a false cuando max ya tiene valor
        String claseResultante = "desconocida";
        for (Map.Entry<String, int[]> entry : this.clases.entrySet()) {
            String key = entry.getKey(); // clase
            for (int j = 0; j < caracteristicas.length; j++) {
                ans[i] += C[i][j] * caracteristicas[j];
            }
            if(flag || ans[i] > max) {
                max = ans[i];
                claseResultante = key;
                flag = false;
            }
            i++;
        }
        // Al patrón le seteamos la clase resultante
        patron.setClaseResultante(claseResultante);
        System.out.println(patron.getClaseOriginal() + "-> " + "Clase resultante: " + claseResultante);
    }
    
    public void recuperaConjunto(ArrayList<Patron> patrones) {
        // Recorremos la colección a clasificar
        int total = patrones.size();
        // Contador de clasificaciones correctas
        int correctos = 0;
        for (Patron conejillo : patrones) {
            recuperar(conejillo);
            if(conejillo.getClaseResultante().equals(conejillo.getClaseOriginal())) {
                correctos++;
            }
        }
        this.eficacia = (correctos * 100) / total;
    }
    
    public double getEficacia() {
        return this.eficacia;
    }
    
    // Pruebas unitarias
    public static void main(String []args) {
        
        Tokenizador.leerDatos();
        ArrayList<Patron> aux = GeneradorDeInstancias
            .genInstanciasPorCaracteristicas(new byte[]
            //{1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1} // wine
            //{1, 1, 1, 1}
            {1, 1, 0, 1, 0, 0, 0, 0, 0}
        );
        ArrayList<Patron> conjuntoARecuperar = (ArrayList<Patron>) aux.clone();
        // Instanciamos el clasificador
        CAP cap = new CAP(aux);
        cap.aprender();
        cap.recuperaConjunto(conjuntoARecuperar);
        System.out.println("\n>> Eficacia: " + cap.getEficacia());
        
        /*
        ArrayList<Patron> aux = new ArrayList<>();
        Patron p1 = new Patron(new double[]{3.4, 1.7, 0.5, 1.1}, "clase1");
        Patron p2 = new Patron(new double[]{2.1, 7.8, 11, 4}, "clase1");
        Patron p3 = new Patron(new double[]{3, 2, 6, 6}, "clase1");
        Patron p4 = new Patron(new double[]{12, 13.4, 6.7, 5.3}, "clase2");
        Patron p5 = new Patron(new double[]{10, 14, 5.5, 8.9}, "clase2");
        aux.add(p1);
        aux.add(p2);
        aux.add(p3);
        aux.add(p4);
        aux.add(p5);
        // Instanciamos el clasificador
        CAP cap = new CAP(aux);
        cap.aprender();
        cap.recuperar(new Patron(new double[]{3.4, 1.7, 0.5, 1.1}, "clase1"));
        cap.recuperar(new Patron(new double[]{2.1, 7.8, 11, 4}, "clase1"));
        cap.recuperar(new Patron(new double[]{3, 2, 6, 6}, "clase1"));
        cap.recuperar(new Patron(new double[]{12, 13.4, 6.7, 5.3}, "clase2"));
        cap.recuperar(new Patron(new double[]{10, 14, 5.5, 8.9}, "clase2"));
        */
    }
 }
