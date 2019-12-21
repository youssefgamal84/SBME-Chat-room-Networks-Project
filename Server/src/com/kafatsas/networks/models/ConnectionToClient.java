package com.kafatsas.networks.models;

import com.kafatsa.common.Message;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.List;

public class ConnectionToClient {
    private ObjectOutputStream oos;
    private ObjectInputStream ois;
    private BufferedInputStream bis;
    private Socket clientSocket;

    public ConnectionToClient(Socket clientSocket) throws IOException {
        this.oos = new ObjectOutputStream(clientSocket.getOutputStream());
        this.bis = new BufferedInputStream(clientSocket.getInputStream());
        this.ois = new ObjectInputStream(bis);
        this.clientSocket = clientSocket;
    }

    public boolean isMessageAvailable() {
        try {
            return this.bis.available() > 5;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public Message<?> receiveMessage() {
        Message message = null;
        try {
            message = (Message) this.ois.readObject();
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println(message);
        return message;
    }

    public void update(Message message) throws IOException {
        this.oos.writeObject(message);
        this.oos.flush();
    }

    public void update(List<Message> messages) {
        for (Message message : messages) {
            try {
                this.update(message);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public boolean isClosed() {
        return !this.clientSocket.isConnected();
    }
}
