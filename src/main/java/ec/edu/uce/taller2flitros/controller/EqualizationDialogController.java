package ec.edu.uce.taller2flitros.controller;

import ec.edu.uce.taller2flitros.service.ImageFilterService;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileInputStream;

public class EqualizationDialogController {

    @FXML private ImageView originalImageView;
    @FXML private ImageView equalizedImageView;
    @FXML private Canvas originalHistogramCanvas;
    @FXML private Canvas equalizedHistogramCanvas;

    private ImageFilterService service;
    private File sourceImage;
    private File resultImage;

    public void setService(ImageFilterService service) {
        this.service = service;
    }

    public void setSourceImage(File sourceImage) {
        this.sourceImage = sourceImage;
    }

    public File getResultImage() {
        return resultImage;
    }

    @FXML
    public void initialize() {
        originalImageView.setPreserveRatio(true);
        equalizedImageView.setPreserveRatio(true);
    }

    public void prepare() throws Exception {
        if (service == null || sourceImage == null) {
            return;
        }

        originalImageView.setImage(new Image(new FileInputStream(sourceImage)));
        resultImage = service.equalize(sourceImage);
        equalizedImageView.setImage(new Image(new FileInputStream(resultImage)));

        drawHistogram(originalHistogramCanvas, service.histogramOf(sourceImage), "#8ec5ff", "Antes");
        drawHistogram(equalizedHistogramCanvas, service.histogramOf(resultImage), "#88c57d", "Después");
    }

    @FXML
    private void handleApply() {
        close();
    }

    @FXML
    private void handleCancel() {
        resultImage = null;
        close();
    }

    private void close() {
        Stage stage = (Stage) originalImageView.getScene().getWindow();
        stage.close();
    }

    private void drawHistogram(Canvas canvas, int[] histogram, String color, String title) {
        GraphicsContext g = canvas.getGraphicsContext2D();
        double w = canvas.getWidth();
        double h = canvas.getHeight();
        g.clearRect(0, 0, w, h);
        g.setFill(javafx.scene.paint.Color.web("#101826"));
        g.fillRoundRect(0, 0, w, h, 16, 16);

        int max = 1;
        for (int value : histogram) {
            max = Math.max(max, value);
        }

        double barW = Math.max(1.0, (w - 32) / 256.0);
        for (int i = 0; i < histogram.length; i++) {
            double barH = (histogram[i] / (double) max) * (h - 48);
            g.setFill(javafx.scene.paint.Color.web(color));
            g.fillRect(16 + i * barW, h - 16 - barH, Math.max(1, barW - 1), barH);
        }

        g.setFill(javafx.scene.paint.Color.web("#f4f7fb"));
        g.fillText(title, 16, 18);
    }
}
