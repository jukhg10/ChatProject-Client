package com.arquitectura.net;

import com.arquitectura.entidades.Message; // Placeholder for now
import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class ServerListener implements Runnable {

    private BufferedReader in;
    private final List<IServerObserver> observers = new ArrayList<>();
    private volatile boolean running = true;

    public ServerListener() {
        // Spring will create this bean, and we'll configure it later
    }

    public void setInputStream(BufferedReader in) {
        this.in = in;
    }

    // --- Observer Pattern Methods ---
    public void addObserver(IServerObserver observer) {
        observers.add(observer);
    }

    public void removeObserver(IServerObserver observer) {
        observers.remove(observer);
    }

    private void notifyMessageReceived(Message message) {
        for (IServerObserver observer : observers) {
            observer.onMessageReceived(message);
        }
    }
    // --- End Observer Methods ---


    @Override
    public void run() {
        try {
            String serverResponse;
            while (running && (serverResponse = in.readLine()) != null) {
                System.out.println("FROM SERVER: " + serverResponse);
                // Here, we will add logic to parse the serverResponse
                // and call the appropriate notify method.
                // For now, we just print it to the console.

                // Example of how it will work:
                // if (serverResponse.startsWith("NEW_MSG")) {
                //     Message msg = parseMessage(serverResponse);
                //     notifyMessageReceived(msg);
                // }
            }
        } catch (IOException e) {
            if (running) {
                System.err.println("Lost connection to the server: " + e.getMessage());
            }
        } finally {
            System.out.println("Server listener stopped.");
        }
    }

    public void stop() {
        running = false;
        try {
            if (in != null) {
                in.close();
            }
        } catch (IOException e) {
            // Ignore
        }
    }
}