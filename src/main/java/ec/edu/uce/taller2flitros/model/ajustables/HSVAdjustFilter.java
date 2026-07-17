package ec.edu.uce.taller2flitros.model.ajustables;

import java.awt.Color;
import java.awt.image.BufferedImage;

public class HSVAdjustFilter implements ImageFilter {

    private final int brightness;
    private final int saturation;
    private final int alpha;

    public HSVAdjustFilter(int brightness, int saturation, int alpha) {
        this.brightness = brightness;
        this.saturation = saturation;
        this.alpha = alpha;
    }

    @Override
    public BufferedImage apply(BufferedImage input) {
        if (input == null) {
            throw new IllegalArgumentException("La imagen de entrada no puede ser nula");
        }

        int width = input.getWidth();
        int height = input.getHeight();
        BufferedImage output = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int pixel = input.getRGB(x, y);

                int a = (pixel >> 24) & 0xFF;
                int r = (pixel >> 16) & 0xFF;
                int g = (pixel >> 8) & 0xFF;
                int b = pixel & 0xFF;

                // 1. Aplicar Alpha
                float alphaFactor = Math.max(0f, alpha / 255f);
                int newA = clamp255(Math.round(a * alphaFactor));

                // 2. Aplicar Brillo y Saturación
                float[] hsv = Color.RGBtoHSB(r, g, b, null);
                float h = hsv[0];
                float s = hsv[1];
                float v = hsv[2];

                // Saturación
                float saturationFactor = Math.max(0f, saturation / 100f);
                s = clamp01(s * saturationFactor);

                // Brillo
                float brightnessOffset = brightness / 100f;
                v = clamp01(v + brightnessOffset);

                // Reconvertir a RGB
                int rgb = Color.HSBtoRGB(h, s, v);
                int newR = (rgb >> 16) & 0xFF;
                int newG = (rgb >> 8) & 0xFF;
                int newB = rgb & 0xFF;

                int newPixel = (newA << 24) | (newR << 16) | (newG << 8) | newB;
                output.setRGB(x, y, newPixel);
            }
        }

        return output;
    }

    @Override
    public String getName() {
        return "Ajustes HSV Combinados";
    }

    private float clamp01(float value) {
        return Math.max(0f, Math.min(1f, value));
    }

    private int clamp255(int value) {
        return Math.max(0, Math.min(255, value));
    }
}