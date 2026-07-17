package ec.edu.uce.taller2flitros.model.degradados;

import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;

public class DegradadoRadial {
    public void generar() {
        File file2 = new File("src/main/java/ec/edu/uce/imagenes/degRadial.jpg");
        int ancho, alto, pixel, pixelNuevo;
        int r, g, b;

        try {

            ancho = 900;
            alto = 400;

            int centroX = ancho / 2;
            int centroY = alto / 2;

            // Distancia máxima (esquina más lejana)
            double maxDist = Math.sqrt(Math.pow(centroX, 2) + Math.pow(centroY, 2));

            BufferedImage bufer2 = new BufferedImage(ancho, alto, BufferedImage.TYPE_INT_RGB);

            for (int y = 0; y < alto; y++) {
                for (int x = 0; x < ancho; x++) {

                    double dx = x - centroX;
                    double dy = y - centroY;

                    double dist = Math.sqrt(dx * dx + dy * dy);
                    double t = dist / maxDist;

                    // Limitar entre 0 y 1
                    t = Math.min(t, 1.0);
                    if (t < 0.5) {
                        // Inicio → medio
                        double t2 = t * 2;

                        r = (int) (235 + t2 * (237 - 235));
                        g = (int) (121 + t2 * (146 - 121));
                        b = (int) (121 + t2 * (144 - 121));

                    } else {
                        // Medio → final
                        double t2 = (t - 0.5) * 2;

                        r = (int) (237 + t2 * (244 - 237));
                        g = (int) (146 + t2 * (192 - 146));
                        b = (int) (144 + t2 * (175 - 144));
                    }

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
