package ec.edu.uce.taller2flitros.model;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class VolverColorear {

    public static void main(String[] args) {

        // Del Word al pegar la imagen, en color hay la opcion de VolverColorear y ahi
        // estan lso colores que toca poner
        int a, r, g, b;

        File file = new File("images/elefantes.png");
        File newFile = new File("images/elefantesAzul.png");

        try {

            BufferedImage bufer = ImageIO.read(file);
            int ancho = bufer.getWidth();
            int alto = bufer.getHeight();

            BufferedImage bufer2 = new BufferedImage(ancho, alto, BufferedImage.TYPE_INT_ARGB);

            for (int y = 0; y < alto; y++) {
                for (int x = 0; x < ancho; x++) {
                    int pixel = bufer.getRGB(x, y);
                    a = (pixel >> 24) & 0xff;
                    r = (pixel >> 16) & 0xFF;
                    g = (pixel >> 8) & 0xFF;
                    b = (pixel) & 0xFF;

                    r = Math.max(0, r - 100);
                    g = Math.max(0, g - 100);
                    // realzamos el color cuando ponemos min y sumamos
                    b = Math.min(255, b + 100);

                    int pixelNuevo = (a << 24) | (r << 16) | (g << 8) | b;

                    bufer2.setRGB(x, y, pixelNuevo);
                }
            }
            ImageIO.write(bufer2, "png", newFile);
            System.out.println("Imagen creada correctamente");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
