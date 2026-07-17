package ec.edu.uce.taller2flitros.model;

import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;

public class Vidrio_esmerilado {

    public void crearVidrio(File file, File file2) {
        int ancho, alto, pixel, pixelNuevo;
        int a, r, g, b;
        int mascara = 0b11111111;
        double brillo;

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

                    brillo = 0.299 * r + 0.587 * g + 0.114 * b;

                    a = (int) (50 + (brillo / 255) * (205));

                    pixelNuevo = (a << 24) | (r << 16) | (g << 8) | (b << 0);

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
