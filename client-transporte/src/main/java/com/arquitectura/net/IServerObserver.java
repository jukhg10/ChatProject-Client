package com.arquitectura.net;

import com.arquitectura.entidades.Channel;
import com.arquitectura.entidades.Message;
import com.arquitectura.entidades.User;
import java.util.List;

public interface IServerObserver {

    void onMessageReceived(Message message);

    void onUserListUpdated(List<User> users);

    void onChannelListUpdated(List<Channel> channels);

}