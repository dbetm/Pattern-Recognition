package clasificadoresSupervisados;

import java.util.ArrayList;
import objetos.Patron;
import objetos.PatronRepresentativo;
import tools.HerramientasClasificadores;

/**
 *
 * @author david
 */
public class MinimaDistancia implements clasificadorSupervisado {
    private ArrayList<PatronRepresentativo> representativos;
    private double eficacia;
    
    public MinimaDistancia() {
        // Se instancia la colección de patrones representativos
        this.representativos = new ArrayList<>();
        this.eficacia = 0;
    }
    
    // Como es un clasificador supervisado que debe implementar los métodos 
    // de la interfaz de todo clasificador supervisado
    @Override
    public void entrena(ArrayList<Patron> instancias) {
        // agregamos el primer representativo
        this.representativos.add(new PatronRepresentativo(
            instancias.get(0).getCaracteristicas(),
            instancias.get(0).getClaseOriginal()));
        // recorrer la coleccion de patrones 
        for (int i = 1; i < instancias.size(); i++){
            Patron patron = instancias.get(i);
            // buscar en los representativos
            buscaYAcumula(patron);
        }
        // Calcular la media
        for (PatronRepresentativo pr: this.representativos) {
            // Recorrremos por características
            for (int i = 0; i < pr.getCaracteristicas().length; i++) {
                pr.getCaracteristicas()[i] /= pr.getNumPatrones();
            }
        }
    }

    @Override
    public void clasifica(Patron patron) {
        // Hipótesis
        double distanciaMenor = HerramientasClasificadores
            .calcularDistanciaEuclidiana(patron, this.representativos.get(0));
        //System.out.println(distanciaMenor);
        patron.setClaseResultante(this.representativos.get(0).getClaseOriginal());
        for (int i = 1; i < this.representativos.size(); i++) {
            double distancia = HerramientasClasificadores.calcularDistanciaEuclidiana(patron,
                this.representativos.get(i));
            //System.out.println(distancia);
            if(distancia < distanciaMenor) {
                distanciaMenor = distancia;
                patron.setClaseResultante(this.representativos.get(i).getClaseOriginal());
            }
        }
    }
    
    public void clasificaConjunto(ArrayList<Patron> instancias) {
        // Recorremos la colección a clasificación
        int total = instancias.size();
        // Contador de clasificaciones correctos
        int aux = 0;
        for (Patron conejillo : instancias) {
            clasifica(conejillo);
            if(conejillo.getClaseResultante().equals(conejillo.getClaseOriginal()))
                aux++;    
        }
        this.eficacia = (aux * 100) / total;
    }

    public double getEficacia() {
        return eficacia;
    }
    
    private void buscaYAcumula(Patron patron) {
        int m = -1;
        // Buscar en la colección de representantes
        for (int i = 0; i < this.representativos.size(); i++) {
            // Verificamos si ya existe
            if(patron.getClaseOriginal().equals(
                this.representativos.get(i).getClaseOriginal())) {
                // Contamos
                this.representativos.get(i).setNumPatrones(
                    this.representativos.get(i).getNumPatrones() + 1);
                // Acumulamos
                for (int j = 0; j < this.representativos.get(i)
                    .getCaracteristicas().length; j++) {
                    this.representativos.get(i).getCaracteristicas()[j] += 
                        patron.getCaracteristicas()[j];
                }
                m = i;
                break;
            }
        }
        if(m == -1) {
            // agrega
            this.representativos.add(new PatronRepresentativo(
                    patron.getCaracteristicas(),
                    patron.getClaseOriginal()
            ));
        }
    }
    
}
