package com.kfatsa.networks;

import com.kafatsa.common.Message;

import java.io.*;
import java.net.Socket;

public class Client {
    private static final int PORT = 6666;
    private static final String IP = "localhost";
    private Socket clientSocket;
    private ObjectOutputStream oos;
    private ObjectInputStream ois;

    public Client() throws IOException {
        this.initializeConnection();
    }

    public void initializeConnection() throws IOException {
        clientSocket = new Socket(IP, PORT);
        oos = new ObjectOutputStream(new BufferedOutputStream(clientSocket.getOutputStream()));
        ois = new ObjectInputStream(new BufferedInputStream(clientSocket.getInputStream()));
        oos.writeObject(new Message<>());
        oos.flush();
    }

    public void send(Message message) {
        try {
            oos.writeObject(message);
            oos.flush();
            System.out.println("MESSAGE SENT");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean isAvailable() {
        try {
            return this.ois.available() > 0;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public Message readMessage(){
        try {
            Message message = (Message)this.ois.readObject();
            return message;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

}
