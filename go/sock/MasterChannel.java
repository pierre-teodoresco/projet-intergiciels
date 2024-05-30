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
            Logger.info("Le MasterChannel est en attente de connexion sur le port " + port + "...");
            while (true) {
                try (Socket client = serverSocket.accept()) {
                    Logger.info("Connexion établie avec " + client.getInetAddress().getHostAddress() + ":" + client.getPort());

                    // Read function (string in/out)
                    BufferedReader input = new BufferedReader(new InputStreamReader(client.getInputStream()));
                    String function = input.readLine();

                    Logger.info("Fonction = " + function);

                    if (function.equals("out")) {
                        // TODO
                    } else if (function.equals("in")) {
                        new Thread(() -> handleIn(client)).start();
                    } else {
                        Logger.error("MasterChannel", new Exception("Fonction inconnue"));
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

    private void handleIn(Socket client) {
        T v = in();
        Logger.info("Valeur lue dans le canal: " + v);
        try {
            ObjectOutputStream output = new ObjectOutputStream(client.getOutputStream());
            output.writeObject(v);
            output.flush();
            Logger.info("Valeur envoyée au client");
        } catch (IOException e) {
            Logger.error("Erreur lors de l'envoi de la réponse", e);
        } finally {
            try {
                client.close(); // Assurez-vous de fermer la socket après l'envoi de la réponse
            } catch (IOException e) {
                Logger.error("Erreur lors de la fermeture de la socket client", e);
            }
        }
    }
}
