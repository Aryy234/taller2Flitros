package ec.edu.uce.taller2flitros.model.ajustables;

import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;

public class FiltroGrisesLuminancia {

    public void aplicar(File inputFile, File outputFile) {
        int ancho, alto, pixel, pixelNuevo;
        int r, g, b;
        int mascara = 0xFF;
        int gris;

        try {
            BufferedImage bufer = ImageIO.read(inputFile);
            ancho = bufer.getWidth();
            alto = bufer.getHeight();

            // Preservamos el canal alpha si es necesario, pero en escala de grises estándar a menudo se descarta. 
            // Para mantener consistencia con los demás filtros usamos ARGB.
            BufferedImage bufer2 = new BufferedImage(ancho, alto, BufferedImage.TYPE_INT_ARGB);

            for (int y = 0; y < alto; y++) {
                for (int x = 0; x < ancho; x++) {
                    pixel = bufer.getRGB(x, y);

                    int a = (pixel >> 24) & mascara;
                    r = (pixel >> 16) & mascara;
                    g = (pixel >> 8) & mascara;
                    b = (pixel >> 0) & mascara;

                    gris = (int) (0.2126 * r + 0.7152 * g + 0.0722 * b);

                    pixelNuevo = (a << 24) | (gris << 16) | (gris << 8) | (gris << 0);

                    bufer2.setRGB(x, y, pixelNuevo);
                }
            }

            ImageIO.write(bufer2, "png", outputFile);
        } catch (Exception e) {
            System.err.println("Error al aplicar Filtro Grises Luminancia: " + e.getMessage());
        }
    }
}
