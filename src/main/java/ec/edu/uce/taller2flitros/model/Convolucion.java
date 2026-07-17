package ec.edu.uce.taller2flitros.model;

import java.awt.image.BufferedImage;
import java.awt.image.ConvolveOp;
import java.awt.image.Kernel;
import java.io.File;
import javax.imageio.ImageIO;

public class Convolucion {

    private final float[] kernelData;
    private final String name;

    public Convolucion(float[] kernelData, String name) {
        if (kernelData == null || kernelData.length == 0) {
            throw new IllegalArgumentException("El kernel no puede ser nulo o vacío");
        }

        int side = (int) Math.sqrt(kernelData.length);
        if (side * side != kernelData.length) {
            throw new IllegalArgumentException("El kernel debe tener tamaño cuadrado (3x3, 5x5, etc.)");
        }

        this.kernelData = kernelData.clone();
        this.name = name;
    }

    public BufferedImage apply(BufferedImage input) {
        if (input == null) {
            throw new IllegalArgumentException("La imagen de entrada no puede ser nula");
        }

        int side = (int) Math.sqrt(kernelData.length);
        Kernel kernel = new Kernel(side, side, kernelData);
        ConvolveOp op = new ConvolveOp(kernel, ConvolveOp.EDGE_NO_OP, null);

        return op.filter(input, null);
    }

    public String getName() {
        return name;
    }

    public void convolucionKernel() {
        File file = new File("src/main/java/ec/edu/uce/imagenes/Nino.png");
        File file2 = new File("src/main/java/ec/edu/uce/imagenes/ConvolucionKernel.png");

        try {
            BufferedImage bufer = ImageIO.read(file);
            BufferedImage bufer2 = apply(bufer);

            ImageIO.write(bufer2, "png", file2);

            System.out.println("Imagen creada correctamente con Kernel");
        } catch (Exception e) {
            System.out.printf("Error al leer la imagen", e);
        }
    }
}
