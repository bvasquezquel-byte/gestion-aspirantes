package edu.ingsoft.bvasquez.quel;

import edu.ingsoft.bvasquez.quel.config.DatabaseConfig;
import edu.ingsoft.bvasquez.quel.controller.AspiranteController;
import edu.ingsoft.bvasquez.quel.controller.DashboardController;
import edu.ingsoft.bvasquez.quel.controller.LoginController;
import edu.ingsoft.bvasquez.quel.controller.MainLayoutController;
import edu.ingsoft.bvasquez.quel.controller.RegisterController;
import edu.ingsoft.bvasquez.quel.exception.AppException;
import edu.ingsoft.bvasquez.quel.repository.AspiranteRepository;
import edu.ingsoft.bvasquez.quel.repository.JdbcAspiranteRepository;
import edu.ingsoft.bvasquez.quel.repository.JdbcRolRepository;
import edu.ingsoft.bvasquez.quel.repository.JdbcUsuarioRepository;
import edu.ingsoft.bvasquez.quel.repository.RolRepository;
import edu.ingsoft.bvasquez.quel.repository.UsuarioRepository;
import edu.ingsoft.bvasquez.quel.security.PasswordHasher;
import edu.ingsoft.bvasquez.quel.security.SessionManager;
import edu.ingsoft.bvasquez.quel.service.AspiranteService;
import edu.ingsoft.bvasquez.quel.service.AuthService;
import edu.ingsoft.bvasquez.quel.service.DashBoardService;
import edu.ingsoft.bvasquez.quel.util.AlertHelper;
import edu.ingsoft.bvasquez.quel.util.SceneManager;
import javafx.application.Application;
import javafx.stage.Stage;

public class MainApp extends Application {

    @Override
    public void start(Stage stage) {
        DatabaseConfig database = new DatabaseConfig();

        RolRepository rolRepository = new JdbcRolRepository(database);
        UsuarioRepository usuarioRepository = new JdbcUsuarioRepository(database);
        AspiranteRepository aspiranteRepository = new JdbcAspiranteRepository(database);

        SessionManager sessionManager = new SessionManager();
        AuthService authService = new AuthService(usuarioRepository, rolRepository,
                new PasswordHasher(), sessionManager);
        AspiranteService aspiranteService = new AspiranteService(aspiranteRepository);
        DashBoardService dashBoardService = new DashBoardService(aspiranteRepository);

        SceneManager sceneManager = new SceneManager(stage);
        sceneManager.register(LoginController.class, () -> new LoginController(authService, sceneManager));
        sceneManager.register(RegisterController.class, () -> new RegisterController(authService, sceneManager));
        sceneManager.register(MainLayoutController.class, () -> new MainLayoutController(authService, sceneManager));
        sceneManager.register(DashboardController.class, () -> new DashboardController(dashBoardService));
        sceneManager.register(AspiranteController.class, () -> new AspiranteController(aspiranteService));

        stage.setTitle("Gestión de Aspirantes");
        try {
            sceneManager.switchTo(SceneManager.View.LOGIN);
            stage.show();
        } catch (AppException e) {
            AlertHelper.error("Error al iniciar", e.getMessage());
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
