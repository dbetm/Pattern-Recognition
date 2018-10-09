package imagenes;

import java.awt.Color;
import objetos.Patron;

/**
 *
 * @author david
 */
public class PatronPixel extends Patron {
    private int x;
    private int y;
    
    // Inicialización por defecto
    public PatronPixel(int dim) {
        super(dim);
    }
    
    public PatronPixel(double[] caracteristicas, String clase, int x, int y) {
        super(caracteristicas, clase);
        this.x = x;
        this.y = y;
    }
    
    public void modificarCaracteristicas() {
        String clase = getClaseOriginal();
        int valorRGB = Integer.parseInt(clase);
        Color color = new Color(valorRGB);
        getCaracteristicas()[0] = color.getRed();
        getCaracteristicas()[1] = color.getGreen();
        getCaracteristicas()[2] = color.getBlue();
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }
    
    public static void main(String []args) {
        Color a = new Color(255, 255, 255);
        System.out.println(a.getRGB());
        //PatronPixel pp = new PatronPixel(new double[]{2, 34, 255}, "Desconocido");
        String Cmeans = "-122222";
        //pp.setClaseOriginal(Cmeans);
        //pp.modificarCaracteristicas();
        
    }
}
