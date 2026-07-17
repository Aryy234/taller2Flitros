package ec.edu.uce.taller2flitros.model;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
// Trigger IDE re-parse

public class EscalaGrises {
    public void escalaAGrises(File file, File file2, int n) {
        int ancho, alto, pixel, pixelNuevo;
        int a, r, g, b;
        int mascara = 0b11111111;

        double factor = 255.0 / (n - 1);
        int gris;

        try {
            BufferedImage bufer = ImageIO.read(file);
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

                    gris = (int) (0.2126 * r + 0.7152 * g + 0.0722 * b);

                    // 2. Cuantización a N niveles para los canales restantes
                    int grisN = (int) (Math.round(gris / factor) * factor);

                    pixelNuevo = (a << 24) | (grisN << 16) | (grisN << 8) | (grisN << 0);

                    bufer2.setRGB(x, y, pixelNuevo);

                }
            }

            ImageIO.write(bufer2, "png", file2);
            pixel = bufer.getRGB(200, 200);

            System.out.println("Imagen creada correctamente");
        } catch (Exception e) {
            System.out.printf("Error al leer la imagen", e);
        }
    }
}
