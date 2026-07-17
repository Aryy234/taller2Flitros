package ec.edu.uce.taller2flitros.model;

public class kerlnes {

    // no modifica imagen
    public static final float[] kNormal = {
            0f, 0f, 0f,
            0f, 1f, 0f,
            0f, 0f, 0f
    };

    // kernel de enfoque (sharpening)
    public static final float[] kEnfoque = {
            0f, -1f, 0f,
            -1f, 5f, -1f,
            0f, -1f, 0f
    };

    // kernel de desenfoque (blur)
    public static final float[] kDesenfoque = {
            1f / 9f, 1f / 9f, 1f / 9f,
            1f / 9f, 1f / 9f, 1f / 9f,
            1f / 9f, 1f / 9f, 1f / 9f
    };

    // kernel de desenfoque (blur)
    public static final float[] kDesenfoque9x9 = {
            1f / 81f, 1f / 81f, 1f / 81f, 1f / 81f, 1f / 81f, 1f / 81f, 1f / 81f, 1f / 81f, 1f / 81f,
            1f / 81f, 1f / 81f, 1f / 81f, 1f / 81f, 1f / 81f, 1f / 81f, 1f / 81f, 1f / 81f, 1f / 81f,
            1f / 81f, 1f / 81f, 1f / 81f, 1f / 81f, 1f / 81f, 1f / 81f, 1f / 81f, 1f / 81f, 1f / 81f,
            1f / 81f, 1f / 81f, 1f / 81f, 1f / 81f, 1f / 81f, 1f / 81f, 1f / 81f, 1f / 81f, 1f / 81f,
            1f / 81f, 1f / 81f, 1f / 81f, 1f / 81f, 1f / 81f, 1f / 81f, 1f / 81f, 1f / 81f, 1f / 81f,
            1f / 81f, 1f / 81f, 1f / 81f, 1f / 81f, 1f / 81f, 1f / 81f, 1f / 81f, 1f / 81f, 1f / 81f,
            1f / 81f, 1f / 81f, 1f / 81f, 1f / 81f, 1f / 81f, 1f / 81f, 1f / 81f, 1f / 81f, 1f / 81f,
            1f / 81f, 1f / 81f, 1f / 81f, 1f / 81f, 1f / 81f, 1f / 81f, 1f / 81f, 1f / 81f, 1f / 81f,
            1f / 81f, 1f / 81f, 1f / 81f, 1f / 81f, 1f / 81f, 1f / 81f, 1f / 81f, 1f / 81f, 1f / 81f
    };

    // kernel de detección de bordes (edge detection)
    public static final float[] kBordes = {
            -0.5f, -0.5f, -0.5f,
            -0.5f, -4f, -0.5f,
            -0.5f, -0.5f, -0.5f,
    };

    // kernel de detección de bordes (edge detection)
    public static final float[] kBordes8 = {
            -1f, -1f, -1f,
            -1f, 8f, -1f,
            -1f, -1f, -1f,
    };

    // kernel de aclarado (brightening)
    public static final float[] kAclarado = {
            0.1f, 0.1f, 0.1f,
            0.1f, 1f, 0.1f,
            0.1f, 0.1f, 0.1f,
    };

    // kernel de oscurecido (darkening)
    public static final float[] kOscurecido = {
            0.01f, 0.01f, 0.01f,
            0.01f, 0.5f, 0.01f,
            0.01f, 0.01f, 0.01f,
    };

}
