package go.sock;

import go.Channel;
import go.Direction;
import go.Selector;
import log.Logger;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Map;
import java.util.Set;

public class Factory implements go.Factory {

    public <T> Channel<T> newChannel(String name) {
        try (Socket socket = new Socket(Naming.SERVER_ADDRESS, Naming.SERVER_PORT)) {
            // Envoyer un message au serveur
            PrintWriter output = new PrintWriter(socket.getOutputStream(), true);
            output.println(name);

            // Lire l'objet renvoyé par le serveur
            ObjectInputStream input = new ObjectInputStream(socket.getInputStream());
            Channel<T> channel = (Channel<T>) input.readObject();

            // Fermer la connexion
            socket.close();

            if (channel instanceof MasterChannel) {
                new Thread((MasterChannel) channel).start();
            }

            return new go.sock.Channel(channel);

        } catch (IOException | ClassNotFoundException e) {
            Logger.error("Erreur de connexion au serveur", e);
        }
        return null;
    }

    public Selector newSelector(Map<Channel, Direction> channels) {
        // Ignore
        return null;
    }

    public Selector newSelector(Set<Channel> channels, Direction direction) {
        // Ignore
        return null;
    }
}
