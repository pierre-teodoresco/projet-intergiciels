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
        try (Socket server = new Socket(masterAddr, masterPort)) {
            Logger.info("Connexion établie avec " + server.getInetAddress().getHostAddress() + ":" + server.getPort());

            // Send function (string in)
            PrintWriter output = new PrintWriter(server.getOutputStream(), true);
            output.println("out");

            BufferedReader input = new BufferedReader(new InputStreamReader(server.getInputStream()));
            String ack = input.readLine();

            if (!ack.equals("ok")) {
                Logger.error("Erreur de communication avec le serveur", new Exception());
                return;
            }

            Logger.info("Envoi de la valeur dans le canal: " + v);

            try (ObjectOutputStream oos = new ObjectOutputStream(server.getOutputStream())) {
                oos.writeObject(v);
                oos.flush();
            } finally {
                server.close();
            }

        } catch (IOException e) {
            Logger.error("Erreur de communication avec le serveur", e);
        }
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
            } finally {
                server.close();
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
