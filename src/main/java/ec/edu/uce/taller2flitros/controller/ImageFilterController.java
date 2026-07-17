package ec.edu.uce.taller2flitros.controller;

import ec.edu.uce.taller2flitros.service.ImageFilterService;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

public class ImageFilterController {

    @FXML private Label statusLabel;
    @FXML private Label fileNameLabel;
    @FXML private Label progressLabel;
    @FXML private Label processingIndicator;
    @FXML private Label previewLabel;
    @FXML private Label activeFilterLabel;

    @FXML private Button applyFilterBtn;
    @FXML private Button saveImageBtn;
    @FXML private Button resetBtn;
    @FXML private Button randomImageBtn;
    @FXML private Button copyImageBtn;

    @FXML private ImageView originalImageView;
    @FXML private ImageView previewImageView;
    @FXML private VBox originalPlaceholder;
    @FXML private VBox previewPlaceholder;
    @FXML private StackPane originalImageStack;
    @FXML private StackPane previewImageStack;

    @FXML private MenuButton menuGenerar;
    @FXML private MenuButton menuColor;
    @FXML private MenuButton menuHsv;
    @FXML private MenuButton menuDegradados;
    @FXML private MenuButton menuEspeciales;
    @FXML private MenuButton menuMascara;
    @FXML private MenuButton menuConvolucion;
    @FXML private MenuButton menuExposiciones;
    @FXML private MenuButton menuDemos;

    @FXML private MenuItem menuItemRandomImage;
    @FXML private MenuItem menuItemCopyImage;

    @FXML private MenuItem menuItemManualConvolution;
    @FXML private MenuItem menuItemOpConvolution;
    @FXML private MenuItem menuItemAmanecer;

    @FXML private MenuItem menuItemBlackWhite;
    @FXML private MenuItem menuItemGrayscale;
    @FXML private MenuItem menuItemGrayscaleHsv;
    @FXML private MenuItem menuItemRetro1;
    @FXML private MenuItem menuItemRetro2;
    @FXML private MenuItem menuItemNegative;
    @FXML private MenuItem menuItemHistogram;
    @FXML private MenuItem menuItemEqualization;
    @FXML private MenuItem menuItemBlend;

    @FXML private MenuItem menuItemHsvFilters;
    @FXML private MenuItem menuItemHsvSaturation;
    @FXML private MenuItem menuItemBrightnessByChannel;
    @FXML private MenuItem menuItemAlphaChannel;

    @FXML private MenuItem menuItemGradientHorizontal;
    @FXML private MenuItem menuItemGradientVertical;
    @FXML private MenuItem menuItemGradientRadial;
    @FXML private MenuItem menuItemGradientSoftRadial;

    @FXML private MenuItem menuItemCircularFade;
    @FXML private MenuItem menuItemFrostedGlass;

    @FXML private MenuItem menuItemBitMask;

    @FXML private MenuItem menuItemStencilCircle;
    @FXML private MenuItem menuItemAlphaTest;
    @FXML private MenuItem menuItemLogicOp;
    @FXML private MenuItem menuItemDepthDemo;
    @FXML private MenuItem menuItemBlendDemo;

    @FXML private MenuItem menuKernelNormal;
    @FXML private MenuItem menuKernelSharpen;
    @FXML private MenuItem menuKernelBlur3;
    @FXML private MenuItem menuKernelBlur9;
    @FXML private MenuItem menuKernelEdges4;
    @FXML private MenuItem menuKernelEdges8;
    @FXML private MenuItem menuKernelLighten;
    @FXML private MenuItem menuKernelDarken;

    @FXML private Slider brightnessSlider;
    @FXML private Label brightnessValueLabel;
    @FXML private Slider saturationSlider;
    @FXML private Label saturationValueLabel;
    @FXML private Slider alphaSlider;
    @FXML private Label alphaValueLabel;

    @FXML private ProgressBar progressBar;

    private final ImageFilterService service = new ImageFilterService();
    private final Map<String, String> filterNames = new LinkedHashMap<>();

    private File selectedImageFile;
    private File processedImageFile;
    private String currentFilterType = "";
    private String currentFilterLabel = "";
    private boolean autoPreviewEnabled = true;

    @FXML
    public void initialize() {
        registerFilterNames();
        wireMenuItems();
        setupSliders();
        updateUIState();
        originalImageView.fitWidthProperty().bind(originalImageStack.widthProperty().subtract(40));
        originalImageView.fitHeightProperty().bind(originalImageStack.heightProperty().subtract(40));
        previewImageView.fitWidthProperty().bind(previewImageStack.widthProperty().subtract(40));
        previewImageView.fitHeightProperty().bind(previewImageStack.heightProperty().subtract(40));
    }

    private void registerFilterNames() {
        filterNames.put("GRAYSCALE", "Escala de Grises");
        filterNames.put("BLACKWHITE", "Blanco y Negro");
        filterNames.put("GRAYSCALE_HSV", "Escala de Grises HSV");
        filterNames.put("RETRO1", "Efecto Retro 1");
        filterNames.put("RETRO2", "Efecto Retro 2");
        filterNames.put("NEGATIVE", "Filtro Negativo");
        filterNames.put("BLEND", "Blending / Mezcla");
        filterNames.put("EQUALIZATION", "Ecualización");
        filterNames.put("HSV_FILTERS", "Filtros HSV");
        filterNames.put("HSV_SATURATION", "Saturación HSV");
        filterNames.put("BRIGHTNESS_CHANNEL", "Brillo por Canal");
        filterNames.put("ALPHA_CHANNEL", "Canal Alpha");
        filterNames.put("CIRCULAR_FADE", "Desvanecimiento Circular");
        filterNames.put("FROSTED_GLASS", "Vidrio Esmerilado");
        filterNames.put("BIT_MASK", "Recorte de Bits");
        filterNames.put("HISTOGRAM", "Histograma RGB");
        filterNames.put("STENCIL_CIRCLE", "Stencil circular");
        filterNames.put("ALPHA_TEST", "Alpha Test");
        filterNames.put("LOGIC_OP", "Logic Op");
        filterNames.put("DEPTH_DEMO", "Depth Test");
        filterNames.put("BLEND_DEMO", "Blending");
        filterNames.put("CONV_MANUAL", "Convolución Manual");
        filterNames.put("CONV_OP", "Convolución Op");
        filterNames.put("AMANECER_X10", "Convolución Amanecer ×10");
        filterNames.put("GRAD_H", "Degradado Horizontal");
        filterNames.put("GRAD_V", "Degradado Vertical");
        filterNames.put("GRAD_R", "Degradado Radial");
        filterNames.put("GRAD_R2", "Gradiente Radial");
    }

    private void wireMenuItems() {
        menuItemRandomImage.setOnAction(e -> handleRandomImage());
        menuItemCopyImage.setOnAction(e -> handleCopyImage());

        menuItemManualConvolution.setOnAction(e -> selectFilter("CONV_MANUAL"));
        menuItemOpConvolution.setOnAction(e -> selectFilter("CONV_OP"));
        menuItemAmanecer.setOnAction(e -> selectFilter("AMANECER_X10"));

        menuItemBlackWhite.setOnAction(e -> selectFilter("BLACKWHITE"));
        menuItemGrayscale.setOnAction(e -> selectFilter("GRAYSCALE"));
        menuItemGrayscaleHsv.setOnAction(e -> selectFilter("GRAYSCALE_HSV"));
        menuItemRetro1.setOnAction(e -> selectFilter("RETRO1"));
        menuItemRetro2.setOnAction(e -> selectFilter("RETRO2"));
        menuItemNegative.setOnAction(e -> selectFilter("NEGATIVE"));
        menuItemBlend.setOnAction(e -> selectFilter("BLEND"));
        menuItemHistogram.setOnAction(e -> selectFilter("HISTOGRAM"));
        menuItemEqualization.setOnAction(e -> handleOpenEqualizationDialog());

        menuItemHsvFilters.setOnAction(e -> selectFilter("HSV_FILTERS"));
        menuItemHsvSaturation.setOnAction(e -> selectFilter("HSV_SATURATION"));
        menuItemBrightnessByChannel.setOnAction(e -> selectFilter("BRIGHTNESS_CHANNEL"));
        menuItemAlphaChannel.setOnAction(e -> selectFilter("ALPHA_CHANNEL"));

        menuItemGradientHorizontal.setOnAction(e -> selectFilter("GRAD_H"));
        menuItemGradientVertical.setOnAction(e -> selectFilter("GRAD_V"));
        menuItemGradientRadial.setOnAction(e -> selectFilter("GRAD_R"));
        menuItemGradientSoftRadial.setOnAction(e -> selectFilter("GRAD_R2"));

        menuItemCircularFade.setOnAction(e -> selectFilter("CIRCULAR_FADE"));
        menuItemFrostedGlass.setOnAction(e -> selectFilter("FROSTED_GLASS"));

        menuItemBitMask.setOnAction(e -> selectFilter("BIT_MASK"));
        menuItemStencilCircle.setOnAction(e -> selectFilter("STENCIL_CIRCLE"));
        menuItemAlphaTest.setOnAction(e -> selectFilter("ALPHA_TEST"));
        menuItemLogicOp.setOnAction(e -> selectFilter("LOGIC_OP"));
        menuItemDepthDemo.setOnAction(e -> selectFilter("DEPTH_DEMO"));
        menuItemBlendDemo.setOnAction(e -> selectFilter("BLEND_DEMO"));

        menuKernelNormal.setOnAction(e -> selectFilter("KERNEL_NORMAL"));
        menuKernelSharpen.setOnAction(e -> selectFilter("KERNEL_SHARPEN"));
        menuKernelBlur3.setOnAction(e -> selectFilter("KERNEL_BLUR3"));
        menuKernelBlur9.setOnAction(e -> selectFilter("KERNEL_BLUR9"));
        menuKernelEdges4.setOnAction(e -> selectFilter("KERNEL_EDGES4"));
        menuKernelEdges8.setOnAction(e -> selectFilter("KERNEL_EDGES8"));
        menuKernelLighten.setOnAction(e -> selectFilter("KERNEL_LIGHTEN"));
        menuKernelDarken.setOnAction(e -> selectFilter("KERNEL_DARKEN"));
    }

    private void setupSliders() {
        brightnessSlider.valueProperty().addListener((obs, o, n) -> brightnessValueLabel.setText(String.valueOf(n.intValue())));
        saturationSlider.valueProperty().addListener((obs, o, n) -> saturationValueLabel.setText(String.valueOf(n.intValue())));
        alphaSlider.valueProperty().addListener((obs, o, n) -> alphaValueLabel.setText(String.valueOf(n.intValue())));
        brightnessSlider.setOnMouseReleased(e -> selectFilter("HSV_FILTERS"));
        saturationSlider.setOnMouseReleased(e -> selectFilter("HSV_FILTERS"));
        alphaSlider.setOnMouseReleased(e -> selectFilter("HSV_FILTERS"));
    }

    @FXML
    private void handleSelectImage() {
        FileChooser chooser = new FileChooser();
        chooser.setTitle("Seleccionar imagen");
        chooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Imágenes", "*.png", "*.jpg", "*.jpeg", "*.bmp", "*.gif"),
                new FileChooser.ExtensionFilter("Todos los archivos", "*.*")
        );

        File file = chooser.showOpenDialog(getStage());
        if (file != null) {
            selectedImageFile = file;
            loadImage(file);
            fileNameLabel.setText(file.getName());
            statusLabel.setText("Imagen cargada: " + file.getName());
            updateUIState();
        }
    }

    @FXML
    private void handleOpenGradientDialog() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ec/edu/uce/taller2flitros/GradientDialog.fxml"));
            Parent root = loader.load();
            Stage dialogStage = new Stage();
            dialogStage.setTitle("Generar degradado");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(getStage());
            Scene scene = new Scene(root);
            scene.getStylesheets().add(getClass().getResource("/ec/edu/uce/taller2flitros/styles.css").toExternalForm());
            dialogStage.setScene(scene);
            dialogStage.setResizable(false);
            dialogStage.showAndWait();

            GradientDialogController controller = loader.getController();
            if (controller != null && controller.isConfirmed()) {
                File outputFile = File.createTempFile("gradient_", ".png");
                outputFile.deleteOnExit();
                java.awt.Color c1 = controller.getSelectedColor1();
                java.awt.Color c2 = controller.getSelectedColor2();
                String type = controller.getSelectedType();
                selectedImageFile = createGradient(type, c1, c2, outputFile);
                loadImage(selectedImageFile);
                fileNameLabel.setText("Degradado generado");
                statusLabel.setText("Degradado creado correctamente");
                updateUIState();
            }
        } catch (Exception e) {
            showError("Error al abrir el generador: " + e.getMessage());
        }
    }

    @FXML
    private void handleOpenClassDemos() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ec/edu/uce/taller2flitros/ClassDemos.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root, 1000, 720);
            scene.getStylesheets().add(getClass().getResource("/ec/edu/uce/taller2flitros/styles.css").toExternalForm());

            Stage stage = new Stage();
            stage.setTitle("Demos de Clase");
            stage.initOwner(getStage());
            stage.initModality(Modality.NONE);
            stage.setScene(scene);
            stage.setWidth(1280);
            stage.setHeight(900);
            stage.setMinWidth(1100);
            stage.setMinHeight(760);
            stage.show();
        } catch (Exception e) {
            showError("No se pudo abrir la sección de demos: " + e.getMessage());
        }
    }

    @FXML
    private void handleOpenEqualizationDialog() {
        if (selectedImageFile == null) {
            showError("Primero selecciona una imagen para ecualizar.");
            return;
        }

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ec/edu/uce/taller2flitros/EqualizationDialog.fxml"));
            Parent root = loader.load();

            EqualizationDialogController controller = loader.getController();
            controller.setService(service);
            controller.setSourceImage(selectedImageFile);
            controller.prepare();

            Stage stage = new Stage();
            stage.setTitle("Ecualización");
            stage.initOwner(getStage());
            stage.initModality(Modality.WINDOW_MODAL);
            Scene scene = new Scene(root, 1200, 820);
            scene.getStylesheets().add(getClass().getResource("/ec/edu/uce/taller2flitros/styles.css").toExternalForm());
            stage.setScene(scene);
            stage.showAndWait();

            File result = controller.getResultImage();
            if (result != null) {
                selectedImageFile = result;
                loadImage(result);
                fileNameLabel.setText("Ecualización aplicada");
                currentFilterType = "EQUALIZATION";
                currentFilterLabel = "Ecualización";
                previewLabel.setText("Ecualización");
                activeFilterLabel.setText("Ecualización");
                activeFilterLabel.setVisible(true);
                activeFilterLabel.setManaged(true);
                processedImageFile = result;
                displayPreview(result);
                statusLabel.setText("Ecualización aplicada");
            }
        } catch (Exception e) {
            showError("No se pudo abrir la ventana de ecualización: " + e.getMessage());
        }
    }

    private File createGradient(String type, java.awt.Color c1, java.awt.Color c2, File outputFile) throws IOException {
        int width = 900;
        int height = 600;
        java.awt.image.BufferedImage image = new java.awt.image.BufferedImage(width, height, java.awt.image.BufferedImage.TYPE_INT_ARGB);
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                double t;
                if (type.contains("Izquierda a Derecha")) {
                    t = x / (double) Math.max(1, width - 1);
                } else if (type.contains("Derecha a Izquierda")) {
                    t = 1.0 - (x / (double) Math.max(1, width - 1));
                } else if (type.contains("Arriba a Abajo")) {
                    t = y / (double) Math.max(1, height - 1);
                } else if (type.contains("Abajo a Arriba")) {
                    t = 1.0 - (y / (double) Math.max(1, height - 1));
                } else {
                    double cx = width / 2.0;
                    double cy = height / 2.0;
                    double dx = x - cx;
                    double dy = y - cy;
                    t = Math.min(1.0, Math.sqrt(dx * dx + dy * dy) / Math.sqrt(cx * cx + cy * cy));
                }
                int r = (int) (c1.getRed() + t * (c2.getRed() - c1.getRed()));
                int g = (int) (c1.getGreen() + t * (c2.getGreen() - c1.getGreen()));
                int b = (int) (c1.getBlue() + t * (c2.getBlue() - c1.getBlue()));
                image.setRGB(x, y, (255 << 24) | (clamp(r) << 16) | (clamp(g) << 8) | clamp(b));
            }
        }
        javax.imageio.ImageIO.write(image, "png", outputFile);
        return outputFile;
    }

    private void loadImage(File file) {
        try {
            originalImageView.setImage(new Image(new FileInputStream(file)));
            originalPlaceholder.setVisible(false);
            originalPlaceholder.setManaged(false);
            previewImageView.setImage(null);
            previewPlaceholder.setVisible(true);
            previewPlaceholder.setManaged(true);
            previewLabel.setText("");
        } catch (Exception e) {
            showError("No se pudo cargar la imagen: " + e.getMessage());
        }
    }

    private void selectFilter(String filterType) {
        currentFilterType = filterType;
        currentFilterLabel = filterNames.getOrDefault(filterType, filterType);
        activeFilterLabel.setText(currentFilterLabel);
        activeFilterLabel.setVisible(true);
        activeFilterLabel.setManaged(true);
        statusLabel.setText("Filtro seleccionado: " + currentFilterLabel);
        if (selectedImageFile != null && autoPreviewEnabled) {
            applyFilterPreview();
        }
        updateUIState();
    }

    @FXML
    private void handleApplyFilter() {
        applyFilterPreview();
    }

    @FXML
    private void handleRandomImage() {
        runTask("Generando imagen aleatoria...", () -> service.randomImage(), true);
    }

    @FXML
    private void handleCopyImage() {
        if (selectedImageFile == null) {
            showError("Primero selecciona una imagen.");
            return;
        }
        runTask("Copiando imagen...", () -> service.copyImage(selectedImageFile), false);
    }

    @FXML
    private void handleSaveImage() {
        if (processedImageFile == null) {
            showError("No hay una imagen procesada para guardar.");
            return;
        }

        FileChooser chooser = new FileChooser();
        chooser.setTitle("Guardar imagen");
        chooser.setInitialFileName("filtrado.png");
        chooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("PNG", "*.png"),
                new FileChooser.ExtensionFilter("JPEG", "*.jpg"),
                new FileChooser.ExtensionFilter("Todos los archivos", "*.*")
        );
        File target = chooser.showSaveDialog(getStage());
        if (target != null) {
            try {
                java.nio.file.Files.copy(processedImageFile.toPath(), target.toPath(), java.nio.file.StandardCopyOption.REPLACE_EXISTING);
                statusLabel.setText("Imagen guardada: " + target.getName());
            } catch (IOException e) {
                showError("No se pudo guardar la imagen: " + e.getMessage());
            }
        }
    }

    @FXML
    private void handleRestoreBeforeFilter() {
        processedImageFile = null;
        currentFilterType = "";
        currentFilterLabel = "";

        previewImageView.setImage(null);
        previewPlaceholder.setVisible(true);
        previewPlaceholder.setManaged(true);
        previewLabel.setText("");

        activeFilterLabel.setText("");
        activeFilterLabel.setVisible(false);
        activeFilterLabel.setManaged(false);

        saveImageBtn.setDisable(true);
        applyFilterBtn.setDisable(selectedImageFile == null);
        statusLabel.setText("Imagen restaurada al estado original");
        updateUIState();
    }

    @FXML
    private void handleReset() {
        selectedImageFile = null;
        processedImageFile = null;
        currentFilterType = "";
        currentFilterLabel = "";
        previewImageView.setImage(null);
        previewPlaceholder.setVisible(true);
        previewPlaceholder.setManaged(true);
        previewLabel.setText("");
        activeFilterLabel.setText("");
        activeFilterLabel.setVisible(false);
        activeFilterLabel.setManaged(false);
        originalImageView.setImage(null);
        originalPlaceholder.setVisible(true);
        originalPlaceholder.setManaged(true);
        brightnessSlider.setValue(0);
        saturationSlider.setValue(100);
        alphaSlider.setValue(255);
        saveImageBtn.setDisable(true);
        statusLabel.setText("Interfaz restablecida");
        updateUIState();
    }

    private void applyFilterPreview() {
        if (selectedImageFile == null || currentFilterType.isEmpty()) {
            return;
        }
        runTask("Aplicando filtro...", () -> applyFilter(selectedImageFile, currentFilterType), false);
    }

    private File applyFilter(File inputFile, String filterType) throws IOException {
        return switch (filterType) {
            case "GRAYSCALE" -> service.applyGrayscale(inputFile);
            case "BLACKWHITE" -> service.applyBlackWhite(inputFile);
            case "GRAYSCALE_HSV" -> service.applyGrayscaleHsv(inputFile);
            case "RETRO1" -> service.applyRetro1(inputFile);
            case "RETRO2" -> service.applyRetro2(inputFile);
            case "NEGATIVE" -> service.applyNegative(inputFile);
            case "BLEND" -> service.applyBlending(inputFile);
            case "EQUALIZATION" -> service.applyEqualization(inputFile);
            case "HSV_FILTERS" -> service.applyHsvAdjust(inputFile, (int) brightnessSlider.getValue(), (int) saturationSlider.getValue(), (int) alphaSlider.getValue());
            case "HSV_SATURATION" -> service.applyHsvAdjust(inputFile, 0, (int) saturationSlider.getValue(), 255);
            case "BRIGHTNESS_CHANNEL" -> service.applyBrightnessByChannel(inputFile, 25);
            case "ALPHA_CHANNEL" -> service.applyAlphaChannel(inputFile, (int) alphaSlider.getValue());
            case "CIRCULAR_FADE" -> service.applyCircularFade(inputFile);
            case "FROSTED_GLASS" -> service.applyFrostedGlass(inputFile);
            case "BIT_MASK" -> service.applyBitMaskCrop(inputFile);
            case "HISTOGRAM" -> service.applyHistogramRgb(inputFile);
            case "STENCIL_CIRCLE" -> service.applyStencilCircle(inputFile);
            case "ALPHA_TEST" -> service.applyAlphaTest(inputFile);
            case "LOGIC_OP" -> service.applyLogicOpXor(inputFile);
            case "DEPTH_DEMO" -> service.applyDepthDemo(inputFile);
            case "BLEND_DEMO" -> service.applyBlendingDemo(inputFile);
            case "CONV_MANUAL" -> service.applyConvolutionManual(inputFile, ec.edu.uce.taller2flitros.model.kerlnes.kEnfoque, "Manual");
            case "CONV_OP" -> service.applyConvolutionOp(inputFile, "ENFOQUE");
            case "AMANECER_X10" -> service.applyAmanecerX10(inputFile);
            case "GRAD_H" -> service.applyRandomGradient(inputFile, "HORIZONTAL");
            case "GRAD_V" -> service.applyRandomGradient(inputFile, "VERTICAL");
            case "GRAD_R" -> service.applyRandomGradient(inputFile, "RADIAL");
            case "GRAD_R2" -> service.applyRandomGradient(inputFile, "RADIAL_SOFT");
            case "KERNEL_NORMAL" -> service.applyConvolutionOp(inputFile, "NORMAL");
            case "KERNEL_SHARPEN" -> service.applyConvolutionOp(inputFile, "ENFOQUE");
            case "KERNEL_BLUR3" -> service.applyConvolutionOp(inputFile, "DESENFOQUE_3X3");
            case "KERNEL_BLUR9" -> service.applyConvolutionOp(inputFile, "DESENFOQUE_9X9");
            case "KERNEL_EDGES4" -> service.applyConvolutionOp(inputFile, "BORDES_4V");
            case "KERNEL_EDGES8" -> service.applyConvolutionOp(inputFile, "BORDES_8V");
            case "KERNEL_LIGHTEN" -> service.applyConvolutionOp(inputFile, "ACLARACION");
            case "KERNEL_DARKEN" -> service.applyConvolutionOp(inputFile, "OSCURECER");
            default -> throw new IOException("Filtro no reconocido: " + filterType);
        };
    }

    private void runTask(String message, TaskSupplier supplier, boolean replaceInputImage) {
        Task<File> task = new Task<>() {
            @Override
            protected File call() throws Exception {
                updateMessage(message);
                updateProgress(-1, 1);
                return supplier.get();
            }
        };

        progressLabel.textProperty().bind(task.messageProperty());
        progressBar.progressProperty().bind(task.progressProperty());
        task.setOnSucceeded(event -> {
            processedImageFile = task.getValue();
            if (processedImageFile != null) {
                if (replaceInputImage) {
                    selectedImageFile = processedImageFile;
                    loadImage(processedImageFile);
                    fileNameLabel.setText(processedImageFile.getName());
                }
                displayPreview(processedImageFile);
                statusLabel.setText("Resultado listo: " + currentFilterLabel);
            }
            progressBar.progressProperty().unbind();
            progressLabel.textProperty().unbind();
            progressBar.setProgress(0);
            progressLabel.setText("");
            processingIndicator.setStyle("-fx-text-fill: #4E7E58;");
        });
        task.setOnFailed(event -> {
            Throwable error = task.getException();
            showError(error != null ? error.getMessage() : "Error desconocido");
            progressBar.progressProperty().unbind();
            progressLabel.textProperty().unbind();
            progressBar.setProgress(0);
            progressLabel.setText("");
            processingIndicator.setStyle("-fx-text-fill: #B85C5C;");
        });
        new Thread(task, "image-filter-task").start();
    }

    private void displayPreview(File file) {
        try {
            previewImageView.setImage(new Image(new FileInputStream(file)));
            previewPlaceholder.setVisible(false);
            previewPlaceholder.setManaged(false);
            previewLabel.setText(currentFilterLabel);
            saveImageBtn.setDisable(false);
        } catch (Exception e) {
            showError("No se pudo mostrar el resultado: " + e.getMessage());
        }
    }

    private void updateUIState() {
        boolean hasImage = selectedImageFile != null;
        boolean hasFilter = currentFilterType != null && !currentFilterType.isBlank();
        applyFilterBtn.setDisable(!hasImage || !hasFilter);
        saveImageBtn.setDisable(processedImageFile == null);
        menuGenerar.setDisable(false);
        menuColor.setDisable(!hasImage);
        menuHsv.setDisable(!hasImage);
        menuDegradados.setDisable(false);
        menuEspeciales.setDisable(!hasImage);
        menuMascara.setDisable(!hasImage);
        menuExposiciones.setDisable(!hasImage);
        menuDemos.setDisable(false);
        menuConvolucion.setDisable(!hasImage);
        randomImageBtn.setDisable(false);
        copyImageBtn.setDisable(!hasImage);
    }

    private Stage getStage() {
        return (Stage) statusLabel.getScene().getWindow();
    }

    private void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
        statusLabel.setText("Error: " + message);
    }

    @FunctionalInterface
    private interface TaskSupplier {
        File get() throws Exception;
    }

    private int clamp(int value) {
        return Math.max(0, Math.min(255, value));
    }
}
