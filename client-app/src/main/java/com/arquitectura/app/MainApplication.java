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

// Importa las clases List y ArrayList
import java.util.ArrayList;
import java.util.List;

public class MainApplication extends Application {

    private ConfigurableApplicationContext springContext;

    @Override
public void start(Stage primaryStage) throws Exception {
    // Get the original application arguments
    List<String> rawParams = getParameters().getRaw();

    // Create a new list of arguments for Spring
    List<String> springArgs = new ArrayList<>(rawParams);
    
    // Add the location of the configuration file as a command-line argument
    springArgs.add("--spring.config.location=file:./application.properties");

    // Start the Spring application with the new arguments
    springContext = new SpringApplicationBuilder(SpringApp.class)
            .run(springArgs.toArray(new String[0]));

    // The rest of your code remains the same
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
    
    public static void main(String[] args) {
        launch(args);
    }
}