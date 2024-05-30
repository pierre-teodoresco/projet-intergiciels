package go.sock;

import go.Channel;
import go.Direction;
import go.Observer;
import log.Logger;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.atomic.AtomicReference;

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
            Logger.info("Le MasterChannel est en attente de connexion sur le port " + port + "...");
            while (true) {
                try (Socket socket = serverSocket.accept()) {
                    Logger.info("Cookie connecté");

                    // Lire les données envoyées par le client
                    BufferedReader input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                    String function = input.readLine();

                    if (function.equals("out")) {
                        PrintWriter zoiper = new PrintWriter(socket.getOutputStream(), true);
                        zoiper.println("ok");

                        ObjectInputStream inputValue = new ObjectInputStream(socket.getInputStream());
                        T v = (T) inputValue.readObject();

                        new Thread(() -> shmChannel.out(v)).start();
                    } else if (function.equals("in")) {
                        AtomicReference<T> v = new AtomicReference<>();
                        new Thread(() -> v.set(shmChannel.in())).start();

                        ObjectOutputStream output = new ObjectOutputStream(socket.getOutputStream());
                        output.writeObject(v.get());
                    }
                } catch (IOException e) {
                    Logger.error("MasterChannel", e);
                }
            }
        } catch (Exception e) {
            Logger.error("MasterChannel", e);
        }
    }

    public String getAddr() {
        return addr;
    }

    public int getPort() {
        return port;
    }
}
