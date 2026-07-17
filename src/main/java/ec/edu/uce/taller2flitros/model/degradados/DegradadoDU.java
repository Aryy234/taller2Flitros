package ec.edu.uce.taller2flitros.model.degradados;

import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;

public class DegradadoDU {
    public void generar() {
        File file2 = new File("src/main/java/ec/edu/uce/imagenes/degAbArr.jpg");
        int ancho, alto, pixel, pixelNuevo;
        int r, g, b;

        try {

            ancho = 900;
            alto = 400;

            BufferedImage bufer2 = new BufferedImage(ancho, alto, BufferedImage.TYPE_INT_RGB);

            for (int y = 0; y < alto; y++) {
                double t;
                if (y < alto / 2) {
                    t = (double) y / (alto / 2);
                    r = (int) (244 + t * (237 - 244));
                    g = (int) (192 + t * (146 - 192));
                    b = (int) (175 + t * (144 - 175));

                } else {
                    t = (double) (y - alto / 2) / (alto / 2);
                    r = (int) (237 + t * (235 - 237));
                    g = (int) (146 + t * (121 - 146));
                    b = (int) (144 + t * (121 - 144));
                }

                for (int x = 0; x < ancho; x++) {
                    System.out.println("r:" + r + " g:" + g + " b:" + b);

                    pixelNuevo = (r << 16) | (g << 8) | (b << 0);

                    bufer2.setRGB(x, y, pixelNuevo);

                    System.out.println(pixelNuevo);
                }
            }

            ImageIO.write(bufer2, "jpg", file2);

            System.out.println("Imagen creada correctamente");
        } catch (Exception e) {
            System.out.printf("Error al crear la imagen", e);
        }
    }
}
