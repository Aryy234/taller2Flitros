package ec.edu.uce.taller2flitros.model;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;

public class Dos_Canales {
    
    public void aplicar(File inputFile, File outputFile, int n, boolean dosCanales) {
        int ancho, alto, pixel, pixelNuevo;
        int a, r, g, b;
        int mascara = 0xFF;
        double factor = 255.0 / (n - 1);

        try {
            BufferedImage bufer = ImageIO.read(inputFile);
            ancho = bufer.getWidth();
            alto = bufer.getHeight();

            BufferedImage bufer2 = new BufferedImage(ancho, alto, BufferedImage.TYPE_INT_ARGB);

            for (int y = 0; y < alto; y++) {
                for (int x = 0; x < ancho; x++) {
                    pixel = bufer.getRGB(x, y);

                    a = (pixel >> 24) & mascara;
                    r = (pixel >> 16) & mascara;
                    g = (pixel >> 8) & mascara;
                    b = (pixel >> 0) & mascara;

                    if (dosCanales) {
                        g = 0; // Descartamos el canal verde para "2 canales"
                    } else {
                        // Cuantización a N niveles para canal verde en retro normal
                        if (g > 0)
                            g = (int) (Math.round(g / factor) * factor);
                    }

                    // Cuantización a N niveles para canales rojo y azul
                    if (r > 0)
                        r = (int) (Math.round(r / factor) * factor);
                    if (b > 0)
                        b = (int) (Math.round(b / factor) * factor);

                    // Aseguramos que los valores estén en el rango 0-255
                    r = Math.min(255, Math.max(0, r));
                    g = Math.min(255, Math.max(0, g));
                    b = Math.min(255, Math.max(0, b));

                    pixelNuevo = (a << 24) | (r << 16) | (g << 8) | (b << 0);

                    bufer2.setRGB(x, y, pixelNuevo);
                }
            }

            ImageIO.write(bufer2, "png", outputFile);
        } catch (Exception e) {
            System.err.println("Error al aplicar Filtro Retro: " + e.getMessage());
        }
    }
}
