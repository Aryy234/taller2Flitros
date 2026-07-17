package ec.edu.uce.taller2flitros.model.ajustables;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;

public class FiltroBlancoyNegro implements ImageFilter {
    @Override
    public BufferedImage apply(BufferedImage input) {
        int ancho, alto, pixel, pixelNuevo;
        int a, r, g, b;
        int mascara = 0b11111111;
        double brillo;

        try {
            BufferedImage bufer = input;
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

                    int nuevoColor;
                    if (brillo > 127) {
                        nuevoColor = 255; // Blanco
                    } else {
                        nuevoColor = 0;
                    }

                    pixelNuevo = (a << 24) | (nuevoColor << 16) | (nuevoColor << 8) | (nuevoColor << 0);

                    bufer2.setRGB(x, y, pixelNuevo);

                }
            }

            System.out.println("Imagen creada correctamente");
            return bufer2;
        } catch (Exception e) {
            System.out.printf("Error al leer la imagen", e);
        }
        return null;
    }

    @Override
    public String getName() {
        return "Blanco y Negro";
    }
}
