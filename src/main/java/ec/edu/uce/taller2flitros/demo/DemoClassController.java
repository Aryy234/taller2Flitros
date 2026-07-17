package ec.edu.uce.taller2flitros.demo;

import javafx.animation.AnimationTimer;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class DemoClassController {

    @FXML private Canvas group1Canvas;
    @FXML private Canvas group3Canvas;
    @FXML private StackPane group1CanvasHolder;
    @FXML private StackPane group3CanvasHolder;
    @FXML private Label group1Description;
    @FXML private Label group3Description;

    private AnimationTimer timer;
    private double angle;

    @FXML
    public void initialize() {
        group1Description.setText("Grupo 1: un solo objeto 3D para mostrar rotación, proyección y sombreado simple.");
        group3Description.setText("Grupo 3: triángulos, textura, Z-buffer y rotación.");

        Platform.runLater(() -> {
            bindCanvas(group1Canvas, group1CanvasHolder, 16);
            bindCanvas(group3Canvas, group3CanvasHolder, 16);
            drawGroup1();
            drawGroup3();
        });

        timer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                angle += 0.02;
                drawGroup1();
                drawGroup3();
            }
        };
        timer.start();
    }

    @FXML
    private void handleClose() {
        if (timer != null) {
            timer.stop();
        }
        Stage stage = (Stage) group1Canvas.getScene().getWindow();
        stage.close();
    }

    private void drawGroup1() {
        GraphicsContext g = group1Canvas.getGraphicsContext2D();
        double w = group1Canvas.getWidth();
        double h = group1Canvas.getHeight();

        g.clearRect(0, 0, w, h);
        g.setFill(javafx.scene.paint.Color.web("#0f1520"));
        g.fillRect(0, 0, w, h);

        drawAxes(g, w, h);
        drawCube(g, w * 0.50, h * 0.54, Math.min(w, h) * 0.18, 2.8, javafx.scene.paint.Color.web("#8ec5ff"), javafx.scene.paint.Color.web("#2d5f8b"));

        g.setFill(javafx.scene.paint.Color.web("#f4f7fb"));
        g.fillText("Transformaciones 3D, proyección y sombreado", 18, 24);
        g.fillText("Tiempo de rotación: " + String.format("%.2f", angle), 18, h - 18);
    }

    private void drawGroup3() {
        GraphicsContext g = group3Canvas.getGraphicsContext2D();
        double w = group3Canvas.getWidth();
        double h = group3Canvas.getHeight();

        g.clearRect(0, 0, w, h);
        g.setFill(javafx.scene.paint.Color.web("#101826"));
        g.fillRect(0, 0, w, h);

        double cx = w / 2;
        double cy = h / 2;
        double r = Math.min(w, h) * 0.22;

        double x1 = cx + Math.cos(angle) * r;
        double y1 = cy + Math.sin(angle) * r;
        double x2 = cx + Math.cos(angle + 2.094) * r;
        double y2 = cy + Math.sin(angle + 2.094) * r;
        double x3 = cx + Math.cos(angle + 4.188) * r;
        double y3 = cy + Math.sin(angle + 4.188) * r;

        g.setFill(javafx.scene.paint.Color.web("#8ec5ff", 0.35));
        g.fillPolygon(new double[]{x1, x2, x3}, new double[]{y1, y2, y3}, 3);
        g.setStroke(javafx.scene.paint.Color.web("#f0b36d"));
        g.setLineWidth(3);
        g.strokePolygon(new double[]{x1, x2, x3}, new double[]{y1, y2, y3}, 3);

        g.setFill(javafx.scene.paint.Color.web("#f4f7fb"));
        g.fillText("Triángulos + Z-buffer conceptual", 18, 24);
        g.fillText("Animación de rotación", 18, h - 18);
    }

    private void bindCanvas(Canvas canvas, StackPane holder, double padding) {
        canvas.widthProperty().bind(holder.widthProperty().subtract(padding));
        canvas.heightProperty().bind(holder.heightProperty().subtract(padding));
    }

    private void drawAxes(GraphicsContext g, double w, double h) {
        double cx = w * 0.18;
        double cy = h * 0.78;
        g.setStroke(javafx.scene.paint.Color.web("#4b5c72"));
        g.setLineWidth(2);
        g.strokeLine(cx, cy, cx + 120, cy - 10);
        g.strokeLine(cx, cy, cx - 15, cy - 110);
        g.strokeLine(cx, cy, cx + 35, cy + 35);
        g.setFill(javafx.scene.paint.Color.web("#4b5c72"));
        g.fillText("X", cx + 126, cy - 12);
        g.fillText("Y", cx - 26, cy - 118);
        g.fillText("Z", cx + 40, cy + 44);
    }

    private void drawCube(GraphicsContext g, double centerX, double centerY, double size, double depthFactor,
                          javafx.scene.paint.Color front, javafx.scene.paint.Color back) {
        double[][] points = {
                {-1, -1, -1}, {1, -1, -1}, {1, 1, -1}, {-1, 1, -1},
                {-1, -1, 1}, {1, -1, 1}, {1, 1, 1}, {-1, 1, 1}
        };
        int[][] edges = {
                {0, 1}, {1, 2}, {2, 3}, {3, 0},
                {4, 5}, {5, 6}, {6, 7}, {7, 4},
                {0, 4}, {1, 5}, {2, 6}, {3, 7}
        };
        double[][] projected = projectShape(points, centerX, centerY, size, depthFactor, 0.9, 0.7);
        g.setStroke(front);
        g.setLineWidth(2.4);
        drawEdges(g, projected, edges);
        g.setFill(back.deriveColor(0, 1, 1, 0.3));
        g.fillPolygon(
                new double[]{projected[4][0], projected[5][0], projected[6][0], projected[7][0]},
                new double[]{projected[4][1], projected[5][1], projected[6][1], projected[7][1]},
                4);
    }

    private void drawPyramid(GraphicsContext g, double centerX, double centerY, double size, double depthFactor,
                             javafx.scene.paint.Color front, javafx.scene.paint.Color back) {
        double[][] points = {
                {-1, -1, -1}, {1, -1, -1}, {1, -1, 1}, {-1, -1, 1},
                {0, 1.2, 0.2}
        };
        int[][] edges = {
                {0, 1}, {1, 2}, {2, 3}, {3, 0},
                {0, 4}, {1, 4}, {2, 4}, {3, 4}
        };
        double[][] projected = projectShape(points, centerX, centerY, size, depthFactor, 1.2, 0.9);
        g.setStroke(front);
        g.setLineWidth(2.3);
        drawEdges(g, projected, edges);
        g.setFill(back.deriveColor(0, 1, 1, 0.22));
        g.fillPolygon(
                new double[]{projected[0][0], projected[1][0], projected[2][0], projected[3][0]},
                new double[]{projected[0][1], projected[1][1], projected[2][1], projected[3][1]},
                4);
    }

    private void drawTetra(GraphicsContext g, double centerX, double centerY, double size, double depthFactor,
                           javafx.scene.paint.Color front, javafx.scene.paint.Color back) {
        double[][] points = {
                {0, 1, 0}, {-1, -1, 1}, {1, -1, 1}, {0, -1, -1}
        };
        int[][] edges = {
                {0, 1}, {0, 2}, {0, 3},
                {1, 2}, {2, 3}, {3, 1}
        };
        double[][] projected = projectShape(points, centerX, centerY, size, depthFactor, 0.6, 1.3);
        g.setStroke(front);
        g.setLineWidth(2.1);
        drawEdges(g, projected, edges);
        g.setFill(back.deriveColor(0, 1, 1, 0.18));
        g.fillPolygon(
                new double[]{projected[1][0], projected[2][0], projected[3][0]},
                new double[]{projected[1][1], projected[2][1], projected[3][1]},
                3);
    }

    private double[][] projectShape(double[][] points, double centerX, double centerY, double size, double depthFactor,
                                    double rotateYFactor, double rotateXFactor) {
        double[][] projected = new double[points.length][2];
        for (int i = 0; i < points.length; i++) {
            double x = points[i][0];
            double y = points[i][1];
            double z = points[i][2];
            double ry = x * Math.cos(angle * rotateYFactor) + z * Math.sin(angle * rotateYFactor);
            double rz = -x * Math.sin(angle * rotateYFactor) + z * Math.cos(angle * rotateYFactor);
            double rx = y * Math.cos(angle * rotateXFactor) - rz * Math.sin(angle * rotateXFactor);
            double depth = depthFactor - rz;
            double scale = size / depth;
            projected[i][0] = centerX + ry * scale;
            projected[i][1] = centerY + rx * scale;
        }
        return projected;
    }

    private void drawEdges(GraphicsContext g, double[][] projected, int[][] edges) {
        for (int[] edge : edges) {
            g.strokeLine(projected[edge[0]][0], projected[edge[0]][1], projected[edge[1]][0], projected[edge[1]][1]);
        }
    }
}
