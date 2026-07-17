package ec.edu.uce.taller2flitros.model;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;

public class Desvanecimiento_circular {

    public void aplicar(File inputFile, File outputFile) {
        int ancho, alto, pixel;
        int a, r, g, b;
        int mascara = 0xFF;

        try {
            BufferedImage bufer = ImageIO.read(inputFile);
            ancho = bufer.getWidth();
            alto = bufer.getHeight();

            BufferedImage bufer2 = new BufferedImage(ancho, alto, BufferedImage.TYPE_INT_ARGB);
            int centroX = ancho / 2;
            int centroY = alto / 2;
            double maxDist = Math.sqrt(Math.pow(centroX, 2) + Math.pow(centroY, 2));

            for (int y = 0; y < alto; y++) {
                for (int x = 0; x < ancho; x++) {
                    pixel = bufer.getRGB(x, y);

                    // Extraer componentes
                    a = (pixel >> 24) & mascara;
                    r = (pixel >> 16) & mascara;
                    g = (pixel >> 8) & mascara;
                    b = (pixel >> 0) & mascara;

                    double dx = x - centroX;
                    double dy = y - centroY;

                    double dist = Math.sqrt(dx * dx + dy * dy);
                    double t = Math.min(dist / maxDist, 1.0);

                    // Modificar alpha en función de la distancia al centro
                    int newAlpha = (int) (255 * (1.0 - t));
                    
                    // Asegurar que no excedemos el alpha original (útil para imágenes con bordes ya transparentes)
                    a = Math.min(a, newAlpha);

                    pixel = (a << 24) | (r << 16) | (g << 8) | (b << 0);

                    bufer2.setRGB(x, y, pixel);
                }
            }

            ImageIO.write(bufer2, "png", outputFile);

        } catch (Exception e) {
            System.err.println("Error al aplicar Desvanecimiento Circular: " + e.getMessage());
        }
    }
}