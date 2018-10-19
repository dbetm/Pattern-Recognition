package imagenes;

import clasificadoresNoSupervisado.CMeans;
import java.awt.Color;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Random;
import javax.swing.JFrame;
import objetos.Patron;

/**
 *
 * @author david
 */
public class ClusterCMeans {
    private Image imagenOriginal;
    private CMeans clasificador;
    
    public Image calcularClusters(Image imagen, int numClusters) {
        this.imagenOriginal = imagen;
        // Extraer de la imagen la info para generar las instancias
        ArrayList<Patron> instancias = generarInstancias();
        Patron[] centroidesIniciales = 
                calcularPixelesCentroidesIniciales(instancias, numClusters);
        //this.clasificador = new CMeans(instancias, numClusters);
        this.clasificador = new CMeans(instancias, numClusters, 5000);
        this.clasificador.clasifica(centroidesIniciales);
        // Modificamos los colores con base a la clasificación
        for (Patron patron : instancias) {
            PatronPixel aux = (PatronPixel)patron;
            aux.modificarCaracteristicas();
        }
        return generarImagen(instancias);
    }
    

    private ArrayList<Patron> generarInstancias() {
        ArrayList<Patron> instancias = new ArrayList<>();
        // Se neceseitan leer los pixeles de la imagen, y no perder la referencia
        // de su posición
        BufferedImage bi = tools.ImageManager.toBufferedImage(imagenOriginal);
        // Recorre la imagen por pixel
        for (int x = 0; x < bi.getWidth(); x++) {
            for (int y = 0; y < bi.getHeight(); y++) {
                Color color = new Color(bi.getRGB(x, y));
                double r = color.getRed();
                double g = color.getGreen();
                double b = color.getBlue();
                double[] caracteristicas = new double[]{r, g, b};
                PatronPixel px = new PatronPixel(caracteristicas, "desconocida", x, y);
                instancias.add(px);
            }
        }
        return instancias;
    }

    private Image generarImagen(ArrayList<Patron> instancias) {
        BufferedImage imgNueva = new BufferedImage(this.imagenOriginal
                .getWidth(null), this.imagenOriginal.getHeight(null),
                BufferedImage.TYPE_INT_RGB);
        for (Patron patron : instancias) {
            PatronPixel aux = (PatronPixel)patron;
            int rgb = Integer.parseInt(aux.getClaseOriginal());
            imgNueva.setRGB(aux.getX(), aux.getY(), rgb);
        }
        return tools.ImageManager.toImage(imgNueva);
    }

    private Patron[] calcularPixelesCentroidesIniciales(ArrayList<Patron> instancias, int numClusters) {
        Random ran = new Random();
        ArrayList<Color> coloresSeleccionados = new ArrayList<>();
        Color color;
        int pos;
        int sizeInstancias = instancias.size();
        String nombre;
        Patron[] centroidesIniciales = new Patron[numClusters];
        for (int i = 0; i < numClusters; i++) {
            pos = ran.nextInt(sizeInstancias);
            color = new Color((int)instancias.get(pos).getCaracteristicas()[0],
                (int)instancias.get(pos).getCaracteristicas()[1], 
                    (int)instancias.get(pos).getCaracteristicas()[2]);
            // Validar que se seleccione un color distinto
            if(coloresSeleccionados.contains(color)) {
                System.out.println("Color repetido!");
                i--;
                continue;
            }
            coloresSeleccionados.add(color);
            nombre = color.getRGB() + "";
            centroidesIniciales[i] = 
                    new Patron(instancias.get(pos).getCaracteristicas().clone(), nombre);
        }
        return centroidesIniciales;
    }
    
    public static void main(String []args) {
        int c = 5;
        Image imagenOriginal = tools.ImageManager.openImage();
        JFrameImagen fo = new JFrameImagen(imagenOriginal);
        fo.setVisible(true);
        fo.setTitle("Número de clusters: " + c);
        ClusterCMeans ci = new ClusterCMeans();
        Image imagenResultante = ci.calcularClusters(imagenOriginal, c);
        JFrameImagen fr = new JFrameImagen(imagenResultante);
        fr.setVisible(true);
        System.out.println("");
    }
    
}
