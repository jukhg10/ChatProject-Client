package com.arquitectura.dto;

public class ChannelViewDTO {

    // FIX: Renombrar 'id' a 'channelId' para que coincida con el JSON del servidor
    private final int channelId;

    // FIX: Renombrar 'name' a 'channelName' para que coincida con el JSON del servidor
    private final String channelName;

    // El constructor y los getters también deben ser actualizados
    public ChannelViewDTO(int channelId, String channelName) {
        this.channelId = channelId;
        this.channelName = channelName;
    }

    // FIX: Actualizar el nombre del getter
    public int getChannelId() {
        return channelId;
    }

    // FIX: Actualizar el nombre del getter
    public String getChannelName() {
        return channelName;
    }

    // (El resto de la clase, como el método toString(), puede quedarse como está o ser actualizado también)
    @Override
    public String toString() {
        return "ChannelViewDTO{" +
               "channelId=" + channelId +
               ", channelName='" + channelName + '\'' +
               '}';
    }
}