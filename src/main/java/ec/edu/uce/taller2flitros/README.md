# Pixel Forge - Editor de Filtros de Imagen

Una interfaz JavaFX moderna y profesional para aplicar filtros a imágenes, con vista previa automática y controles deslizantes para parámetros ajustables.

## 🎨 Características

- **Interfaz Dark Mode Profesional**: Diseño moderno con paleta de colores azul oscuro
- **Vista Previa Automática**: Los cambios se reflejan instantáneamente
- **Controles Deslizantes**: Ajusta brillo, saturación y alpha en tiempo real
- **16 Filtros Disponibles**: Desde básicos hasta artísticos y de convolución
- **Diseño Responsivo**: Interfaz adaptable con scroll en sección de filtros
- **Indicadores Visuales**: Barra de progreso y estados de procesamiento

## 📋 Filtros Disponibles

### Básicos
- Escala de Grises
- Negativo
- Blanco y Negro

### Ajustables (con sliders)
- Brillo (-100 a 100)
- Saturación (0 a 200)
- Canal Alpha (0 a 255)

### Artísticos
- Vidrio Esmerilado
- Desvanecimiento Circular
- Retro 8-bits
- Retro 8-bits 2 Canales
- Grises con Reducción de Color

### Convolución
- Aclarado
- Oscurecido
- Detección de Bordes
- Blur
- Enfoque/Sharpening

## 🚀 Instalación y Configuración

### Requisitos
- Java JDK 11 o superior
- JavaFX SDK 17 o superior
- Tu proyecto Java existente con clases de filtros

### Estructura de Archivos

```
tu-proyecto/
├── src/
│   ├── ImageFilterApp.java         # Clase principal
│   ├── ImageFilterController.java  # Controlador
│   ├── ImageFilterApp.fxml         # Interfaz FXML
│   ├── styles.css                  # Estilos CSS
│   └── [Tus clases de filtros]     # Clases existentes
```

### Paso 1: Copiar Archivos

Copia los siguientes archivos a tu proyecto:
- `ImageFilterApp.java`
- `ImageFilterController.java`
- `ImageFilterApp.fxml`
- `styles.css`

### Paso 2: Integrar tus Clases de Filtros

En `ImageFilterController.java`, localiza el método `applyFilter()` (línea ~300) y reemplaza los comentarios con llamadas a tus métodos reales:

```java
private File applyFilter(File inputFile, String filterType) throws Exception {
    File outputFile = File.createTempFile("filtered_", ".png");
    
    switch (filterType) {
        case "GRAYSCALE":
            // REEMPLAZA ESTO con tu método real
            TuClaseDeFiltros.aplicarEscalaGrises(inputFile, outputFile);
            break;
            
        case "BRIGHTNESS":
            int brightnessValue = (int) brightnessSlider.getValue();
            TuClaseDeFiltros.aplicarBrillo(inputFile, outputFile, brightnessValue);
            break;
            
        // ... continúa con los demás filtros
    }
    
    return outputFile;
}
```

### Paso 3: Configurar Dependencias

Si usas Maven, agrega JavaFX a tu `pom.xml`:

```xml
<dependencies>
    <dependency>
        <groupId>org.openjfx</groupId>
        <artifactId>javafx-controls</artifactId>
        <version>17.0.2</version>
    </dependency>
    <dependency>
        <groupId>org.openjfx</groupId>
        <artifactId>javafx-fxml</artifactId>
        <version>17.0.2</version>
    </dependency>
</dependencies>
```

Si usas Gradle:

```gradle
dependencies {
    implementation 'org.openjfx:javafx-controls:17.0.2'
    implementation 'org.openjfx:javafx-fxml:17.0.2'
}
```

### Paso 4: Configurar VM Options

Al ejecutar la aplicación, asegúrate de agregar las opciones de VM:

```
--module-path /ruta/a/javafx-sdk/lib --add-modules javafx.controls,javafx.fxml
```

## 🎯 Uso de la Aplicación

### Flujo de Trabajo

1. **Seleccionar Imagen**: Haz clic en "Seleccionar Imagen" y elige una imagen (PNG, JPG, BMP, GIF)

2. **Elegir Filtro**: Selecciona el filtro deseado de la lista en el panel izquierdo

3. **Ajustar Parámetros** (opcional): Si el filtro tiene parámetros (brillo, saturación, alpha), usa los sliders

4. **Vista Previa Automática**: El filtro se aplica automáticamente al cambiar parámetros

5. **Guardar Imagen**: Haz clic en "Guardar Imagen" y elige la ubicación

6. **Restablecer**: Usa el botón "Restablecer" para limpiar la interfaz

### Atajos de Teclado

- **Ctrl + O**: Seleccionar imagen (puedes agregarlo)
- **Ctrl + S**: Guardar imagen (puedes agregarlo)
- **Ctrl + R**: Restablecer (puedes agregarlo)

## 🎨 Personalización de Colores

Los colores están definidos en `styles.css`. Para cambiarlos, modifica las variables CSS:

```css
.root {
    -fx-background: #0B1220;        /* Fondo base */
    -fx-card: #121A2E;              /* Tarjetas */
    -fx-primary: #3B82F6;           /* Color principal */
    /* ... otros colores ... */
}
```

## 🔧 Integración con tus Clases

### Ejemplo de Integración

Si tienes una clase llamada `FiltrosImagen` con métodos como:

```java
public class FiltrosImagen {
    public static void aplicarEscalaGrises(File entrada, File salida) { ... }
    public static void aplicarBrillo(File entrada, File salida, int valor) { ... }
}
```

Entonces en el controlador harías:

```java
case "GRAYSCALE":
    FiltrosImagen.aplicarEscalaGrises(inputFile, outputFile);
    break;

case "BRIGHTNESS":
    int brightnessValue = (int) brightnessSlider.getValue();
    FiltrosImagen.aplicarBrillo(inputFile, outputFile, brightnessValue);
    break;
```

### Manejo de BufferedImage

Si tus métodos usan `BufferedImage` en lugar de archivos:

```java
private File applyFilter(File inputFile, String filterType) throws Exception {
    // Leer imagen
    BufferedImage inputImage = ImageIO.read(inputFile);
    BufferedImage outputImage = null;
    
    switch (filterType) {
        case "GRAYSCALE":
            outputImage = TuClase.aplicarEscalaGrises(inputImage);
            break;
        // ...
    }
    
    // Guardar resultado
    File outputFile = File.createTempFile("filtered_", ".png");
    ImageIO.write(outputImage, "png", outputFile);
    
    return outputFile;
}
```

## 📊 Estructura del Código

### ImageFilterController.java

- **Variables FXML**: Referencias a componentes de la UI
- **initialize()**: Configuración inicial de la interfaz
- **Manejadores de Eventos**: Métodos que responden a acciones del usuario
- **applyFilter()**: ⚠️ MÉTODO PRINCIPAL A MODIFICAR - Aquí integras tus filtros
- **Métodos Auxiliares**: Utilidades para manejo de UI

### Métodos Principales a Personalizar

```java
// 1. Aplicar filtro (OBLIGATORIO modificar)
private File applyFilter(File inputFile, String filterType)

// 2. Opcional: Agregar validaciones
private boolean validateImageFormat(File file)

// 3. Opcional: Procesar en background con feedback
private void applyFilterPreview()
```

## 🐛 Solución de Problemas

### Error: "Location not set"
**Solución**: Verifica que el archivo FXML esté en el mismo directorio que la clase Main

### Error: "javafx.fxml.LoadException"
**Solución**: Asegúrate que el atributo `fx:controller` en el FXML coincida con el nombre completo de la clase

### Los filtros no se aplican
**Solución**: Revisa que hayas reemplazado las llamadas `simulateProcessing()` con tus métodos reales

### Las imágenes no se muestran
**Solución**: Verifica que las rutas de archivo sean accesibles y los formatos soportados

## 💡 Consejos de Optimización

1. **Procesamiento Asíncrono**: Los filtros se ejecutan en un Thread separado para no bloquear la UI

2. **Cache de Imágenes**: Considera implementar un sistema de cache para filtros frecuentes

3. **Validación de Entrada**: Agrega validaciones para formatos de imagen soportados

4. **Historial de Cambios**: Implementa un sistema de undo/redo si es necesario

## 📝 Notas Importantes

- ⚠️ **IMPORTANTE**: El método `applyFilter()` contiene código placeholder que debes reemplazar
- La vista previa automática se actualiza en tiempo real con los sliders
- Los archivos temporales se crean en el directorio temporal del sistema
- La aplicación soporta PNG, JPG, JPEG, BMP y GIF

## 🎓 Recursos Adicionales

- [Documentación JavaFX](https://openjfx.io/)
- [Tutorial FXML](https://docs.oracle.com/javafx/2/fxml_get_started/jfxpub-fxml_get_started.htm)
- [Guía de Estilos CSS JavaFX](https://docs.oracle.com/javafx/2/api/javafx/scene/doc-files/cssref.html)

## 📄 Licencia

Este código es de uso libre para proyectos educativos y personales.

---

**¡Feliz procesamiento de imágenes! 🎨✨**
