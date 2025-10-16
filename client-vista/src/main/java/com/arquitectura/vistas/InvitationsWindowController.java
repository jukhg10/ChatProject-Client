package com.arquitectura.vistas;

import com.arquitectura.controller.AppController;
import com.arquitectura.dto.ChannelViewDTO;
import com.arquitectura.dto.events.InvitationListUpdateEvent;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.application.Platform;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class InvitationsWindowController {

    @FXML private ListView<ChannelViewDTO> invitationsListView;
    @FXML private Button acceptButton;
    @FXML private Button rejectButton;
    @FXML private Label statusLabel;

    private final AppController appController;
    private ObservableList<ChannelViewDTO> invitations = FXCollections.observableArrayList();

    public InvitationsWindowController(AppController appController) {
        this.appController = appController;
    }

    @FXML
    private void initialize() {
        // Personalizamos cómo se muestra cada ítem en la lista
        invitationsListView.setCellFactory(param -> new ListCell<>() {
            @Override
            protected void updateItem(ChannelViewDTO item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null || item.getName() == null) {
                    setText(null);
                } else {
                    setText(item.getName());
                }
            }
        });
        invitationsListView.setItems(invitations);

        // Deshabilitar botones si no hay nada seleccionado
        acceptButton.disableProperty().bind(invitationsListView.getSelectionModel().selectedItemProperty().isNull());
        rejectButton.disableProperty().bind(invitationsListView.getSelectionModel().selectedItemProperty().isNull());
    }

    // Este método será llamado por el AppController para cargar las invitaciones
    public void loadInvitations() {
        appController.solicitarInvitaciones();
    }

    @FXML
    private void handleAcceptAction() {
        ChannelViewDTO selected = invitationsListView.getSelectionModel().getSelectedItem();
        if (selected != null) {
            appController.responderInvitacion(selected.getId(), true);
            statusLabel.setText("Invitación a '" + selected.getName() + "' aceptada.");
            invitations.remove(selected); // La quitamos de la lista
        }
    }

    @FXML
    private void handleRejectAction() {
        ChannelViewDTO selected = invitationsListView.getSelectionModel().getSelectedItem();
        if (selected != null) {
            appController.responderInvitacion(selected.getId(), false);
            statusLabel.setText("Invitación a '" + selected.getName() + "' rechazada.");
            invitations.remove(selected); // La quitamos de la lista
        }
    }

    // Escucha el evento que la capa de lógica dispara cuando recibe las invitaciones
    @EventListener
    public void onInvitationListUpdate(InvitationListUpdateEvent event) {
        Platform.runLater(() -> {
            invitations.setAll(event.getInvitations());
            if (invitations.isEmpty()) {
                statusLabel.setText("No tienes invitaciones pendientes.");
            } else {
                statusLabel.setText("");
            }
        });
    }
}