package com.arquitectura.app;

import com.arquitectura.controller.AppController;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;

public class MainApplication extends Application {

    private ConfigurableApplicationContext springContext;

    @Override
    public void start(Stage primaryStage) throws Exception {
        // Inicia el contexto de Spring aquí, dentro del hilo de JavaFX
        springContext = new SpringApplicationBuilder(SpringApp.class).run();
        
        // El resto del código para mostrar la ventana
        AppController appController = springContext.getBean(AppController.class);
        appController.setPrimaryStage(primaryStage);

        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/vistas/Login.fxml"));
        fxmlLoader.setControllerFactory(springContext::getBean);
        Parent root = fxmlLoader.load();
        
        primaryStage.setTitle("Chat - Iniciar Sesión");
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
    }

    @Override
    public void stop() {
        if (springContext != null) {
            springContext.close();
        }
        Platform.exit();
    }
    
    // Este método es llamado por la clase Launcher
    public static void main(String[] args) {
        launch(args);
    }
}