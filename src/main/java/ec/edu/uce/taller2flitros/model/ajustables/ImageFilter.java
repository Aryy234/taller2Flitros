package ec.edu.uce.taller2flitros.model.ajustables;

import java.awt.image.BufferedImage;

public interface ImageFilter {
    BufferedImage apply(BufferedImage input);

    String getName();
}