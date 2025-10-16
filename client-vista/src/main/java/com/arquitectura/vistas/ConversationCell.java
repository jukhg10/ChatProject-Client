package com.arquitectura.vistas;

import com.arquitectura.dto.ConversationItemDTO;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;

import java.io.IOException;

public class ConversationCell extends ListCell<ConversationItemDTO> {

    @FXML
    private HBox hBox;
    @FXML
    private ImageView avatarImageView;
    @FXML
    private Label nameLabel;
    @FXML
    private Label lastMessageLabel;

    private FXMLLoader fxmlLoader;

    @Override
    protected void updateItem(ConversationItemDTO item, boolean empty) {
        super.updateItem(item, empty);

        if (empty || item == null) {
            setText(null);
            setGraphic(null);
        } else {
            if (fxmlLoader == null) {
                fxmlLoader = new FXMLLoader(getClass().getResource("/vistas/ConversationCell.fxml"));
                fxmlLoader.setController(this);
                try {
                    fxmlLoader.load();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            nameLabel.setText(item.getConversationName());
            lastMessageLabel.setText(item.getLastMessage());
            
            // Cargar imagen (con una imagen por defecto si falla)
            try {
                Image avatar = new Image(item.getImageUrl(), true); // true para cargar en segundo plano
                avatarImageView.setImage(avatar);
            } catch (Exception e) {
                // En caso de URL inválida o error de red, usamos una imagen por defecto
                avatarImageView.setImage(new Image(getClass().getResourceAsStream("/assets/default_avatar.png")));
            }

            setGraphic(hBox);
        }
    }
}