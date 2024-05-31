package go.sock;

import go.Channel;
import go.Direction;
import go.Observer;
import log.Logger;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class MasterChannel<T> implements Channel<T>, Runnable {

    private final go.shm.Channel<T> shmChannel;

    private final String addr;
    private final int port;

    public MasterChannel(String name, String addr, int port) {
        this.shmChannel = new go.shm.Channel<>(name);
        this.addr = addr;
        this.port = port;
    }

    public void out(T v) {
        shmChannel.out(v);
    }

    public T in() {
        return shmChannel.in();
    }

    public String getName() {
        return shmChannel.getName();
    }

    public void observe(Direction direction, Observer observer) {
        // Ignore
    }

    public void run() {
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            Logger.info("MasterChannel started on " + port + "...");
            while (true) {
                Socket client = serverSocket.accept();
                Logger.info("Client connected: " + client.getInetAddress().getHostAddress() + ":" + client.getPort());
                new Thread(() -> handleConnection(client)).start();
            }
        } catch (IOException e) {
            Logger.error("Error at server socket creation", e);
        }
    }

    public String getAddr() {
        return addr;
    }

    public int getPort() {
        return port;
    }

    private void handleConnection(Socket client) {
        try (ObjectOutputStream output = new ObjectOutputStream(client.getOutputStream());
             ObjectInputStream input = new ObjectInputStream(client.getInputStream())) {

            String operation = (String) input.readObject();
            Logger.info("Received operation: " + operation);

            if ("in".equals(operation)) {
                T message = in();
                Logger.info("Sending message: " + message);
                output.writeObject(message);
                output.flush();
            } else if ("out".equals(operation)) {
                @SuppressWarnings("unchecked")
                T message = (T) input.readObject();
                Logger.info("Received message to out: " + message);
                out(message);
            } else {
                Logger.error("Unknown operation: " + operation);
            }
        } catch (IOException | ClassNotFoundException e) {
            Logger.error("Error in client communication", e);
        } finally {
            try {
                Logger.info("Closing client connection: " + client.getInetAddress().getHostAddress() + ":" + client.getPort());
                client.close();
            } catch (IOException e) {
                Logger.error("Error in client socket closing", e);
            }
        }
    }
}
