package clasificadoresSupervisados;

import java.util.ArrayList;
import objetos.Patron;
import objetos.PatronRepresentativo;

/**
 *
 * @author david
 */
public class MinimaDistancia implements clasificadorSupervisado {
    private ArrayList<PatronRepresentativo> representativos;
    
    public MinimaDistancia() {
        // Se instancia la colección de patrones representativos
        this.representativos = new ArrayList<>();
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
    }

    @Override
    public void clasifica(Patron patron) {
    }
    
    private void buscaYAcumula(Patron patron) {
        // buscar en la coleccion de represantes
        for (int i = 0; i < this.representativos.size(); i++) {
            //verificamos que exista 
            if (patron.getClaseOriginal().equals(
                this.representativos.get(i).getClaseOriginal())) {
                // contamos
                this.representativos.get(i)
                    .setNumPatrones(this.representativos.get(i).getNumPatrones()+1);
                // acumulamos
                for(int j=0; j<this.representativos.get(i).getCaracteristicas().length; j++) {
                    this.representativos
                        .get(i).getCaracteristicas()[j]+=patron.getCaracteristicas()[j];
                }
                break;
            }
            else {
                // agrega 
                this.representativos.add(new PatronRepresentativo(
                    patron.getCaracteristicas(), patron.getClaseOriginal()));
                break;
            }
        }
       // TODO: ESTA MAL DEL ACUMULADO 
    }
    
}
