package com.arquitectura.controller;

import com.arquitectura.dto.events.LoginSuccessEvent;
import com.arquitectura.logica.IClienteFachada;
import com.arquitectura.logica.ports.INetworkInputPort;
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
import org.springframework.beans.factory.annotation.Value;


@Controller
public class AppController {

    private final IClienteFachada fachada;
    private final ServerConnection serverConnection;
    private final ConfigurableApplicationContext springContext;
    private Stage primaryStage;
    @Value("${server.ip}")
    private String serverIp;

    @Value("${server.port}")
    private int serverPort;
    
    @Autowired
    public AppController(IClienteFachada fachada, ServerConnection serverConnection, ConfigurableApplicationContext springContext) {
        this.fachada = fachada;
        this.serverConnection = serverConnection;
        this.springContext = springContext;
    }

    public void setPrimaryStage(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    public void connectAndLogin(String username, String password) throws Exception {
        serverConnection.connect(serverIp, serverPort);
        
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
            
        });
    }

    public void solicitarListaCanales() {
        fachada.solicitarListaCanales();
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

  
    public void crearCanalGrupo(String channelName) {
    System.out.println("CONTROLADOR: Solicitando crear canal de grupo '" + channelName + "'");
    fachada.crearCanalGrupo(channelName);
}
    
    public void enviarMensaje(int channelId, String content) {
        fachada.enviarMensajeTexto(channelId, content);
    }

    public void solicitarHistorialMensajes(int channelId) {
        fachada.solicitarHistorialMensajes(channelId);
    }

    public void solicitarInvitaciones() {
        fachada.solicitarInvitaciones();
    }

    public void responderInvitacion(int channelId, boolean aceptada) {
        fachada.responderInvitacion(channelId, aceptada);
    }
    public void invitarUsuario(int channelId, int userIdToInvite) {
    fachada.invitarUsuario(channelId, userIdToInvite);
}

    public void enviarMensajeAudio(int channelId, String filePath) {
        fachada.enviarMensajeAudio(channelId, filePath);
    }
    public void solicitarListaUsuarios() {
    fachada.solicitarListaUsuarios();
}
    public void disconnectFromServer() throws Exception {
        fachada.logout();
        serverConnection.disconnect();
    }
}