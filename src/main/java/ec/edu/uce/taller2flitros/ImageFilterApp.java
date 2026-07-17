package ec.edu.uce.taller2flitros;


import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Clase principal de la aplicación Pixel Forge
 * Editor de filtros de imagen con JavaFX
 */
public class ImageFilterApp extends Application {
    
    private static final String APP_TITLE = "Generador de Flitros";
    private static final int WINDOW_WIDTH = 1400;
    private static final int WINDOW_HEIGHT = 900;
    private static final int MIN_WIDTH = 1200;
    private static final int MIN_HEIGHT = 700;
    
    @Override
    public void start(Stage primaryStage) {
        try {
            // Cargar el archivo FXML
            FXMLLoader loader = new FXMLLoader(getClass().getResource("ImageFilterApp.fxml"));
            Parent root = loader.load();
            
            // Crear la escena
            Scene scene = new Scene(root, WINDOW_WIDTH, WINDOW_HEIGHT);
            
            // Agregar el archivo CSS
            scene.getStylesheets().add(getClass().getResource("styles.css").toExternalForm());
            
            // Configurar el Stage
            primaryStage.setTitle(APP_TITLE);
            primaryStage.setScene(scene);
            primaryStage.setMinWidth(MIN_WIDTH);
            primaryStage.setMinHeight(MIN_HEIGHT);
            
            // Opcional: Agregar ícono de la aplicación
            // primaryStage.getIcons().add(new Image(getClass().getResourceAsStream("icon.png")));
            
            // Mostrar la ventana
            primaryStage.show();
            
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Error al iniciar la aplicación: " + e.getMessage());
        }
    }
    
    /**
     * Método principal
     */
    public static void main(String[] args) {
        launch(args);
    }
}
