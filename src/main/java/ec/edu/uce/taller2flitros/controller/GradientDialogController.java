package ec.edu.uce.taller2flitros.controller;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ColorPicker;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.RadialGradient;
import javafx.scene.paint.Stop;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

public class GradientDialogController {

    @FXML private ComboBox<String> gradientTypeCombo;
    @FXML private ColorPicker color1Picker;
    @FXML private ColorPicker color2Picker;
    @FXML private Rectangle previewRect;

    private boolean confirmed = false;

    @FXML
    public void initialize() {
        gradientTypeCombo.setItems(FXCollections.observableArrayList(
            "Lineal (Izquierda a Derecha)",
            "Lineal (Derecha a Izquierda)",
            "Lineal (Arriba a Abajo)",
            "Lineal (Abajo a Arriba)",
            "Circular (Radial)"
        ));
        gradientTypeCombo.getSelectionModel().selectFirst();
        
        color1Picker.setValue(Color.web("#7C9EBD")); // Color primario pastel
        color2Picker.setValue(Color.web("#EDF3EE")); // Color de fondo claro

        // Listeners for live preview (simplemente visual)
        gradientTypeCombo.valueProperty().addListener((obs, oldVal, newVal) -> updatePreview());
        color1Picker.valueProperty().addListener((obs, oldVal, newVal) -> updatePreview());
        color2Picker.valueProperty().addListener((obs, oldVal, newVal) -> updatePreview());

        updatePreview();
    }

    private void updatePreview() {
        Color c1 = color1Picker.getValue();
        Color c2 = color2Picker.getValue();
        String type = gradientTypeCombo.getValue();
        
        Stop[] stops = new Stop[] { new Stop(0, c1), new Stop(1, c2) };

        if (type == null) return;
        
        switch (type) {
            case "Lineal (Izquierda a Derecha)":
                previewRect.setFill(new LinearGradient(0, 0, 1, 0, true, CycleMethod.NO_CYCLE, stops));
                break;
            case "Lineal (Arriba a Abajo)":
                previewRect.setFill(new LinearGradient(0, 0, 0, 1, true, CycleMethod.NO_CYCLE, stops));
                break;
            case "Lineal (Diagonal)":
                previewRect.setFill(new LinearGradient(0, 0, 1, 1, true, CycleMethod.NO_CYCLE, stops));
                break;
            case "Circular (Radial)":
                previewRect.setFill(new RadialGradient(0, 0, 0.5, 0.5, 0.5, true, CycleMethod.NO_CYCLE, stops));
                break;
        }
    }

    @FXML
    private void handleGenerate() {
        confirmed = true;
        closeDialog();
    }

    @FXML
    private void handleClose() {
        confirmed = false;
        closeDialog();
    }

    private void closeDialog() {
        Stage stage = (Stage) previewRect.getScene().getWindow();
        stage.close();
    }
    
    public boolean isConfirmed() {
        return confirmed;
    }
    
    public String getSelectedType() {
        return gradientTypeCombo.getValue();
    }
    
    public java.awt.Color getSelectedColor1() {
        Color fxColor = color1Picker.getValue();
        return new java.awt.Color((float)fxColor.getRed(), (float)fxColor.getGreen(), (float)fxColor.getBlue(), (float)fxColor.getOpacity());
    }
    
    public java.awt.Color getSelectedColor2() {
        Color fxColor = color2Picker.getValue();
        return new java.awt.Color((float)fxColor.getRed(), (float)fxColor.getGreen(), (float)fxColor.getBlue(), (float)fxColor.getOpacity());
    }
}
