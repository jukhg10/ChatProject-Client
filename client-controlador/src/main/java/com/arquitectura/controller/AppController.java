package com.arquitectura.controller;

import com.arquitectura.dto.events.LoginSuccessEvent;
import com.arquitectura.logica.IClienteFachada;
import com.arquitectura.logica.ports.INetworkInputPort; // <-- THIS IMPORT WAS MISSING
import com.arquitectura.net.ServerListener;
import com.arquitectura.transporte.ServerConnection;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Controller;

import java.util.List;

@Controller
public class AppController {

    private final IClienteFachada fachada;
    private final ServerConnection serverConnection;
    private final ConfigurableApplicationContext springContext;
    private Stage primaryStage;

    @Autowired
    public AppController(IClienteFachada fachada, ServerConnection serverConnection, ConfigurableApplicationContext springContext) {
        this.fachada = fachada;
        this.serverConnection = serverConnection;
        this.springContext = springContext;
    }

    public void setPrimaryStage(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    public void connectAndLogin(String ip, int port, String username, String password) throws Exception {
        serverConnection.connect(ip, port);
        
        ServerListener listener = springContext.getBean(ServerListener.class);
        listener.setInputStream(serverConnection.getInputStream());
        listener.setNetworkInputPort((INetworkInputPort) fachada);
        new Thread(listener).start();
        
        fachada.login(username, password);
    }
    
    @EventListener
    public void onLoginSuccess(LoginSuccessEvent event) {
        Platform.runLater(() -> {
            showMainWindow();
            fachada.solicitarListaUsuarios();
            fachada.solicitarListaCanales();
        });
    }

    public void showMainWindow() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/vistas/MainWindow.fxml"));
            fxmlLoader.setControllerFactory(springContext::getBean);
            Parent root = fxmlLoader.load();
            Scene scene = new Scene(root, 800, 600);
            primaryStage.setScene(scene);
            primaryStage.setTitle("Chat");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void iniciarChatDirecto(String username) {
    System.out.println("CONTROLADOR: Solicitando chat directo con '" + username + "'");
    fachada.solicitarChatDirecto(username);
}
    public void crearCanalGrupo(String channelName, List<String> invitedUsers) {
        System.out.println("CONTROLADOR: Solicitando crear canal '" + channelName + "'");
        fachada.crearCanalGrupo(channelName);
    }
    
    public void sendMessage(int channelId, String content) {
        fachada.sendMessage(channelId, content);
    }

    public void disconnectFromServer() throws Exception {
        serverConnection.disconnect();
    }
}