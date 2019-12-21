package com.kafatsas.networks;

import com.kafatsa.common.Message;
import com.kafatsas.networks.models.ConnectionToClient;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.Semaphore;

public class Server {
    private static List<ConnectionToClient> clients = new ArrayList<>();
    private static List<Message> messages = new ArrayList<>();
    private static Semaphore semaphore = new Semaphore(1, true);

    public static void notifyClients(Message message) {
        if (message == null) return;
        Iterator<ConnectionToClient> iter = clients.iterator();
        while(iter.hasNext()){
            ConnectionToClient client = iter.next();
            try {
                client.update(message);
            } catch (IOException e) {
                e.printStackTrace();
                iter.remove();
            }
        }
        messages.add(message);
    }

    public static void main(String[] args) {
        ServerSocket serverSocket = null;
        try {
            serverSocket = new ServerSocket(6666);
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(-1);
        }

        Thread thread = new Thread(() -> {
            while (true) {
                System.out.println("checking for messages");
                try {
                    semaphore.acquire();
                } catch (InterruptedException e) {
                    continue;
                }

                System.out.println(clients.size());
                Iterator<ConnectionToClient> iter = clients.iterator();
                while(iter.hasNext()) {
                    ConnectionToClient client = iter.next();
                    try{
                        Message message = null;
                        if(client.isMessageAvailable()){
                            System.out.println("MESSAGE IS AVAILABLE");
                            message = client.receiveMessage();
                        }
                        if(message != null){
                            notifyClients(message);
                            break;
                        }
                    } catch(Exception e){
                        e.printStackTrace();
                    }
                }
                semaphore.release();
            }
        });
        thread.start();

        while (true) {
            System.out.println("CHECKING FOR NEW CLIENTS");
            Socket clientSocket = null;
            try {
                clientSocket = serverSocket.accept();
            } catch (IOException e) {
                e.printStackTrace();
            }
            System.out.println("CLIENT CONNECTED");
            try {
                semaphore.acquire();
            } catch (InterruptedException e) {
                e.printStackTrace();
                continue;
            }
            ConnectionToClient client = null;
            try {
                client = new ConnectionToClient(clientSocket);
            } catch (IOException e) {
                e.printStackTrace();
            }
            clients.add(client);
            client.update(messages);
            semaphore.release();
        }
    }
}
