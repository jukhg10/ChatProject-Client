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
public void initialize() {
    invitationsListView.setCellFactory(param -> new ListCell<ChannelViewDTO>() {
        @Override
        protected void updateItem(ChannelViewDTO item, boolean empty) {
            super.updateItem(item, empty);
            if (empty || item == null) {
                setText(null);
            } else {
                setText(item.getChannelName()); // <-- FIX: Was getName()
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
    // This line was correct, it gets the selected item
    ChannelViewDTO selectedInvitation = invitationsListView.getSelectionModel().getSelectedItem();

    // The 'if' condition was correct
    if (selectedInvitation != null) {
        // FIX: The variable name inside the 'if' was wrong.
        // It should use 'selectedInvitation', which we just declared.
        appController.responderInvitacion(selectedInvitation.getChannelId(), true);
        statusLabel.setText("Aceptada la invitación a: " + selectedInvitation.getChannelName());
        
        // This line was also correct
        invitationsListView.getItems().remove(selectedInvitation);
    }
}

    @FXML
private void handleRejectAction() {
    // This line was correct
    ChannelViewDTO selectedInvitation = invitationsListView.getSelectionModel().getSelectedItem();

    // The 'if' condition was correct
    if (selectedInvitation != null) {
        // FIX: The variable name inside the 'if' was also wrong here.
        appController.responderInvitacion(selectedInvitation.getChannelId(), false);
        statusLabel.setText("Rechazada la invitación a: " + selectedInvitation.getChannelName());
        
        // This line was correct
        invitationsListView.getItems().remove(selectedInvitation);
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