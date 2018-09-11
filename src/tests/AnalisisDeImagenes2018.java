package tests;


import gui.ImageJFrame;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.image.BufferedImage;
import tools.ImageManager;

/**
 *
 * @author Roberto Cruz Leija
 */
public class AnalisisDeImagenes2018 {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        Image imagenO = ImageManager.openImage();
        ImageJFrame frame1 = new ImageJFrame(imagenO);
    }
    
}
