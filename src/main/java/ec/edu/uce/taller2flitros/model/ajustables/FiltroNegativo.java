package ec.edu.uce.taller2flitros.model.ajustables;

import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;

public class FiltroNegativo {

    public void aplicar(File inputFile, File outputFile) {
        int ancho, alto, pixel, pixelNuevo;
        int r, g, b;
        int mascara = 0xFF;

        try {
            BufferedImage bufer = ImageIO.read(inputFile);
            ancho = bufer.getWidth();
            alto = bufer.getHeight();

            // Usamos ARGB para conservar el canal alpha de la imagen original
            BufferedImage bufer2 = new BufferedImage(ancho, alto, BufferedImage.TYPE_INT_ARGB);

            for (int y = 0; y < alto; y++) {
                for (int x = 0; x < ancho; x++) {
                    pixel = bufer.getRGB(x, y);

                    int a = (pixel >> 24) & mascara;
                    r = (pixel >> 16) & mascara;
                    g = (pixel >> 8) & mascara;
                    b = (pixel >> 0) & mascara;

                    // Invertimos los colores
                    r = 255 - r;
                    g = 255 - g;
                    b = 255 - b;

                    pixelNuevo = (a << 24) | (r << 16) | (g << 8) | (b << 0);

                    bufer2.setRGB(x, y, pixelNuevo);
                }
            }

            ImageIO.write(bufer2, "png", outputFile);
        } catch (Exception e) {
            System.err.println("Error al aplicar Filtro Negativo: " + e.getMessage());
        }
    }
}
