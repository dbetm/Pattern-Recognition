package imagenes;

import clasificadoresNoSupervisado.MinMax;
import java.awt.Color;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import objetos.Patron;

/**
 *
 * @author david
 */
public class ClusterMaxMin {
    // Atributos
    private Image imagenOriginal;
    private MinMax clasificador;
    
    public Image calcularClusters(Image imagen, double umbral) {
        this.imagenOriginal = imagen;
        // Extraer de la imagen la info para generar las instancias
        ArrayList<Patron> instancias = generarInstancias();
        this.clasificador = new MinMax(instancias, 0.2);
        this.clasificador.clasifica(true);
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
    
    public static void main(String []args) {
        double umbral = 0.5;
        Image imagenOriginal = tools.ImageManager.openImage();
        JFrameImagen fo = new JFrameImagen(imagenOriginal);
        fo.setVisible(true);
        fo.setTitle("Umbral: " + umbral);
        ClusterMaxMin cmm = new ClusterMaxMin();
        Image imagenResultante = cmm.calcularClusters(imagenOriginal, umbral);
        JFrameImagen fr = new JFrameImagen(imagenResultante);
        fr.setVisible(true);
        System.out.println("");
    }
}
