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
    public void init() {
        springContext = new SpringApplicationBuilder(SpringApp.class).run();
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        // Inyectamos el Stage en nuestro AppController para que pueda manejar las escenas
        AppController appController = springContext.getBean(AppController.class);
        appController.setPrimaryStage(primaryStage);

        // Cargamos la vista de Login primero
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/vistas/Login.fxml"));
        fxmlLoader.setControllerFactory(springContext::getBean);
        Parent root = fxmlLoader.load();
        
        primaryStage.setTitle("Chat - Iniciar Sesión");
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
    }

    @Override
    public void stop() {
        springContext.close();
        Platform.exit();
    }
}