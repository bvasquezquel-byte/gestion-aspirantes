package edu.ingsoft.bvasquez.quel.util;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

public final class AlertHelper {

    private AlertHelper() { }

    public static void info(String titulo, String mensaje) {
        mostrar(Alert.AlertType.INFORMATION, titulo, mensaje);
    }

    public static void warning(String titulo, String mensaje) {
        mostrar(Alert.AlertType.WARNING, titulo, mensaje);
    }

    public static void error(String titulo, String mensaje) {
        mostrar(Alert.AlertType.ERROR, titulo, mensaje);
    }

    public static boolean confirmar(String titulo, String mensaje) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, mensaje, ButtonType.OK, ButtonType.CANCEL);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        return alert.showAndWait().filter(b -> b == ButtonType.OK).isPresent();
    }

    private static void mostrar(Alert.AlertType tipo, String titulo, String mensaje) {
        Alert alert = new Alert(tipo);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}
