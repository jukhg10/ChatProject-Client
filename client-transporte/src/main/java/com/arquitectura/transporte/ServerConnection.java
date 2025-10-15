package com.arquitectura.transporte;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import org.springframework.stereotype.Component;

@Component // Marks this as a Spring component so it can be injected
public class ServerConnection {

    private Socket socket;
    private PrintWriter out;
    private BufferedReader in;

    // Default constructor for Spring
    public ServerConnection() {}

    public void connect(String serverAddress, int port) throws IOException {
        socket = new Socket(serverAddress, port);
        out = new PrintWriter(socket.getOutputStream(), true);
        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        System.out.println("Connected to the server at " + serverAddress + ":" + port);
    }

    public void sendMessage(String message) {
        if (out != null) {
            out.println(message);
        }
    }

    public String receiveMessage() throws IOException {
        if (in != null) {
            return in.readLine();
        }
        return null;
    }

    public void disconnect() throws IOException {
        if (socket != null && !socket.isClosed()) {
            in.close();
            out.close();
            socket.close();
            System.out.println("Disconnected from the server.");
        }
    }
}