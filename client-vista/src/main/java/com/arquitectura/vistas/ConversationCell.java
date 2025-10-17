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
            // This is the fix:
            // Use the item's getConversationName() method to set the text.
            setText(item.getConversationName()); 
        }
    }
}