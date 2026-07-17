package ec.edu.uce.taller2flitros.service;

import ec.edu.uce.taller2flitros.model.Convolucion;
import ec.edu.uce.taller2flitros.model.Desvanecimiento_circular;
import ec.edu.uce.taller2flitros.model.Dos_Canales;
import ec.edu.uce.taller2flitros.model.Vidrio_esmerilado;
import ec.edu.uce.taller2flitros.model.ajustables.FiltroBlancoyNegro;
import ec.edu.uce.taller2flitros.model.ajustables.FiltroGrisesLuminancia;
import ec.edu.uce.taller2flitros.model.ajustables.FiltroNegativo;
import ec.edu.uce.taller2flitros.model.ajustables.HSVAdjustFilter;
import ec.edu.uce.taller2flitros.model.kerlnes;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Random;

public class ImageFilterService {

    private final Random random = new Random();

    public File copyImage(File inputFile) throws IOException {
        File outputFile = createTempImage();
        BufferedImage input = ImageIO.read(inputFile);
        ImageIO.write(input, "png", outputFile);
        return outputFile;
    }

    public File randomImage() throws IOException {
        int width = 900;
        int height = 600;
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);

        Graphics2D g = image.createGraphics();
        g.setPaint(new GradientPaint(0, 0, new Color(247, 242, 235), width, height, new Color(124, 158, 189)));
        g.fillRect(0, 0, width, height);

        for (int i = 0; i < 12; i++) {
            int x = random.nextInt(width);
            int y = random.nextInt(height);
            int size = 60 + random.nextInt(180);
            g.setColor(new Color(random.nextInt(255), random.nextInt(255), random.nextInt(255), 140));
            g.fillOval(x, y, size, size);
        }

        g.dispose();

        File outputFile = createTempImage();
        ImageIO.write(image, "png", outputFile);
        return outputFile;
    }

    public File applyGrayscale(File inputFile) throws IOException {
        File outputFile = createTempImage();
        new FiltroGrisesLuminancia().aplicar(inputFile, outputFile);
        return outputFile;
    }

    public File applyBlackWhite(File inputFile) throws IOException {
        BufferedImage image = read(inputFile);
        BufferedImage output = new FiltroBlancoyNegro().apply(image);
        return write(output);
    }

    public File applyNegative(File inputFile) throws IOException {
        File outputFile = createTempImage();
        new FiltroNegativo().aplicar(inputFile, outputFile);
        return outputFile;
    }

    public File applyRetro1(File inputFile) throws IOException {
        return applyChannelQuantization(inputFile, 6, false);
    }

    public File applyRetro2(File inputFile) throws IOException {
        return applyChannelQuantization(inputFile, 8, true);
    }

    public File applyGrayscaleHsv(File inputFile) throws IOException {
        BufferedImage input = read(inputFile);
        BufferedImage output = new BufferedImage(input.getWidth(), input.getHeight(), BufferedImage.TYPE_INT_ARGB);
        for (int y = 0; y < input.getHeight(); y++) {
            for (int x = 0; x < input.getWidth(); x++) {
                int pixel = input.getRGB(x, y);
                int a = (pixel >>> 24) & 0xFF;
                int r = (pixel >>> 16) & 0xFF;
                int g = (pixel >>> 8) & 0xFF;
                int b = pixel & 0xFF;
                float[] hsv = Color.RGBtoHSB(r, g, b, null);
                int gray = Math.round(hsv[2] * 255f);
                output.setRGB(x, y, (a << 24) | (gray << 16) | (gray << 8) | gray);
            }
        }
        return write(output);
    }

    public File applyHistogramRgb(File inputFile) throws IOException {
        BufferedImage input = read(inputFile);
        int[] red = new int[256];
        int[] green = new int[256];
        int[] blue = new int[256];
        for (int y = 0; y < input.getHeight(); y++) {
            for (int x = 0; x < input.getWidth(); x++) {
                int pixel = input.getRGB(x, y);
                red[(pixel >>> 16) & 0xFF]++;
                green[(pixel >>> 8) & 0xFF]++;
                blue[pixel & 0xFF]++;
            }
        }

        int width = 900;
        int height = 600;
        BufferedImage output = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = output.createGraphics();
        g.setColor(new Color(249, 247, 243));
        g.fillRect(0, 0, width, height);
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int chartX = 60;
        int chartY = 60;
        int chartW = width - 120;
        int chartH = height - 140;
        g.setColor(new Color(230, 224, 216));
        g.drawRoundRect(chartX, chartY, chartW, chartH, 18, 18);

        int max = 1;
        for (int i = 0; i < 256; i++) {
            max = Math.max(max, Math.max(red[i], Math.max(green[i], blue[i])));
        }

        for (int i = 0; i < 256; i++) {
            int barX = chartX + (i * chartW / 256);
            int barW = Math.max(1, chartW / 256);
            int rH = (int) ((red[i] / (double) max) * (chartH - 40));
            int gH = (int) ((green[i] / (double) max) * (chartH - 40));
            int bH = (int) ((blue[i] / (double) max) * (chartH - 40));
            g.setColor(new Color(214, 87, 87, 170));
            g.fillRect(barX, chartY + chartH - rH - 20, barW, rH);
            g.setColor(new Color(85, 145, 92, 170));
            g.fillRect(barX, chartY + chartH - gH - 20, barW, gH);
            g.setColor(new Color(89, 126, 183, 170));
            g.fillRect(barX, chartY + chartH - bH - 20, barW, bH);
        }

        g.setColor(new Color(58, 53, 48));
        g.setFont(new Font("SansSerif", Font.BOLD, 24));
        g.drawString("Histograma RGB", chartX, 34);
        g.dispose();
        return write(output);
    }

    public File applyEqualization(File inputFile) throws IOException {
        BufferedImage input = read(inputFile);
        int width = input.getWidth();
        int height = input.getHeight();

        int[] histogram = new int[256];
        int[] luminance = new int[width * height];
        int[] alpha = new int[width * height];
        int[] red = new int[width * height];
        int[] green = new int[width * height];
        int[] blue = new int[width * height];
        int index = 0;

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int pixel = input.getRGB(x, y);
                int a = (pixel >>> 24) & 0xFF;
                int r = (pixel >>> 16) & 0xFF;
                int g = (pixel >>> 8) & 0xFF;
                int b = pixel & 0xFF;
                int gray = clamp((int) Math.round(0.299 * r + 0.587 * g + 0.114 * b));
                histogram[gray]++;
                alpha[index] = a;
                red[index] = r;
                green[index] = g;
                blue[index] = b;
                luminance[index++] = gray;
            }
        }

        int total = width * height;
        int[] cdf = new int[256];
        cdf[0] = histogram[0];
        for (int i = 1; i < 256; i++) {
            cdf[i] = cdf[i - 1] + histogram[i];
        }

        int cdfMin = 0;
        for (int value : cdf) {
            if (value != 0) {
                cdfMin = value;
                break;
            }
        }

        BufferedImage output = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        index = 0;
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int gray = luminance[index];
                int a = alpha[index];
                int r = red[index];
                int g = green[index];
                int b = blue[index];

                int equalizedGray;
                if (total == cdfMin) {
                    equalizedGray = gray;
                } else {
                    equalizedGray = clamp((int) Math.round(((cdf[gray] - cdfMin) / (double) (total - cdfMin)) * 255.0));
                }
                float factor = gray == 0 ? (equalizedGray / 255f) : (equalizedGray / (float) gray);
                int nr = clamp(Math.round(r * factor));
                int ng = clamp(Math.round(g * factor));
                int nb = clamp(Math.round(b * factor));
                output.setRGB(x, y, (a << 24) | (nr << 16) | (ng << 8) | nb);
                index++;
            }
        }

        return write(output);
    }

    public int[] histogramOf(File inputFile) throws IOException {
        BufferedImage input = read(inputFile);
        int[] histogram = new int[256];
        for (int y = 0; y < input.getHeight(); y++) {
            for (int x = 0; x < input.getWidth(); x++) {
                int pixel = input.getRGB(x, y);
                int r = (pixel >>> 16) & 0xFF;
                int g = (pixel >>> 8) & 0xFF;
                int b = pixel & 0xFF;
                int gray = clamp((int) Math.round(0.299 * r + 0.587 * g + 0.114 * b));
                histogram[gray]++;
            }
        }
        return histogram;
    }

    public File equalize(File inputFile) throws IOException {
        return applyEqualization(inputFile);
    }

    public File applyBlending(File inputFile) throws IOException {
        BufferedImage input = read(inputFile);
        BufferedImage output = new BufferedImage(input.getWidth(), input.getHeight(), BufferedImage.TYPE_INT_ARGB);
        for (int y = 0; y < input.getHeight(); y++) {
            for (int x = 0; x < input.getWidth(); x++) {
                int pixel = input.getRGB(x, y);
                int a = (pixel >>> 24) & 0xFF;
                int r = (pixel >>> 16) & 0xFF;
                int g = (pixel >>> 8) & 0xFF;
                int b = pixel & 0xFF;
                int blendR = clamp((int) (r * 0.72 + 60));
                int blendG = clamp((int) (g * 0.90 + 20));
                int blendB = clamp((int) (b * 0.78 + 35));
                output.setRGB(x, y, (a << 24) | (blendR << 16) | (blendG << 8) | blendB);
            }
        }
        return write(output);
    }

    public File applyHsvAdjust(File inputFile, int brightness, int saturation, int alpha) throws IOException {
        BufferedImage input = read(inputFile);
        BufferedImage output = new HSVAdjustFilter(brightness, saturation, alpha).apply(input);
        return write(output);
    }

    public File applyBrightnessByChannel(File inputFile, int amount) throws IOException {
        BufferedImage input = read(inputFile);
        BufferedImage output = new BufferedImage(input.getWidth(), input.getHeight(), BufferedImage.TYPE_INT_ARGB);
        for (int y = 0; y < input.getHeight(); y++) {
            for (int x = 0; x < input.getWidth(); x++) {
                int pixel = input.getRGB(x, y);
                int a = (pixel >>> 24) & 0xFF;
                int r = clamp(((pixel >>> 16) & 0xFF) + amount);
                int g = clamp(((pixel >>> 8) & 0xFF) + amount);
                int b = clamp((pixel & 0xFF) + amount);
                output.setRGB(x, y, (a << 24) | (r << 16) | (g << 8) | b);
            }
        }
        return write(output);
    }

    public File applyAlphaChannel(File inputFile, int alpha) throws IOException {
        BufferedImage input = read(inputFile);
        BufferedImage output = new BufferedImage(input.getWidth(), input.getHeight(), BufferedImage.TYPE_INT_ARGB);
        for (int y = 0; y < input.getHeight(); y++) {
            for (int x = 0; x < input.getWidth(); x++) {
                int pixel = input.getRGB(x, y);
                int r = (pixel >>> 16) & 0xFF;
                int g = (pixel >>> 8) & 0xFF;
                int b = pixel & 0xFF;
                output.setRGB(x, y, (clamp(alpha) << 24) | (r << 16) | (g << 8) | b);
            }
        }
        return write(output);
    }

    public File applyCircularFade(File inputFile) throws IOException {
        File outputFile = createTempImage();
        new Desvanecimiento_circular().aplicar(inputFile, outputFile);
        return outputFile;
    }

    public File applyFrostedGlass(File inputFile) throws IOException {
        File outputFile = createTempImage();
        new Vidrio_esmerilado().crearVidrio(inputFile, outputFile);
        return outputFile;
    }

    public File applyBitMaskCrop(File inputFile) throws IOException {
        BufferedImage input = read(inputFile);
        BufferedImage output = new BufferedImage(input.getWidth(), input.getHeight(), BufferedImage.TYPE_INT_ARGB);
        for (int y = 0; y < input.getHeight(); y++) {
            for (int x = 0; x < input.getWidth(); x++) {
                int pixel = input.getRGB(x, y);
                int a = (pixel >>> 24) & 0xFF;
                int r = ((pixel >>> 16) & 0xFF) & 0xF0;
                int g = ((pixel >>> 8) & 0xFF) & 0xF0;
                int b = (pixel & 0xFF) & 0xF0;
                output.setRGB(x, y, (a << 24) | (r << 16) | (g << 8) | b);
            }
        }
        return write(output);
    }

    public File applyRandomGradient(File inputFile, String type) throws IOException {
        BufferedImage input = read(inputFile);
        BufferedImage output = new BufferedImage(input.getWidth(), input.getHeight(), BufferedImage.TYPE_INT_ARGB);
        Color start = new Color(247, 242, 235);
        Color end = new Color(124, 158, 189);
        for (int y = 0; y < input.getHeight(); y++) {
            for (int x = 0; x < input.getWidth(); x++) {
                double t;
                if ("HORIZONTAL".equals(type)) {
                    t = x / (double) Math.max(1, input.getWidth() - 1);
                } else if ("VERTICAL".equals(type)) {
                    t = y / (double) Math.max(1, input.getHeight() - 1);
                } else {
                    double cx = input.getWidth() / 2.0;
                    double cy = input.getHeight() / 2.0;
                    double dx = x - cx;
                    double dy = y - cy;
                    t = Math.min(1.0, Math.sqrt(dx * dx + dy * dy) / Math.sqrt(cx * cx + cy * cy));
                }
                if ("RADIAL_SOFT".equals(type)) {
                    t = Math.sqrt(t);
                }
                int r = (int) (start.getRed() + t * (end.getRed() - start.getRed()));
                int g = (int) (start.getGreen() + t * (end.getGreen() - start.getGreen()));
                int b = (int) (start.getBlue() + t * (end.getBlue() - start.getBlue()));
                output.setRGB(x, y, (255 << 24) | (clamp(r) << 16) | (clamp(g) << 8) | clamp(b));
            }
        }
        return write(output);
    }

    public File applyConvolutionManual(File inputFile, float[] kernel, String name) throws IOException {
        BufferedImage input = read(inputFile);
        BufferedImage output = new Convolucion(kernel, name).apply(input);
        return write(output);
    }

    public File applyConvolutionOp(File inputFile, String kernelType) throws IOException {
        return applyConvolutionManual(inputFile, kernelFor(kernelType), kernelType);
    }

    public File applyAmanecerX10(File inputFile) throws IOException {
        BufferedImage input = read(inputFile);
        BufferedImage output = new BufferedImage(input.getWidth(), input.getHeight(), BufferedImage.TYPE_INT_ARGB);
        for (int y = 0; y < input.getHeight(); y++) {
            for (int x = 0; x < input.getWidth(); x++) {
                int pixel = input.getRGB(x, y);
                int a = (pixel >>> 24) & 0xFF;
                int r = clamp((int) (((pixel >>> 16) & 0xFF) * 1.08 + 18));
                int g = clamp((int) (((pixel >>> 8) & 0xFF) * 1.05 + 10));
                int b = clamp((int) ((pixel & 0xFF) * 0.92));
                output.setRGB(x, y, (a << 24) | (r << 16) | (g << 8) | b);
            }
        }
        return write(output);
    }

    public File applyGradientOverlay(File inputFile) throws IOException {
        BufferedImage input = read(inputFile);
        BufferedImage output = new BufferedImage(input.getWidth(), input.getHeight(), BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = output.createGraphics();
        g.drawImage(input, 0, 0, null);
        g.setComposite(AlphaComposite.SrcOver.derive(0.35f));
        g.setPaint(new GradientPaint(0, 0, new Color(255, 204, 153), input.getWidth(), input.getHeight(), new Color(124, 158, 189)));
        g.fillRect(0, 0, input.getWidth(), input.getHeight());
        g.dispose();
        return write(output);
    }

    public File applyStencilCircle(File inputFile) throws IOException {
        BufferedImage input = read(inputFile);
        BufferedImage output = new BufferedImage(input.getWidth(), input.getHeight(), BufferedImage.TYPE_INT_ARGB);
        int centerX = input.getWidth() / 2;
        int centerY = input.getHeight() / 2;
        int radius = Math.min(input.getWidth(), input.getHeight()) / 3;
        int radius2 = radius * radius;
        for (int y = 0; y < input.getHeight(); y++) {
            for (int x = 0; x < input.getWidth(); x++) {
                int dx = x - centerX;
                int dy = y - centerY;
                if (dx * dx + dy * dy <= radius2) {
                    output.setRGB(x, y, input.getRGB(x, y));
                } else {
                    output.setRGB(x, y, 0xFF101826);
                }
            }
        }
        return write(output);
    }

    public File applyAlphaTest(File inputFile) throws IOException {
        BufferedImage input = read(inputFile);
        BufferedImage output = new BufferedImage(input.getWidth(), input.getHeight(), BufferedImage.TYPE_INT_ARGB);
        for (int y = 0; y < input.getHeight(); y++) {
            for (int x = 0; x < input.getWidth(); x++) {
                int pixel = input.getRGB(x, y);
                int r = (pixel >>> 16) & 0xFF;
                int g = (pixel >>> 8) & 0xFF;
                int b = pixel & 0xFF;
                int avg = (r + g + b) / 3;
                if (avg > 128) {
                    output.setRGB(x, y, pixel | 0xFF000000);
                } else {
                    output.setRGB(x, y, 0x00000000);
                }
            }
        }
        return write(output);
    }

    public File applyLogicOpXor(File inputFile) throws IOException {
        BufferedImage input = read(inputFile);
        BufferedImage output = new BufferedImage(input.getWidth(), input.getHeight(), BufferedImage.TYPE_INT_ARGB);
        for (int y = 0; y < input.getHeight(); y++) {
            for (int x = 0; x < input.getWidth(); x++) {
                int pixel = input.getRGB(x, y);
                int xor = pixel ^ 0x00FFFFFF;
                output.setRGB(x, y, (pixel & 0xFF000000) | (xor & 0x00FFFFFF));
            }
        }
        return write(output);
    }

    public File applyDepthDemo(File inputFile) throws IOException {
        BufferedImage input = read(inputFile);
        BufferedImage output = new BufferedImage(input.getWidth(), input.getHeight(), BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = output.createGraphics();
        g.drawImage(input, 0, 0, null);
        g.setComposite(AlphaComposite.SrcOver.derive(0.52f));
        g.setColor(new Color(52, 80, 122));
        g.fillRoundRect(input.getWidth() / 8, input.getHeight() / 6, input.getWidth() * 3 / 4, input.getHeight() * 2 / 3, 40, 40);
        g.setComposite(AlphaComposite.SrcOver.derive(0.76f));
        g.setColor(new Color(226, 169, 86));
        g.fillOval(input.getWidth() / 3, input.getHeight() / 3, input.getWidth() / 3, input.getHeight() / 3);
        g.dispose();
        return write(output);
    }

    public File applyBlendingDemo(File inputFile) throws IOException {
        BufferedImage input = read(inputFile);
        BufferedImage overlay = new BufferedImage(input.getWidth(), input.getHeight(), BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = overlay.createGraphics();
        g.setPaint(new GradientPaint(0, 0, new Color(238, 161, 95, 220), input.getWidth(), input.getHeight(), new Color(97, 136, 188, 220)));
        g.fillRect(0, 0, input.getWidth(), input.getHeight());
        g.setComposite(AlphaComposite.SrcOver.derive(0.30f));
        g.setColor(new Color(255, 255, 255, 180));
        g.fillOval(input.getWidth() / 4, input.getHeight() / 5, input.getWidth() / 2, input.getHeight() / 2);
        g.dispose();
        return blendImages(input, overlay, 0.45f);
    }

    private File blendImages(BufferedImage background, BufferedImage foreground, float alpha) throws IOException {
        int width = Math.min(background.getWidth(), foreground.getWidth());
        int height = Math.min(background.getHeight(), foreground.getHeight());
        BufferedImage output = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int p1 = background.getRGB(x, y);
                int p2 = foreground.getRGB(x, y);
                int a1 = (p1 >>> 24) & 0xFF;
                int r1 = (p1 >>> 16) & 0xFF;
                int g1 = (p1 >>> 8) & 0xFF;
                int b1 = p1 & 0xFF;
                int a2 = (p2 >>> 24) & 0xFF;
                int r2 = (p2 >>> 16) & 0xFF;
                int g2 = (p2 >>> 8) & 0xFF;
                int b2 = p2 & 0xFF;
                int a = clamp((int) (a2 * alpha + a1 * (1 - alpha)));
                int r = clamp((int) (r2 * alpha + r1 * (1 - alpha)));
                int g = clamp((int) (g2 * alpha + g1 * (1 - alpha)));
                int b = clamp((int) (b2 * alpha + b1 * (1 - alpha)));
                output.setRGB(x, y, (a << 24) | (r << 16) | (g << 8) | b);
            }
        }
        return write(output);
    }

    public File applyHSVGray(File inputFile) throws IOException {
        return applyGrayscaleHsv(inputFile);
    }

    public File applySliderHsv(File inputFile, int brightness, int saturation, int alpha) throws IOException {
        return applyHsvAdjust(inputFile, brightness, saturation, alpha);
    }

    private File applyChannelQuantization(File inputFile, int levels, boolean removeGreen) throws IOException {
        File outputFile = createTempImage();
        new Dos_Canales().aplicar(inputFile, outputFile, levels, removeGreen);
        return outputFile;
    }

    private float[] kernelFor(String kernelType) {
        return switch (kernelType) {
            case "NORMAL" -> kerlnes.kNormal;
            case "ENFOQUE" -> kerlnes.kEnfoque;
            case "DESENFOQUE_3X3" -> kerlnes.kDesenfoque;
            case "DESENFOQUE_9X9" -> kerlnes.kDesenfoque9x9;
            case "BORDES_4V" -> kerlnes.kBordes;
            case "BORDES_8V" -> kerlnes.kBordes8;
            case "ACLARACION" -> kerlnes.kAclarado;
            case "OSCURECER" -> kerlnes.kOscurecido;
            default -> kerlnes.kNormal;
        };
    }

    private BufferedImage read(File file) throws IOException {
        return ImageIO.read(file);
    }

    private File write(BufferedImage image) throws IOException {
        File outputFile = createTempImage();
        ImageIO.write(image, "png", outputFile);
        return outputFile;
    }

    private File createTempImage() throws IOException {
        File outputFile = File.createTempFile("taller2_filter_", ".png");
        outputFile.deleteOnExit();
        return outputFile;
    }

    private int clamp(int value) {
        return Math.max(0, Math.min(255, value));
    }
}
