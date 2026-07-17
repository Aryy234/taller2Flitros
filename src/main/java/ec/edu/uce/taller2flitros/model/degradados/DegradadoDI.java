package ec.edu.uce.taller2flitros.model.degradados;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;

public class DegradadoDI {
    public void generar(File outputFile, int ancho, int alto, Color c1, Color c2) {
        int r, g, b;
        int r1 = c1.getRed(), g1 = c1.getGreen(), b1 = c1.getBlue();
        int r2 = c2.getRed(), g2 = c2.getGreen(), b2 = c2.getBlue();

        try {
            BufferedImage bufer = new BufferedImage(ancho, alto, BufferedImage.TYPE_INT_RGB);

            for (int x = 0; x < ancho; x++) {
                double t = 1.0 - ((double) x / (ancho - 1));
                
                r = (int) (r1 + t * (r2 - r1));
                g = (int) (g1 + t * (g2 - g1));
                b = (int) (b1 + t * (b2 - b1));
                
                int pixelNuevo = (r << 16) | (g << 8) | b;

                for (int y = 0; y < alto; y++) {
                    bufer.setRGB(x, y, pixelNuevo);
                }
            }

            ImageIO.write(bufer, "png", outputFile);
        } catch (Exception e) {
            System.err.println("Error al crear la imagen: " + e.getMessage());
        }
    }
}
