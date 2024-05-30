package go.sock;

import go.Direction;
import go.Observer;
import log.Logger;

import java.io.*;
import java.net.Socket;

public class SlaveChannel<T> implements go.Channel<T> {

    private String name;

    // Socket
    private String masterAddr;
    private int masterPort;

    public SlaveChannel(String name, String masterAddr, int masterPort) {
        this.name = name;
        this.masterAddr = masterAddr;
        this.masterPort = masterPort;
    }

    public String getName() {
        return name;
    }

    public void out(T v) {
        // TODO
    }

    public T in() {
        try (Socket server = new Socket(masterAddr, masterPort)) {
            Logger.info("Connexion établie avec " + server.getInetAddress().getHostAddress() + ":" + server.getPort());

            // Send function (string in)
            PrintWriter output = new PrintWriter(server.getOutputStream(), true);
            output.println("in");

            Logger.info("En attente de la valeur dans le canal...");

            try (ObjectInputStream input = new ObjectInputStream(server.getInputStream())) {
                T result = (T) input.readObject();
                Logger.info("Valeur lue dans le canal: " + result);
                return result;
            }
        } catch (IOException | ClassNotFoundException e) {
            Logger.error("Erreur de communication avec le serveur", e);
            return null;
        }
    }

    public void observe(Direction direction, Observer observer) {
        // Ignore
    }
}
