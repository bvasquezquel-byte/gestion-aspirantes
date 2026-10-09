package edu.ingsoft.bvasquez.quel.util;

import edu.ingsoft.bvasquez.quel.exception.AppException;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

public class SceneManager {

    public enum View {
        LOGIN("login-view.fxml"),
        REGISTER("register-view.fxml"),
        MAIN_LAYOUT("MainLayout.fxml"),
        DASHBOARD("dashboard-view.fxml"),
        ASPIRANTES("aspirante-view.fxml");

        private final String file;
        View(String file) { this.file = file; }
        public String file() { return file; }
    }

    private static final String VIEW_PATH = "/view/";
    private static final String CSS_PATH = "/css/styles.css";

    private final Stage stage;
    private final Map<Class<?>, Supplier<?>> controllerFactories = new HashMap<>();
    private Pane contentHost;

    public SceneManager(Stage stage) {
        this.stage = stage;
    }

    public <T> void register(Class<T> controllerType, Supplier<? extends T> factory) {
        controllerFactories.put(controllerType, factory);
    }

    /** Reemplaza la escena completa (login, registro, layout principal). */
    public void switchTo(View view) {
        Scene scene = new Scene(load(view));
        URL css = getClass().getResource(CSS_PATH);
        if (css != null) {
            scene.getStylesheets().add(css.toExternalForm());
        }
        stage.setScene(scene);
        stage.sizeToScene();
        stage.centerOnScreen();
    }

    /** Define el panel central del layout principal donde se cargan las vistas internas. */
    public void setContentHost(Pane contentHost) {
        this.contentHost = contentHost;
    }

    public void showInContent(View view) {
        if (contentHost == null) {
            throw new AppException("El layout principal no está inicializado.");
        }
        contentHost.getChildren().setAll(load(view));
    }

    private Parent load(View view) {
        URL location = getClass().getResource(VIEW_PATH + view.file());
        if (location == null) {
            throw new AppException("No se encontró la vista: " + view.file());
        }
        FXMLLoader loader = new FXMLLoader(location);
        loader.setControllerFactory(this::createController);
        try {
            return loader.load();
        } catch (IOException e) {
            e.printStackTrace(); // Muestra el error real con todo el rastreo en la consola
            throw new AppException("No se pudo cargar la vista " + view.file(), e);
        }
    }

    private Object createController(Class<?> type) {
        Supplier<?> factory = controllerFactories.get(type);
        if (factory == null) {
            throw new AppException("Controlador no registrado: " + type.getSimpleName());
        }
        return factory.get();
    }
}